/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.interview.sharedutility.core;

/**
 * This class implements a RuntimeException indicating that a validation failed.
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String messageParm, Throwable causeParm) {
		super(messageParm, causeParm);
	}

	public ValidationException(String messageParm) {
		super(messageParm);
	}

	public ValidationException(Throwable causeParm) {
		super(causeParm);
	}
}