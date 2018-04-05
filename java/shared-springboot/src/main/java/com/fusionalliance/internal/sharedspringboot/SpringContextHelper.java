/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 26, 2017
 */
package com.fusionalliance.internal.sharedspringboot;

import org.springframework.context.ConfigurableApplicationContext;

import com.fusionalliance.internal.sharedutility.core.GenericCastUtility;
import com.fusionalliance.internal.sharedutility.core.TreatAsPrivate;

/**
 * This utility class provides static access methods to the Spring application context.
 */
public final class SpringContextHelper {

	private static ConfigurableApplicationContext applicationContext;

	/**
	 * DO NOT USE: Setter used by the Application class to provide the application context.
	 * 
	 * @param applicationContextParm
	 */
	@TreatAsPrivate
	public static void setApplicationContext(final ConfigurableApplicationContext applicationContextParm) {
		applicationContext = applicationContextParm;
	}

	/**
	 * Return a bean by class.
	 * 
	 * @param beanClassParm
	 *            required
	 * 
	 * @return
	 */
	public static <T extends Object> T getBeanByClass(final Class<T> beanClassParm) {
		final T bean = applicationContext.getBean(beanClassParm);

		return bean;
	}

	/**
	 * Return a bean by name.
	 * 
	 * @param beanNameParm
	 *            required
	 * @param expectedClassParm
	 *            required
	 * 
	 * @return
	 */
	public static <T extends Object> T getBeanByName(final String beanNameParm, final Class<T> expectedClassParm) {
		final Object beanObject = applicationContext.getBean(beanNameParm);

		if (beanObject == null) {
			return null;
		}

		final T bean = GenericCastUtility.castOrNull(beanObject);
		if (bean == null) {
			throw new IllegalArgumentException(
					"The bean is not an instance of the expected class: " + beanNameParm + ", " + expectedClassParm.getName());
		}

		return bean;
	}

	/**
	 * Return the application context.
	 * 
	 * @return
	 */
	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Hidden constructor
	 */
	public SpringContextHelper() {
		// Do nothing
	}
}
