/*
 * Copyright (C) 2017, Steve Page of Fusion Alliance
 *
 * Created on May 16, 2017
 */
package com.fusionalliance.internal.sharedutility.messagemanager;

import com.fusionalliance.internal.sharedutility.core.ValidationUtility;

/**
 * This class implements a message.
 */
public final class Message implements Comparable<Message> {
	private final String text;
	private final Severity severity;
	private final int sequence;
	private final String entity;
	private final long entityKey;
	private final String entityField;

	/**
	 * Restricted constructor
	 * 
	 * @param textParm
	 * @param severityParm
	 */
	Message(final String textParm, final Severity severityParm, final int sequenceParm) {
		ValidationUtility.checkStringNotBlank("Message text is blank", textParm);
		ValidationUtility.checkObjectNotNull("Message severity is null.", severityParm);

		text = textParm;
		severity = severityParm;
		sequence = sequenceParm;

		entity = null;
		entityKey = 0;
		entityField = null;
	}

	/**
	 * Restricted constructor, with entity fields
	 * 
	 * @param textParm
	 * @param severityParm
	 */
	Message(final String textParm, final Severity severityParm, final int sequenceParm, final String entityParm, final long entityKeyParm,
			final String entityFieldParm) {
		ValidationUtility.checkStringNotBlank("Message text is blank", textParm);
		ValidationUtility.checkObjectNotNull("Message severity is null.", severityParm);
		ValidationUtility.checkStringNotBlank("Message entity is blank", textParm);
		ValidationUtility.checkStringNotBlank("Message entity field is blank", textParm);

		text = textParm;
		severity = severityParm;
		sequence = sequenceParm;

		entity = entityParm;
		entityKey = entityKeyParm;
		entityField = entityFieldParm;
	}

	public String getText() {
		return text;
	}

	public Severity getSeverity() {
		return severity;
	}

	int getSequence() {
		return sequence;
	}

	public String getEntity() {
		return entity;
	}

	public long getEntityKey() {
		return entityKey;
	}

	public String getEntityField() {
		return entityField;
	}

	@Override
	public boolean equals(Object objectParm) {
		final Message otherMessage;

		if (this == objectParm) {
			return true;
		}

		if (!(objectParm instanceof Message)) {
			return false;
		}

		otherMessage = (Message) objectParm;

		if (severity != otherMessage.getSeverity()) {
			return false;
		}

		if (sequence != otherMessage.getSequence()) {
			return false;
		}

		// We should never get here; this is a formality
		return text.equals(otherMessage.getText());
	}

	@Override
	public int hashCode() {
		// 7FFFFFFF is 2^32 - 1 and is prime
		final long hash = severity.hashCode() + text.hashCode() + ((sequence + 0x40000000L) * 0x7FFFFFFFL);

		return (int) hash;
	}

	/**
	 * Comparison is based on severity (descending), created.
	 */
	@Override
	public int compareTo(final Message otherParm) {
		if (this == otherParm) {
			return 0;
		}

		if (severity.compareTo(otherParm.getSeverity()) != 0) {
			// Severity descending
			return -severity.compareTo(otherParm.getSeverity());
		}

		if (sequence < otherParm.getSequence()) {
			return -1;
		}
		if (sequence > otherParm.getSequence()) {
			return 1;
		}

		return 0;
	}

	/**
	 * This enum lists Message severities.
	 */
	public enum Severity {
		/**
		 * A positive outcome occurred that should be reported.
		 */
		INFO,
		/**
		 * A negative outcome occurred that should be reported but that did not prevent request processing from completing.
		 */
		WARN,
		/**
		 * An expected negative outcome occurred that prevented request processing from completing, typically involving bad data in the request.
		 */
		ERROR,
		/**
		 * An unexpected negative outcome occurred that interrupted request processing, typically involving a bad system configuration or a
		 * programming error.
		 */
		SYSTEM, //
		;
	}
}