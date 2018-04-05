package com.fusionalliance.internal.jwt.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.fusionalliance.internal.jwt.api.AuthorizationInboundDto;
import com.fusionalliance.internal.jwt.api.JwtOutboundDto;
import com.fusionalliance.internal.jwt.shared.SecretContainer;
import com.fusionalliance.internal.jwt.shared.SecretContainer.SecretInfo;
import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.jwt.JwtException;
import com.fusionalliance.internal.sharedutility.jwt.JwtUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;

public class AuthorizationProcessor extends BusinessProcessor<AuthorizationInboundDto> {

	private static final int EXPIRATION_SECONDS = 30 * 60;

	public AuthorizationProcessor(AuthorizationInboundDto inboundDtoParm) {
		super(inboundDtoParm);
	}

	@Override
	protected BaseOutboundDto<?> process() throws ApplicationException {
		final AuthorizationInboundDto inboundDto = getInboundDto();

		final Map<String, String> userInfoMap;
		try {
			userInfoMap = retrieveUserInfo(inboundDto.getLogin(), inboundDto.getCreds());
		}
		catch (Exception e) {
			throw new ApplicationException("Unable to access database.", e);
		}

		if (userInfoMap == null) {
			MessageManager.addError("Unable to authenticate user login: " + inboundDto.getLogin());

			return new MessagesOnlyOutboundDto();
		}

		final SecretContainer secretContainer = SpringContextHelper.getBeanByClass(SecretContainer.class);
		final SecretInfo secretInfo = secretContainer.getCurrentSecretInfo();

		final JwtImpl jwtImpl;
		try {
			jwtImpl = new JwtImpl() //
					.admin(Boolean.parseBoolean(userInfoMap.get("admin"))) //
					.expires((System.currentTimeMillis() / 1000) + EXPIRATION_SECONDS) //
					.login(inboundDto.getLogin()) //
					.name(userInfoMap.get("name")) //
					.secretId(secretInfo.getSecretId()) //
					.build();
		}
		catch (JwtException e) {
			throw new ApplicationException("Unexpected JWT error: " + e.getMessage());
		}

		final String jwt = JwtUtility.buildToken(jwtImpl.getPayload(), secretInfo.getSecret());

		final BaseOutboundDto<?> outboundDto = new JwtOutboundDto() //
				.admin(jwtImpl.isAdmin()) //
				.exp(jwtImpl.getExpires()) //
				.jwt(jwt) //
				.login(jwtImpl.getLogin()) //
				.name(jwtImpl.getName()) //
				.build();

		MessageManager.addInfo("User login %1$s validated.", inboundDto.getLogin());

		return outboundDto;
	}

	/**
	 * Retrieve user info from the Interviewer table.
	 * <p>
	 * Results are returned in a Map with two entries, "name" and "admin". "admin" is "true" or "false".
	 * 
	 * @param loginParm
	 * @param credsParm
	 * @return null if an active Interviewer is not found
	 * @throws Exception
	 *             if unable to access the user info
	 */
	private Map<String, String> retrieveUserInfo(String loginParm, String credsParm) throws Exception {
		final DataSource dataSource = SpringContextHelper.getBeanByName("postgreSqlDataSource", DataSource.class);

		try (
				final Connection connection = dataSource.getConnection();
				final PreparedStatement statement = connection.prepareStatement("" //
						+ "select interviewer_name, admin " //
						+ "from interview.interviewer " //
						+ "where login = ? and creds = ? and deactivated = false" //
				); //
		) {
			statement.setString(1, loginParm);
			statement.setString(2, credsParm);

			final ResultSet resultSet = statement.executeQuery();

			if (!resultSet.next()) {
				return null;
			}

			final Map<String, String> userInfoMap = new HashMap<>();
			userInfoMap.put("name", resultSet.getString(1));
			userInfoMap.put("admin", Boolean.toString(resultSet.getBoolean(2)));

			return userInfoMap;
		}
	}
}
