package com.fusionalliance.internal.interview.sharedutility.jwt;

/**
 * This exception class indicates that an error occurred during JWT processing.
 */
public class JwtException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param messageParm
	 */
	public JwtException(String messageParm) {
		super(messageParm);
	}
}
