/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created April 4, 2018
 */
package com.fusionalliance.internal.sharedutility.core;

import org.apache.commons.lang3.StringUtils;

/**
 * This utility class contains methods that check a condition and throw ValidationException if the condition is not met.
 * <p>
 * <b>Note:</b> The method names all have an implicit "validate" prefix, which is omitted for brevity.
 * <p>
 * The "backwards" parameter ordering enables the use of varargs.
 */
public final class ValidationUtility {

	/**
	 * Validate that a bad condition is not met; throw a ValidationException if it is.
	 * 
	 * @param exceptionTextParm
	 * @param badConditionParm
	 * @throws ValidationException
	 */
	public static void checkBadConditionNotMet(final String exceptionTextParm, final boolean badConditionParm) {
		if (badConditionParm) {
			throw new ValidationException(checkExceptionText(exceptionTextParm, "Bad condition was met."));
		}
	}

	/**
	 * Validate that a good condition is met; throw a ValidationException if it is not.
	 * 
	 * @param exceptionTextParm
	 * @param
	 * @throws ValidationException
	 */
	public static void checkGoodConditionMet(final String exceptionTextParm, final boolean goodConditionParm) {
		if (!goodConditionParm) {
			throw new ValidationException(checkExceptionText(exceptionTextParm, "Good condition was not met."));
		}
	}

	/**
	 * Validate that the object is not null; throw a ValidationException if it is.
	 * 
	 * @param exceptionTextParm
	 * @param objectParm
	 * @throws ValidationException
	 */
	public static void checkObjectNotNull(final String exceptionTextParm, final Object objectParm) {
		if (objectParm == null) {
			throw new ValidationException(checkExceptionText(exceptionTextParm, "The object is null."));
		}
	}

	/**
	 * Validate that the String is not blank (or null, empty;) throw a ValidationException if it is.
	 * 
	 * @param exceptionTextParm
	 * @param stringParm
	 * @throws ValidationException
	 */
	public static void checkStringNotBlank(final String exceptionTextParm, final String stringParm) {
		if (StringUtils.isBlank(stringParm)) {
			throw new ValidationException(checkExceptionText(exceptionTextParm, "The string is blank."));
		}
	}

	/**
	 * Return the exception text if it is not blank; otherwise, return the default message.
	 * 
	 * @param exceptionTextParm
	 * @param defaultTextParm
	 *            required, not blank
	 * @return
	 */
	private static String checkExceptionText(final String exceptionTextParm, final String defaultTextParm) {
		if (StringUtils.isBlank(defaultTextParm)) {
			throw new ValidationException("Internal error, default text is blank.");
		}

		if (StringUtils.isBlank(exceptionTextParm)) {
			return defaultTextParm;
		}

		return exceptionTextParm;
	}
}
