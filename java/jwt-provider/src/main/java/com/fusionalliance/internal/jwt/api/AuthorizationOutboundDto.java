package com.fusionalliance.internal.jwt.api;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;

public class AuthorizationOutboundDto extends BaseOutboundDto<AuthorizationOutboundDto> {

	/** Login identifier */
	private String login;
	/** User name */
	private String name;
	/** Is admin? Note: OK for POC, but raises security concerns */
	private boolean admin;
	/** Expiration time in seconds */
	private long exp;
	/** JWT */
	private String jwt;

	@Override
	public void validate() {
		super.validate();
		validateLogin();
		validateName();
		validateExp();
		validateJwt();
	}

	private void validateLogin() {
		if (StringUtils.isBlank(login)) {
			addValidationError("Login is blank");
		}
	}

	private void validateName() {
		if (StringUtils.isBlank(name)) {
			addValidationError("Name is blank");
		}
	}

	private void validateExp() {
		final long nowMillis = System.currentTimeMillis();
		final long nowSeconds = nowMillis / 1000;

		if (exp < nowSeconds) {
			addValidationError("Exp seconds is in the past");
		}

		if (exp > nowMillis) {
			addValidationError("Exp seconds is too large (passed millis?)");
		}
	}

	private void validateJwt() {
		if (StringUtils.isBlank(jwt)) {
			addValidationError("JWT is blank");
		}
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin() {
		return admin;
	}

	public long getExp() {
		return exp;
	}

	public String getJwt() {
		return jwt;
	}

	public AuthorizationOutboundDto login(final String loginParm) {
		checkNotBuilt();

		login = loginParm;

		return this;
	}

	public AuthorizationOutboundDto name(final String nameParm) {
		checkNotBuilt();

		name = nameParm;

		return this;
	}

	public AuthorizationOutboundDto admin(final boolean adminParm) {
		checkNotBuilt();

		admin = adminParm;

		return this;
	}

	public AuthorizationOutboundDto exp(final long expParm) {
		checkNotBuilt();

		exp = expParm;

		return this;
	}

	public AuthorizationOutboundDto jwt(final String jwtParm) {
		checkNotBuilt();

		jwt = jwtParm;

		return this;
	}
}
