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
 * <td>userId</td>
 * <td>required</td>
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
 * <td>creds</td>
 * <td>required</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>ignored</td>
 * <td>required</td>
 * <td>required</td>
 * </tr>
 * <tr>
 * <td>name</td>
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
 * </table>
 */
public final class UserInboundDto extends BaseInboundDto<UserInboundDto> {

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

		final String requestType = getRequestType();

		switch (requestType) {
		case RequestTypeHolder.ADD:
			deactivated = false;

			validateUserId();
			validateLogin();
			validateCreds();
			validateName();
			validateDescription();
			break;
		case RequestTypeHolder.DELETE:
			deactivated = true;

			validateUserId();
			break;
		case RequestTypeHolder.GET:
			validateUserId();
			break;
		case RequestTypeHolder.LIST:
			break;
		case RequestTypeHolder.UPDATE:
			validateUserId();
			validateLogin();
			validateCreds();
			validateName();
			validateDescription();
			break;
		case UserRequestTypeHolder.UPDATE_CREDS:
			validateUserId();
			validateLogin();
			validateCreds();
			validateName();
			validateDescription();
			break;
		default:
			if (!requestType.isEmpty()) {
				addValidationError("Unknown request type: " + requestType);
			}
		}
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

	public UserInboundDto userId(long userIdParm) {
		checkNotBuilt();

		userId = userIdParm;

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

	public UserInboundDto creds(String credsParm) {
		checkNotBuilt();

		creds = credsParm;

		return this;
	}

	public UserInboundDto name(String nameParm) {
		checkNotBuilt();

		name = nameParm;

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
}
