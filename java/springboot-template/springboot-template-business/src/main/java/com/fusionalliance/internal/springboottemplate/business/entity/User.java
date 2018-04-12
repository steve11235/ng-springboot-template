/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 9, 2018
 */
package com.fusionalliance.internal.springboottemplate.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;
import com.fusionalliance.internal.sharedspringboot.business.PersistenceEntity;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.springboottemplate.business.dao.UserDao;

/**
 * This class implements the User entity.
 * <p>
 * <b>Note:</b> The use of fluent setters is for convenience; there is no <code>build()</code> method, and instances are mutable.
 * <p>
 * "user" is a system table, so qualify the table name
 */
@Entity
@Table(schema = "sb_template", name = "user_def")
public class User implements PersistenceEntity {

	@Id
	@GeneratedValue
	@Column(name = "user_key", unique = true, nullable = false)
	private long userKey;

	@Version
	@Column(nullable = false)
	private long version;

	private boolean deactivated;

	@NaturalId(mutable = true)
	@Column(unique = true, nullable = false)
	private String login;

	@Column(nullable = false)
	private String creds;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(nullable = false)
	private String description;

	private boolean admin;

	@Override
	public boolean validate() {
		boolean valid = true;

		if (StringUtils.isBlank(login)) {
			valid = false;
			MessageManager.addError("The login is blank.");
		}
		if (StringUtils.isBlank(creds)) {
			valid = false;
			MessageManager.addError("The creds is blank.");
		}
		if (StringUtils.isBlank(fullName)) {
			valid = false;
			MessageManager.addError("The full name is blank.");
		}
		if (StringUtils.isBlank(description)) {
			valid = false;
			MessageManager.addError("The description is blank.");
		}

		if (!checkLoginUnique()) {
			valid = false;
			MessageManager.addError("The login '%1$s' is already in use. (The user may be deactivated.)", login);
		}

		return valid;
	}

	/**
	 * Check if the login is unique. This requires database access.
	 * 
	 * @return
	 */
	private boolean checkLoginUnique() {
		final UserDao userDao = SpringContextHelper.getBeanByClass(UserDao.class);

		final boolean loginUnique = userDao.checkLoginUnique(this);

		return loginUnique;
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

	public String getCreds() {
		return creds;
	}

	public String getFullName() {
		return fullName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isAdmin() {
		return admin;
	}

	public User userKey(long userKeyParm) {
		userKey = userKeyParm;

		return this;
	}

	public User deactivated(boolean deactivatedParm) {
		deactivated = deactivatedParm;

		return this;
	}

	public User login(String loginParm) {
		login = loginParm;

		return this;
	}

	public User creds(String credsParm) {
		creds = credsParm;

		return this;
	}

	public User fullName(String fullNameParm) {
		fullName = fullNameParm;

		return this;
	}

	public User description(String descriptionParm) {
		description = descriptionParm;

		return this;
	}

	public User admin(boolean adminParm) {
		admin = adminParm;

		return this;
	}
}
