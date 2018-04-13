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

	private long userKey;
	/** Has this user been deactivated? */
	private boolean deactivated;
	/** The unique value supplied on the login screen */
	private String login;
	// private String creds; intentionally omitted
	/** Full fullName */
	private String fullName;
	private String description;
	private boolean admin;

	@Override
	public void validate() {
		super.validate();

		validateUserKey();
		validateLogin();
		validateFullfullName();
		validateDescription();
	}

	void validateUserKey() {
		if (userKey <= 0) {
			addValidationError("User ID is less than or equal to 0");
		}
	}

	void validateLogin() {
		if (StringUtils.isBlank(login)) {
			addValidationError("Login is blank");
		}
	}

	void validateFullfullName() {
		if (StringUtils.isBlank(fullName)) {
			addValidationError("FullfullName is blank");
		}
	}

	void validateDescription() {
		if (StringUtils.isBlank(description)) {
			addValidationError("Description is blank");
		}
	}

	public long getUserKey() {
		return userKey;
	}

	public boolean isDeactivated() {
		return deactivated;
	}

	public String getLogin() {
		return login;
	}

	public String getFullfullName() {
		return fullName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isAdmin() {
		return admin;
	}

	public UserOutboundDto userKey(long userKeyParm) {
		checkNotBuilt();

		userKey = userKeyParm;

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

	public UserOutboundDto fullName(String fullNameParm) {
		checkNotBuilt();

		fullName = fullNameParm;

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
