/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 19, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a type or method should be treated as protected.
 * <p>
 * Typically, the type or method will have public scope but only to allow subclasses of a base class in different packages to access it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface TreatAsProtected {
	String value();
}
