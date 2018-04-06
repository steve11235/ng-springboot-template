/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 6, 2018
 */
package com.fusionalliance.internal.springboottemplate.api.user;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;

/**
 * This class implements an outbound DTO for user.
 */
public class UserOutboundDto extends BaseOutboundDto<UserOutboundDto> {

	private long userId;
	/** Has this user been deactivated? */
	private boolean deactivated;
	/** The unique value supplied on the login screen */
	private String login;
	/** Hashed password */
	private String creds;
	/** Full name */
	private String name;
	private String description;
	private boolean admin;

	@Override
	public void validate() {
		super.validate();

		validateUserId();
		validateLogin();
		validateCreds();
		validateName();
		validateDescription();
	}

	void validateUserId() {
		if (userId <= 0) {
			addValidationError("User ID is less than or equal to 0");
		}
	}

	void validateLogin() {
		if (StringUtils.isBlank(login)) {
			addValidationError("Login is blank");
		}
	}

	void validateCreds() {
		if (StringUtils.isBlank(creds)) {
			addValidationError("Creds is blank");
		}
	}

	void validateName() {
		if (StringUtils.isBlank(name)) {
			addValidationError("Name is blank");
		}
	}

	void validateDescription() {
		if (StringUtils.isBlank(description)) {
			addValidationError("Description is blank");
		}
	}

	public long getUserId() {
		return userId;
	}

	public boolean isDeactivated() {
		return deactivated;
	}

	public String getLogin() {
		return login;
	}

	public String getCreds() {
		return creds;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isAdmin() {
		return admin;
	}

	public UserOutboundDto userId(long userIdParm) {
		checkNotBuilt();

		userId = userIdParm;

		return this;
	}

	public UserOutboundDto deactivated(boolean deactivatedParm) {
		checkNotBuilt();

		deactivated = deactivatedParm;

		return this;
	}

	public UserOutboundDto login(String loginParm) {
		checkNotBuilt();

		login = loginParm;

		return this;
	}

	public UserOutboundDto creds(String credsParm) {
		checkNotBuilt();

		creds = credsParm;

		return this;
	}

	public UserOutboundDto name(String nameParm) {
		checkNotBuilt();

		name = nameParm;

		return this;
	}

	public UserOutboundDto description(String descriptionParm) {
		checkNotBuilt();

		description = descriptionParm;

		return this;
	}

	public UserOutboundDto admin(boolean adminParm) {
		checkNotBuilt();

		admin = adminParm;

		return this;
	}
}
