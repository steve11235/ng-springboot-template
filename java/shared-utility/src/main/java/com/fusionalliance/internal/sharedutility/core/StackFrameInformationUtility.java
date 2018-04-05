/*
 * Copyright (C) 2017, Steve Page of Fusion Alliance
 *
 * Created on May 16, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

/**
 * This utility class returns information about the stack frames, identifying the caller and its callers. The return value starts with a newline.
 * <p>
 * The typical use case is an exception where a full stack trace is unnecessary but the log needs a few stack frames to identify the caller.
 */
public final class StackFrameInformationUtility {
	private static final int STACK_FRAMES = 5;

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
	 * @param exceptionParm
	 * @return
	 */
	public static String retrievePartialStackTrace(final Exception exceptionParm) {
		// Validate input
		if (exceptionParm == null) {
			return "";
		}

		return processStackFrames(exceptionParm, false);
	}

	/**
	 * Build a five frame stack trace.
	 * 
	 * @param exceptionParm
	 *            required, passing null will result in an NPE
	 * @param skipFirstFrameParm
	 *            typically false; true ignores the class instantiating the exception
	 * @return
	 */
	private static String processStackFrames(final Exception exceptionParm, final boolean skipFirstFrameParm) {
		final StringBuilder stringBuilder = new StringBuilder(500);
		final StackTraceElement[] stackTraceElements;
		final int startFrame, maxFrames;

		// Validate input
		if (exceptionParm == null) {
			return "";
		}

		startFrame = skipFirstFrameParm ? 1 : 0;
		stackTraceElements = exceptionParm.getStackTrace();

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
	private StackFrameInformationUtility() {
		// Do nothing
	}
}