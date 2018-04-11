/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 11, 2018
 */
package com.fusionalliance.internal.sharedutility.core;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test class contains tests for {@link LoggerUtility}.
 */
public class LoggerUtilityTest {
	private static final Logger LOG = LoggerFactory.getLogger(LoggerUtilityTest.class);

	@Test
	public void logExceptionTest() {
		final Exception exception3 = new Exception("Level 3");
		final Exception exception2 = new Exception("Level 2", exception3);
		final Exception exception1 = new Exception("Level 1", exception2);

		LoggerUtility.logException(LOG, "Test multiple cause levels.", exception1);
	}

	@Test
	public void retrieveCallerInformationTest() {
		final String callerInformation = LoggerUtility.retrieveCallerInformation();

		Assert.assertTrue(callerInformation.contains(getClass().getName()));
	}

	@Test
	public void retrievePartialStackTraceTest() {
		final String stackTrace = LoggerUtility.retrievePartialStackTrace(new Exception("Boom!"));

		Assert.assertTrue(stackTrace.contains(getClass().getName()));
	}
}
