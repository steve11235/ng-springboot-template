/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.sharedutility.application;

/**
 * This exception class implements a RuntimeException indicating that an error occurred during application processing that prevents the process from
 * providing a normal return. This may be used for errors caused by invalid data passed in the inbound request or by system errors.
 * <p>
 * Classes throwing this exception should update the MessageManager and log the error first. Thus, when this exception is caught, there should be no
 * need for further logging of the stack frames or updating of the MessageManager.
 * <p>
 * Typically, this exception is caught only in the transaction layer, after it has caused a transaction roll back.
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ApplicationException() {
		super("No message provided.");
	}

	/**
	 * Constructor
	 * 
	 * @param messageParm
	 */
	public ApplicationException(String messageParm) {
		super(messageParm);
	}

	/**
	 * Constructor
	 * 
	 * @param causeParm
	 */
	public ApplicationException(Throwable causeParm) {
		super(causeParm);
	}

	/**
	 * Constructor
	 * 
	 * @param messageParm
	 * @param causeParm
	 */
	public ApplicationException(String messageParm, Throwable causeParm) {
		super(messageParm, causeParm);
	}
}
