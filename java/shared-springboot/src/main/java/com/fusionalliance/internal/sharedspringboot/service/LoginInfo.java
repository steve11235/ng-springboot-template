/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 11, 2018
 */
package com.fusionalliance.internal.sharedspringboot.service;

import org.apache.commons.lang3.StringUtils;

/**
 * This class is an immutable value object for login information. Typically, it is created by an authorization filter and attached as a ServletRequest
 * attribute.
 */
public class LoginInfo {

	private final String login;
	private final String userName;
	private final boolean admin;

	/**
	 * Constructor
	 * 
	 * @param loginParm
	 *            optional, defaults to EMPTY
	 * @param userNameParm
	 *            optional, defaults to "anonymous"
	 * @param adminParm
	 *            pass <code>false</code> by default
	 */
	public LoginInfo(final String loginParm, final String userNameParm, final boolean adminParm) {
		login = StringUtils.isNotBlank(loginParm) ? loginParm : StringUtils.EMPTY;
		userName = StringUtils.isNotBlank(userNameParm) ? userNameParm : "anonymous";
		admin = adminParm;
	}

	public String getLogin() {
		return login;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isAdmin() {
		return admin;
	}
}
