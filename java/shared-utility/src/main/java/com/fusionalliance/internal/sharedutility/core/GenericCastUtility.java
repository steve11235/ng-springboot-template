/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created Jun 6, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

/**
 * This utility class performs unchecked casts on generics. It is intended to minimize "unchecked" warnings.
 */
public class GenericCastUtility {

	/**
	 * Cast the from object to the specified type. Often, the JRE will infer the type T from the context.
	 * <p>
	 * Use this method if type of the object being cast is dependably known.
	 * <p>
	 * <b>Note:</b> This will throw class cast exceptions if the types don't match.
	 * 
	 * @param fromObjectParm
	 * @return
	 */
	public static <T extends Object> T cast(final Object fromObjectParm) {
		@SuppressWarnings("unchecked")
		final T type = (T) fromObjectParm;

		return type;
	}

	/**
	 * Cast the from object to the specified type, or return null if the cast cannot be performed. Often, the JRE will infer the type T from the
	 * context.
	 * <p>
	 * Use this method if the type of an object is expected but cannot be guaranteed, e.g., when parsing JSON.
	 * 
	 * @param fromObjectParm
	 * @return
	 */
	public static <T extends Object> T castOrNull(final Object fromObjectParm) {
		try {
			@SuppressWarnings("unchecked")
			final T type = (T) fromObjectParm;

			return type;
		}
		catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Hidden constructor
	 */
	private GenericCastUtility() {
		// Do nothing
	}
}
