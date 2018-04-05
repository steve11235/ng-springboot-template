package com.fusionalliance.internal.jwt.api;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedspringboot.api.BaseInboundDto;

public class AuthorizationInboundDto extends BaseInboundDto<AuthorizationInboundDto> {
	private String login;
	private String creds;

	@Override
	public void validate() {
		super.validate();

		validateLogin();
		validateCreds();
	}

	private void validateLogin() {
		if (StringUtils.isBlank(login)) {
			addValidationError("Login is blank");
		}
	}

	private void validateCreds() {
		if (StringUtils.isBlank(creds)) {
			addValidationError("Creds is blank");
		}
	}

	public String getLogin() {
		return login;
	}

	public String getCreds() {
		return creds;
	}

	public AuthorizationInboundDto login(final String loginParm) {
		checkNotBuilt();

		login = loginParm;

		return fetchThisAsT();
	}

	public AuthorizationInboundDto creds(final String credsParm) {
		checkNotBuilt();

		creds = credsParm;

		return fetchThisAsT();
	}
}
