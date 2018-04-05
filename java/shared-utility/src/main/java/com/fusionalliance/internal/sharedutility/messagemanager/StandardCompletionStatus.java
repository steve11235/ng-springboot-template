/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 19, 2017
 */
package com.fusionalliance.internal.sharedutility.messagemanager;

/**
 * This enum lists standard request completion statuses.
 */
public enum StandardCompletionStatus {
	OK,
	ERROR,
	INVALID_REQUEST,
	FAILURE;

	public static StandardCompletionStatus determineFromMessageManagerSeverity() {
		switch (MessageManager.getMaxSeverity()) {
		case INFO:
			return OK;
		case WARN:
			return OK;
		case ERROR:
			return ERROR;
		default:
			return FAILURE;
		}
	}
}
