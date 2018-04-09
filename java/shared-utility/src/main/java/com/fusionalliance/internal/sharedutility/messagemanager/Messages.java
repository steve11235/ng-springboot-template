/*
 * Copyright (C) 2017, Steve Page of Fusion Alliance
 *
 * Created on May 16, 2017
 */
package com.fusionalliance.internal.sharedutility.messagemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fusionalliance.internal.sharedutility.core.LoggerUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.Message.Severity;

/**
 * This class contains all the {@link Message} added during the current request.
 * <p>
 * Note that Messages must be added through the {@link MessageManager}.
 * <p>
 * This class is <i>not</i> thread-safe; it is meant to be managed by {@link MessageManager}, which handles thread safety.
 */
public final class Messages {
	private static final Logger LOG = LoggerFactory.getLogger(Messages.class);

	private final SortedSet<Message> messagesSorted = new TreeSet<Message>();
	private Severity maxSeverity = Severity.INFO;
	private String completionText = "";

	private transient int messageSequence;

	/**
	 * Add a message with the specified severity.
	 * <p>
	 * SYSTEM severity messages cause all non-SYSTEM messages to be removed. SYSTEM errors indicate that the process was interrupted, and other
	 * messages are not meaningful.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	String addMessage(final String messageTextParm, final Severity severityParm, final Object... replacementsParm) {
		final String text = doFormat(messageTextParm, replacementsParm);

		if (text.isEmpty()) {
			return "";
		}

		final Message message = new Message(text, severityParm, ++messageSequence);

		if (message.getSeverity() == Severity.SYSTEM && maxSeverity != Severity.SYSTEM) {
			messagesSorted.clear();
		}

		if (maxSeverity == Severity.SYSTEM && severityParm != Severity.SYSTEM) {
			// We still need to return the text, as it might be used for logging or an exception
			return text;
		}

		messagesSorted.add(message);

		if (severityParm.compareTo(maxSeverity) > 0) {
			maxSeverity = severityParm;
		}

		return text;
	}

	/**
	 * Do formatting with error checks. Any errors cause the raw message text to be returned. Null message text causes an empty String to be returned.
	 * 
	 * @param messageTextParm
	 * @param replacementsParm
	 * @return
	 */
	private static String doFormat(final String messageTextParm, Object... replacementsParm) {
		final String text;

		if (messageTextParm == null) {
			LOG.warn("Null message passed to MessageManager." + LoggerUtility.retrieveCallerInformation());

			return "";
		}

		if (replacementsParm == null) {
			return messageTextParm;
		}

		try {
			text = String.format(messageTextParm, replacementsParm);
		}
		catch (final Exception e) {
			LOG.warn("Unable to format message: " + messageTextParm + ". " + e.getMessage()
					+ LoggerUtility.retrievePartialStackTrace(e));

			return messageTextParm;
		}

		return text;
	}

	public Severity getMaxSeverity() {
		return maxSeverity;
	}

	public List<Message> getMessages() {
		return new ArrayList<Message>(messagesSorted);
	}

	public String getCompletionText() {
		return completionText;
	}

	void setCompletionText(final String completionTextParm) {
		if (completionTextParm == null) {
			completionText = "";
		}
		else {
			completionText = completionTextParm;
		}
	}

	public boolean isError() {
		return maxSeverity.compareTo(Severity.ERROR) >= 0;
	}
}