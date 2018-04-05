package com.fusionalliance.internal.sharedspringboot.api;

/**
 * This exception class indicates that an attempt was made to use a fluent setter after the build() method was called.
 */
public class IllegalUpdateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IllegalUpdateException() {
		super("Attempt to set a field after the DTO instance was built.");
	}
}
