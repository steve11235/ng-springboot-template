/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.interview.sharedspringboot.business;

import javax.persistence.Entity;

/**
 * This interface defines the API contract for classes that specify @{@link Entity}.
 * <p>
 * The design goal is that entity classes encapsulate the logic to validate their state. Every entity instance modified by a {@link BusinessProcessor}
 * must be validated to avoid persisting bad state.
 */
public interface PersistenceEntity {
	/**
	 * Return true if the internal state of the entity is valid. Add error messages as needed to MessageManager.
	 * <p>
	 * Validate as much of the state as practicable; do not stop validating on the first problem. This provides more information to the client,
	 * helping to avoid multiple error responses.
	 * <p>
	 * Ensure the entity name appears prominently in the messages; the client should be able to clearly identify which entity is invalid.
	 * <p>
	 * Typically, no logging is necessary, as invalid state is caused by invalid information passed by a client request.
	 */
	public boolean validate();
}
