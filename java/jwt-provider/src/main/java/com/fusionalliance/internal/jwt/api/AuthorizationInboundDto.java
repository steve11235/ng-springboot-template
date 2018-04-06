package com.fusionalliance.internal.jwt.api;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedspringboot.api.BaseInboundDto;
import com.fusionalliance.internal.sharedspringboot.api.RequestTypeHolder;

/**
 * This class implements an inbound DTO for authorization requests. Note that only the GET request type is allowed.
 */
public class AuthorizationInboundDto extends BaseInboundDto<AuthorizationInboundDto> {

	private String login;
	private String creds;

	@Override
	public void validate() {
		super.validate();

		if (getRequestType().equals(RequestTypeHolder.GET)) {
			validateLogin();
			validateCreds();

			return;
		}

		addValidationError("Invalid request type: " + getRequestType());
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

		return this;
	}

	public AuthorizationInboundDto creds(final String credsParm) {
		checkNotBuilt();

		creds = credsParm;

		return this;
	}
}
