package com.fusionalliance.internal.jwt.business;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.jwt.filter.JwtAuthenticationFilter;
import com.fusionalliance.internal.sharedutility.core.GenericCastUtility;
import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.core.ValidationException;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.jwt.JwtException;
import com.fusionalliance.internal.sharedutility.jwt.JwtUtility;

/**
 * This class implements an immutable JSON Web Token (JWT) with a payload that conforms to the expectations of the provider and known consumers (see
 * {@link JwtAuthenticationFilter}). Instances can be built by passing a JWT string to a constructor, or properties can be specified using fluent
 * setters followed by {@link #build()}.
 * <p>
 * Instances are immutable. If a getter or {@link #retrieveJwtPayload()} are called before {@link #build()} or a fluent setter is called after
 * {@link #build()}, then a {@link ValidationException} is thrown.
 */
public class JwtImpl {
	private String name;
	private long expires;
	private String login;
	private boolean admin;
	private String secretId;

	private transient String header;
	private transient String payload;
	private transient String signature;

	private transient boolean built;

	/**
	 * Constructor for use with fluent setters.
	 */
	public JwtImpl() {
		// Do nothing
	}

	/**
	 * Constructor from JWT string. The JWT is validated, and values expected to be in the payload are extracted and validated. However, the signature
	 * <i>is not</i> checked, as that requires access to the secret used to create it, which in turn requires the secret ID.
	 */
	public JwtImpl(final String jwtParm) throws JwtException {
		final Map<String, Object> payloadMap;
		payloadMap = JwtUtility.retrieveTokenPayload(jwtParm);

		header = GenericCastUtility.cast(payloadMap.get(JwtUtility.TOKEN_HEADER));
		payload = GenericCastUtility.cast(payloadMap.get(JwtUtility.TOKEN_PAYLOAD));
		signature = GenericCastUtility.cast(payloadMap.get(JwtUtility.TOKEN_SIGNATURE));

		final Number expiresAsNumber = GenericCastUtility.castOrNull(payloadMap.get("exp"));
		if (expiresAsNumber == null) {
			throw new JwtException("JWT expiration missing or invalid.");
		}

		expires = expiresAsNumber.longValue();

		login = GenericCastUtility.castOrNull(payloadMap.get("lid"));
		name = GenericCastUtility.castOrNull(payloadMap.get("name"));
		admin = Boolean.TRUE.equals(GenericCastUtility.castOrNull(payloadMap.get("admin")));

		secretId = GenericCastUtility.castOrNull(payloadMap.get("sid"));

		built = true;

		validate();
	}

	public String getName() {
		throwIfNotBuilt();

		return name;
	}

	public long getExpires() {
		throwIfNotBuilt();

		return expires;
	}

	public String getLogin() {
		throwIfNotBuilt();

		return login;
	}

	public boolean isAdmin() {
		throwIfNotBuilt();

		return admin;
	}

	public String getSecretId() {
		throwIfNotBuilt();

		return secretId;
	}

	/**
	 * Return the header if the instance was constructed by passing a JWT, or null.
	 * 
	 * @return
	 */
	public String getHeader() {
		throwIfNotBuilt();

		return header;
	}

	/**
	 * Return the payload.
	 * <p>
	 * If the instance was constructed by passing a JWT, this is the payload JSON as passed. Otherwise, it is the payload constructed from the
	 * instance properties.
	 * 
	 * @return
	 */
	public String getPayload() {
		throwIfNotBuilt();

		return payload;
	}

	/**
	 * Return the signature if the instance was constructed by passing a JWT, or null.
	 * 
	 * @return
	 */
	public String getSignature() {
		throwIfNotBuilt();

		return signature;
	}

	public JwtImpl name(final String nameParm) {
		throwIfBuilt();

		name = nameParm;

		return this;
	}

	public JwtImpl expires(final long expiresParm) {
		throwIfBuilt();

		expires = expiresParm;

		return this;
	}

	public JwtImpl login(final String loginParm) {
		throwIfBuilt();

		login = loginParm;

		return this;
	}

	/**
	 * Set the value of admin. This is optional, and admin defaults to false.
	 * 
	 * @param adminParm
	 * @return
	 */
	public JwtImpl admin(final boolean adminParm) {
		throwIfBuilt();

		admin = adminParm;

		return this;
	}

	public JwtImpl secretId(final String secretIdParm) {
		throwIfBuilt();

		secretId = secretIdParm;

		return this;
	}

	/**
	 * Validate the contents of the JWT and make the instance immutable.
	 * 
	 * @return
	 * @throws JwtException
	 *             If any required payload entry value is missing.
	 */
	public JwtImpl build() throws JwtException {

		// Build the payload JSON by serializing the non-transient properties of the instance
		payload = GsonHelper.GSON.toJson(this);

		built = true;

		validate();

		return this;
	}

	/**
	 * Validate the instance.
	 * 
	 * @throws JwtException
	 *             If any required payload field value is missing.
	 */
	private void validate() throws JwtException {
		throwIfNotBuilt();

		// expires is in seconds, per JWT standard
		if (expires < System.currentTimeMillis() / 1000) {
			throw new JwtException("JWT is expired.");
		}

		if (StringUtils.isBlank(login)) {
			throw new JwtException("JWT lid (login) is missing.");
		}

		if (StringUtils.isBlank(name)) {
			throw new JwtException("JWT name is missing.");
		}

		if (StringUtils.isBlank(secretId)) {
			throw new JwtException("JWT sid (secret ID) is missing.");
		}
	}

	private void throwIfNotBuilt() {
		ValidationUtility.checkGoodConditionMet("Attempt to access getter when build() has not been called.", built);
	}

	private void throwIfBuilt() {
		ValidationUtility.checkBadConditionNotMet("Attempt to access fluent setter when build() has been called.", built);
	}
}
