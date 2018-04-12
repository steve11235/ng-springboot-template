/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 9, 2018
 */
package com.fusionalliance.internal.springboottemplate.business.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.GenericCastUtility;
import com.fusionalliance.internal.sharedutility.core.LoggerUtility;
import com.fusionalliance.internal.sharedutility.core.TreatAsRestricted;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.springboottemplate.business.entity.User;

/**
 * This class implements DAO operations for the User entity.
 */
@Service("userDao")
public class UserDao {
	private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

	private final DefaultDao defaultDao;

	/**
	 * Constructor
	 * 
	 * @param defaultDaoParm
	 *            autowired
	 */
	public UserDao(@Autowired final DefaultDao defaultDaoParm) {
		defaultDao = defaultDaoParm;
	}

	/**
	 * Return true if the login for the User passed is unique.
	 * <p>
	 * Return false if the User is null or the login is blank.
	 * 
	 * @param userParm
	 *            required
	 * @return
	 */
	public boolean checkLoginUnique(final User userParm) {
		if (userParm == null) {
			return false;
		}
		if (StringUtils.isBlank(userParm.getLogin())) {
			return false;
		}

		final String query = String.join(" ", //
				"select count(*) rowCount", //
				"from sb_template.user_def", //
				"where login = :login", //
				"  and user_key <> :userKey" //
		);

		try {
			final NativeQuery<?> nativeQuery = defaultDao.retrieveCurrentSession() //
					.createNativeQuery(query) //
					.setParameter("login", userParm.getLogin()) //
					.setParameter("userKey", userParm.getUserKey()) //
					.setReadOnly(true);

			final BigInteger rows = (BigInteger) nativeQuery.uniqueResult();

			return rows.equals(BigInteger.ZERO);
		}
		catch (final Exception e) {
			LoggerUtility.logException(LOG, "A Hibernate error occurred.", e);
			MessageManager.addSystem();

			throw new ApplicationException("A Hibernate error occurred.");
		}
	}

	/**
	 * Return a list of User ordered by login. These entities are read-only.
	 * 
	 * @param includeDeactivatedParm
	 *            if true, return deactivated rows as well
	 * @param includeAdminOnlyParm
	 *            if true, return only admin = true rows
	 * @return
	 */
	public List<User> retrieveUserList(final boolean includeDeactivatedParm, final boolean includeAdminOnlyParm) {
		final String query = String.join(" ", //
				"select *", //
				"from sb_template.user_def", //
				"where (:includeDeactivated or not deactivated)", //
				"  and (not :includeAdminOnly or admin)", //
				"order by login" //
		);

		try {
			final NativeQuery<User> nativeQuery = defaultDao.retrieveCurrentSession() //
					.createNativeQuery(query, User.class) //
					.setParameter("includeDeactivated", includeDeactivatedParm) //
					.setParameter("includeAdminOnly", includeAdminOnlyParm) //
					.setReadOnly(true);

			final List<User> userList = GenericCastUtility.cast(nativeQuery.list());

			return userList;
		}
		catch (final Exception e) {
			LoggerUtility.logException(LOG, "A Hibernate error occurred.", e);
			MessageManager.addSystem();

			throw new ApplicationException("A Hibernate error occurred.");
		}
	}

	/**
	 * Generate the admin user, if it does not exist.
	 * <p>
	 * This method should only be called by the Spring context initialed listener.
	 */
	@TreatAsRestricted("For use by Spring context initialized listener only")
	public void generateAdminAccount() {
		final String query = String.join(" ", //
				"select count(*) rowCount", //
				"from sb_template.user_def", //
				"where login = 'admin'" //
		);

		try {
			final NativeQuery<?> nativeQuery = defaultDao.retrieveCurrentSession() //
					.createNativeQuery(query) //
					.setReadOnly(true);

			final Integer rows = (Integer) nativeQuery.uniqueResult();

			// This is the normal case
			if (rows == 1) {
				return;
			}
		}
		catch (final Exception e) {
			LoggerUtility.logException(LOG, "A Hibernate error occurred.", e);
			MessageManager.addSystem();

			throw new ApplicationException("A Hibernate error occurred.");
		}

		// The initial value of the admin password is special and should be changed immediately
		final User adminUser = new User() //
				.admin(true) //
				.creds("**initial**") //
				.deactivated(false) //
				.description("Administrator account") //
				.fullName("Admininistrator") //
				.login("admin") //
		;

		defaultDao.persist(adminUser);
	}
}
