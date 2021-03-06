/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 6, 2018
 */
package com.fusionalliance.internal.springboottemplate.api.user;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedspringboot.api.BaseInboundDto;
import com.fusionalliance.internal.sharedspringboot.api.RequestTypeHolder;

/**
 * This class implements an inbound DTO for User.
 * <p>
 * <table border="1" cellpadding="3" cellspacing="0">
 * <tr>
 * <th>&nbsp;</th>
 * <th>ADD</th>
 * <th>DELETE</th>
 * <th>GET</th>
 * <th>LIST</th>
 * <th>UPDATE</th>
 * <th>UPDATE_CREDS</th>
 * </tr>
 * <tr>
 * <td>userKey</td>
 * <td>ignored, treated as 0</td>
 * <td>required</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>required</td>
 * <td>required</td>
 * </tr>
 * <tr>
 * <td>deactivated</td>
 * <td>ignored, treated as false</td>
 * <td>ignored, treated as true</td>
 * <td>ignored</td>
 * <td>if true, deactivated rows are returned as well</td>
 * <td>optional, false</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>login</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>fullName</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>description</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>admin</td>
 * <td>optional, false</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>if true, only admin true rows are returned</td>
 * <td>optional, false</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>existingCreds</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * </tr>
 * <tr>
 * <td>updateToCreds</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * </tr>
 * </table>
 */
public final class UserInboundDto extends BaseInboundDto<UserInboundDto> {

	private long userKey;
	/** Has this user been deactivated? */
	private boolean deactivated;
	/** The unique value supplied on the login screen */
	private String login;
	/** Full fullName */
	private String fullName;
	private String description;
	private boolean admin;
	/** Hashed password (current) */
	private String existingCreds;
	/** Hashed password (update to) */
	private String updateToCreds;

	@Override
	public void validate() {
		super.validate();

		final String requestType = getRequestType();

		switch (requestType) {
		case RequestTypeHolder.ADD:
			userKey = 0;
			deactivated = false;

			validateLogin();
			validateExistingCreds();
			validateFullfullName();
			validateDescription();
			validateUpdateToCreds();
			break;
		case RequestTypeHolder.DELETE:
			deactivated = true;

			validateUserKey();
			break;
		case RequestTypeHolder.GET:
			validateUserKey();
			break;
		case RequestTypeHolder.LIST:
			break;
		case RequestTypeHolder.UPDATE:
			validateUserKey();
			validateLogin();
			validateFullfullName();
			validateDescription();
			break;
		case UserRequestTypeHolder.UPDATE_CREDS:
			validateUserKey();
			validateExistingCreds();
			validateUpdateToCreds();
			break;
		default:
			if (!requestType.isEmpty()) {
				addValidationError("Unknown request type: " + requestType);
			}
		}
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

	void validateExistingCreds() {
		if (StringUtils.isBlank(existingCreds)) {
			addValidationError("Existing existingCreds is blank");
		}
	}

	void validateUpdateToCreds() {
		if (StringUtils.isBlank(updateToCreds)) {
			addValidationError("Update-to existingCreds is blank");
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

	public String getExistingCreds() {
		return existingCreds;
	}

	public String getUpdateToCreds() {
		return updateToCreds;
	}

	public UserInboundDto userKey(long userKeyParm) {
		checkNotBuilt();

		userKey = userKeyParm;

		return this;
	}

	public UserInboundDto deactivated(boolean deactivatedParm) {
		checkNotBuilt();

		deactivated = deactivatedParm;

		return this;
	}

	public UserInboundDto login(String loginParm) {
		checkNotBuilt();

		login = loginParm;

		return this;
	}

	public UserInboundDto fullName(String fullNameParm) {
		checkNotBuilt();

		fullName = fullNameParm;

		return this;
	}

	public UserInboundDto description(String descriptionParm) {
		checkNotBuilt();

		description = descriptionParm;

		return this;
	}

	public UserInboundDto admin(boolean adminParm) {
		checkNotBuilt();

		admin = adminParm;

		return this;
	}

	public UserInboundDto existingCreds(String credsParm) {
		checkNotBuilt();

		existingCreds = credsParm;

		return this;
	}

	public UserInboundDto updateToCreds(String credsParm) {
		checkNotBuilt();

		existingCreds = credsParm;

		return this;
	}
}
