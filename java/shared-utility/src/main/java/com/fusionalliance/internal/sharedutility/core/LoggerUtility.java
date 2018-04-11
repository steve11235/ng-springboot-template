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

		final StringBuilder messageBuilder = new StringBuilder(1000);

		messageBuilder.append(messageParm);

		processStackFrames(new Exception(), messageBuilder, true);
		processCause(throwableParm, messageBuilder);

		loggerParm.error(messageBuilder.toString());
	}

	public static String retrieveCallerInformation() {
		final StringBuilder messageBuilder = new StringBuilder(1000);

		processStackFrames(new Exception(), messageBuilder, true);

		return messageBuilder.toString();
	}

	public static String retrievePartialStackTrace(final Throwable throwableParm) {
		final StringBuilder messageBuilder = new StringBuilder(1000);

		processCause(throwableParm, messageBuilder);

		return messageBuilder.toString();
	}

	/**
	 * Add the the throwable and its stack frames to the log message builder. Recursively processes the Throwable's cause, if any.
	 * 
	 * @param throwableParm
	 *            optional, no action if null
	 * @param messageBuilderParm
	 *            required, <b>mutated by method</b>
	 */
	private static void processCause(final Throwable throwableParm, final StringBuilder messageBuilderParm) {
		if (throwableParm == null) {
			return;
		}

		messageBuilderParm.append("\nCaused by ");
		messageBuilderParm.append(throwableParm.getClass().getSimpleName());
		messageBuilderParm.append(": ");
		messageBuilderParm.append(throwableParm.getMessage());

		processStackFrames(throwableParm, messageBuilderParm, false);

		final Throwable cause = throwableParm.getCause();
		if (cause == null || cause == throwableParm) {
			return;
		}

		processCause(cause, messageBuilderParm);
	}

	/**
	 * Build a five frame stack trace.
	 * 
	 * @param throwableParm
	 *            optional, no action if null
	 * @param messageBuilderParm
	 *            required, <b>mutated by method</b>
	 * @param skipFirstFrameParm
	 *            typically false; true ignores the class instantiating the exception
	 * @return
	 */
	private static void processStackFrames(final Throwable throwableParm, final StringBuilder messageBuilderParm, final boolean skipFirstFrameParm) {
		if (throwableParm == null) {
			return;
		}

		final int startFrame = skipFirstFrameParm ? 1 : 0;
		final StackTraceElement[] stackTraceElements = throwableParm.getStackTrace();

		// Output up to the specified frames
		final int maxFrames = stackTraceElements.length < STACK_FRAMES + startFrame ? stackTraceElements.length + startFrame
				: STACK_FRAMES + startFrame;
		for (int i = startFrame; i < maxFrames; i++) {
			if (i == startFrame) {
				messageBuilderParm.append("\nAt\t");
			}
			else {
				messageBuilderParm.append("\nCaller\t");
			}
			messageBuilderParm.append(stackTraceElements[i].getClassName());
			messageBuilderParm.append(", ");
			messageBuilderParm.append(stackTraceElements[i].getMethodName());
			messageBuilderParm.append("(), ");
			messageBuilderParm.append(stackTraceElements[i].getLineNumber());
		}

		return;
	}

	/**
	 * Hidden constructor
	 */
	private LoggerUtility() {
		// Do nothing
	}
}