/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 9, 2018
 */
package com.fusionalliance.internal.springboottemplate.business.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fusionalliance.internal.sharedspringboot.business.BaseHibernateDao;

/**
 * This class provides base DAO support for the default session. In particular, it implements {@link #retrieveCurrentSession()}. An instance of this
 * class can be used for generic operations. However, entity-specific code should be placed in classes that inject this class, in order to avoid a
 * monolith of low cohesion methods.
 */
@Service("defaultDao")
public class DefaultDao extends BaseHibernateDao {

	private final SessionFactory defaultSessionFactory;

	/**
	 * Constructor
	 * 
	 * @param defaultSessionFactoryParm
	 *            autowired
	 */
	public DefaultDao(@Autowired @Qualifier("defaultSessionFactory") final SessionFactory defaultSessionFactoryParm) {
		defaultSessionFactory = defaultSessionFactoryParm;
	}

	@Override
	public Session retrieveCurrentSession() {
		return defaultSessionFactory.getCurrentSession();
	}
}
