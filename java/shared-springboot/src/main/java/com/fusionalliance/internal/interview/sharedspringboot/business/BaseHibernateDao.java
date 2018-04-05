/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.interview.sharedspringboot.business;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * This abstract class provides base data access through Hibernate. Implementing classes may add to or modify these behaviors.
 * <p>
 * Sub-classes should typically do the following.
 * <ul>
 * <li>Specify @{@link Service} on the class</li>
 * <li>Specify an instance field of type SessionFactory modified by @{@link Autowired} and @{@link Qualifier}.</li>
 * </ul>
 */
public abstract class BaseHibernateDao {

	/**
	 * Add the entity to the persistence session.
	 * <p>
	 * This method delegates to {@link Session#persist(Object)}.
	 * 
	 * @param persistenceEntityParm
	 *            if null, does nothing
	 */
	public void persist(final PersistenceEntity persistenceEntityParm) {
		if (persistenceEntityParm == null) {
			return;
		}

		retrieveCurrentSession().persist(persistenceEntityParm);
	}

	/**
	 * Get an entity by id.
	 * <p>
	 * This method delegates to {@link Session#get(Class, java.io.Serializable)}.
	 * 
	 * @param entityClassParm
	 * @param idParm
	 * @return null if not found
	 */
	public <T extends PersistenceEntity> T get(final Class<T> entityClassParm, final long idParm) {
		final T entity = retrieveCurrentSession().get(entityClassParm, idParm);

		return entity;
	}

	/**
	 * Get a lazy proxy to an entity by id. This approach can save a database access if the non-id fields of the proxy are not used.
	 * <p>
	 * This method delegates to {@link Session#load(Class, java.io.Serializable)}.
	 * <p>
	 * <b>Warning:</b> Lazy proxies throw ObjectNotFoundException if the database row does not exist, but only when of the non-id fields is accessed.
	 * Use this method only after the existence of a row having the id has been established.
	 * 
	 * @param <T>
	 *            the entity type
	 * @param entityClassParm
	 *            required
	 * @param idParm
	 * @return null if not found
	 */
	public <T extends PersistenceEntity> T getLazyProxy(final Class<T> entityClassParm, final long idParm) {
		final T entity = retrieveCurrentSession().load(entityClassParm, idParm);

		return entity;
	}

	/**
	 * Delete an entity.
	 * <p>
	 * This method delegates to {@link Session#delete(Object)}.
	 * 
	 * @param entityParm
	 */
	public void delete(final PersistenceEntity entityParm) {
		retrieveCurrentSession().delete(entityParm);
	}

	/**
	 * Retrieve the current Session.
	 * <p>
	 * Implementing classes should return {@link SessionFactory#getCurrentSession()} using the injected SessionFactory, as described in
	 * {@link BaseHibernateDao}.
	 * 
	 * @return
	 */
	public abstract Session retrieveCurrentSession();
}
