/*
 * Copyright (C) 2017, Steve Page of Fusion Alliance
 *
 * Created on May 16, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

import org.slf4j.Logger;

/**
 * This utility class provides support for logging exceptions with limited stack traces. This ensures that needed information is available to
 * understand the exception and the context in which it occurred without excessive stack frame logging.
 */
public final class LoggerUtility {
	private static final int STACK_FRAMES = 5;

	/**
	 * Log an Error entry to the Logger containing the message, up to five stack frames of the caller, and, optionally, the throwable message and up
	 * to five of its stack frames.
	 * 
	 * @param loggerParm
	 *            required
	 * @param messageParm
	 *            required, not blank
	 * @param throwableParm
	 *            optional, no "Caused by:" if null
	 */
	public static void logException(final Logger loggerParm, final String messageParm, final Throwable throwableParm) {
		ValidationUtility.checkObjectNotNull("Logger is null", loggerParm);
		ValidationUtility.checkStringNotBlank("Message is blank", messageParm);

		final StringBuilder logMessageBuilder = new StringBuilder(1000);

		logMessageBuilder.append(messageParm);
		logMessageBuilder.append(retrieveCallerInformation());

		processThrowable(throwableParm, logMessageBuilder);
	}

	/**
	 * Add the the throwable and its stack frames to the log message builder. Recursively process its cause, if any.
	 * 
	 * @param throwableParm
	 * @param logMessageBuilder
	 */
	private static void processThrowable(final Throwable throwableParm, final StringBuilder logMessageBuilder) {
		if (throwableParm == null) {
			return;
		}

		logMessageBuilder.append("\nCaused by ");
		logMessageBuilder.append(throwableParm.getClass().getSimpleName());
		logMessageBuilder.append(": ");
		logMessageBuilder.append(throwableParm.getMessage());
		logMessageBuilder.append(retrievePartialStackTrace(throwableParm));

		final Throwable cause = throwableParm.getCause();
		if (cause == null || cause == throwableParm) {
			return;
		}

		processThrowable(cause, logMessageBuilder);
	}

	/**
	 * Retrieve the caller's stack frame information and up to four of it's callers.
	 * 
	 * @return
	 */
	public static String retrieveCallerInformation() {
		// Pass a dummy exception, ignore this call level
		return processStackFrames(new Exception(), true);
	}

	/**
	 * Retrieve up to five frames from the exception.
	 * 
	 * @param throwableParm
	 * @return
	 */
	public static String retrievePartialStackTrace(final Throwable throwableParm) {
		// Validate input
		if (throwableParm == null) {
			return "";
		}

		return processStackFrames(throwableParm, false);
	}

	/**
	 * Build a five frame stack trace.
	 * 
	 * @param throwableParm
	 *            required, passing null will result in an NPE
	 * @param skipFirstFrameParm
	 *            typically false; true ignores the class instantiating the exception
	 * @return
	 */
	private static String processStackFrames(final Throwable throwableParm, final boolean skipFirstFrameParm) {
		final StringBuilder stringBuilder = new StringBuilder(500);
		final StackTraceElement[] stackTraceElements;
		final int startFrame, maxFrames;

		// Validate input
		if (throwableParm == null) {
			return "";
		}

		startFrame = skipFirstFrameParm ? 1 : 0;
		stackTraceElements = throwableParm.getStackTrace();

		// Output up to the specified frames
		maxFrames = stackTraceElements.length < STACK_FRAMES + startFrame ? stackTraceElements.length + startFrame : STACK_FRAMES + startFrame;
		for (int i = startFrame; i < maxFrames; i++) {
			if (i == startFrame) {
				stringBuilder.append("\nAt\t");
			}
			else {
				stringBuilder.append("\nCaller\t");
			}
			stringBuilder.append(stackTraceElements[i].getClassName());
			stringBuilder.append(", ");
			stringBuilder.append(stackTraceElements[i].getMethodName());
			stringBuilder.append("(), ");
			stringBuilder.append(stackTraceElements[i].getLineNumber());
		}

		return stringBuilder.toString();
	}

	/**
	 * Hidden constructor
	 */
	private LoggerUtility() {
		// Do nothing
	}
}