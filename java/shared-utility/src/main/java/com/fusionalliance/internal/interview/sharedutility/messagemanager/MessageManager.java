/*
 * Copyright (C) 2017, Steve Page of Fusion Alliance
 *
 * Created on May 16, 2017
 */
package com.fusionalliance.internal.interview.sharedutility.messagemanager;

import java.util.List;

import com.fusionalliance.internal.interview.sharedutility.messagemanager.Message.Severity;

/**
 * This class implements a means for multiple classes within a request to add messages that will be returned in the response. Each {@link Message} has
 * a {@link Message.Severity} that can be used to determine if the request has "failed". MessageManager can be used in conjunction with logging and
 * exceptions.
 * <p>
 * Access to MessageManager is through static methods for convenience. {@link Messages} instances are maintained <i>by thread</i> and the static
 * methods delegate to the current thread's instance.
 * <p>
 * {@link MessageManager#makeFinal()} returns the Messages instance after detaching it from the request. The Messages instance is effectively closed
 * for updates at this point. Typically, only classes in the service layer should invoke makeFinal().
 * <p>
 * Usage
 * <ul>
 * <li>initialize() - Initializes the MessageManager for the current request; this is typically performed in the service layer</li>
 * <li>addInfo(), addWarn(), addError(), addSystem() - Add a message of the specified severity
 * <ul>
 * <li>Uses String.format() to provide replacement capabilities</li>
 * <li>Returns the formatted text as a convenience</li>
 * </ul>
 * </li>
 * <li>makeFinal() - Returns the Messages instance for the current request after detaching it from the request; this is typically performed in the
 * service layer</li>
 * <li>destroy() - Releases all resources of the MessageManager: Use only from a ServletContextListener.contextDestroyed()
 * </ul>
 * Notes
 * <ul>
 * <li>initialize() is mandatory; failure to invoke it may lead to messages accumulating between requests</li>
 * <li>Multiple calls to setCurrentCompletionText() simply updates the completion text</li>
 * <li>Failure to invoke destroy() can lead to memory leaks in development and test environments, multiple invocations have no effect</li>
 * <li>System severity messages causes the clearing of all non-System severity messages, as this severity indicates that the request failed and the
 * other messages are partial and therefore not reliable</li>
 * <li>After makeFinal(), a subsequent add*() in the same request creates a new Messages instance that is empty</li>
 * </ul>
 */
public final class MessageManager {
	private static final Object syncObject = new Object();

	private static ThreadLocal<Messages> threadLocal;

	static {
		/*
		 * Ensure the ThreadLocal exists when the class is loaded.
		 * 
		 * Certain legacy application servers that don't properly handle class loading after context destroy, which can lead to this class not 
		 * reloading when a new context is initialized. This leads to threadLocal being null.
		 * 
		 * Instantiating the ThreadLocal in initialize() mitigates this problem.
		 */
		initialize();
	}

	/**
	 * Initialize the MessageManager at the beginning of a request.
	 * <p>
	 * <b>Warning:</b> This method must be called at the beginning of each request
	 */
	public static void initialize() {

		if (threadLocal == null) {
			synchronized (syncObject) {
				if (threadLocal == null) {
					threadLocal = new ThreadLocal<Messages>() {

						@Override
						protected Messages initialValue() {
							return new Messages();
						}

					};
				}
			}
		}

		threadLocal.remove();
	}

	/**
	 * Add an Info severity message.
	 * <p>
	 * Info severity indicates that a normal condition occurred.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	public static String addInfo(final String messageTextParm, final Object... replacementsParm) {
		return threadLocal.get().addMessage(messageTextParm, Severity.INFO, replacementsParm);
	}

	/**
	 * Add a Warn severity message.
	 * <p>
	 * Warn severity indicates that an abnormal condition occurred that did not prevent the request from completing normally.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	public static String addWarn(final String messageTextParm, final Object... replacementsParm) {
		return threadLocal.get().addMessage(messageTextParm, Severity.WARN, replacementsParm);
	}

	/**
	 * Add an Error severity message.
	 * <p>
	 * Error severity indicates that some condition, typically invalid input data, prevented the request from completing normally.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	public static String addError(final String messageTextParm, final Object... replacementsParm) {
		return threadLocal.get().addMessage(messageTextParm, Severity.ERROR, replacementsParm);
	}

	/**
	 * Add an System severity message using the standard application failure message.
	 * <p>
	 * <b>Warning:</b> System severity indicates that the request was interrupted by an internal error. All messages that have severity less than
	 * System will be discarded, as the state of the request is now invalid.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	public static String addSystem() {
		return addSystem(StandardMessages.APPLICATION_FAILURE);
	}

	/**
	 * Add an System severity message.
	 * <p>
	 * <b>Warning:</b> System severity indicates that the request was interrupted by an internal error. All messages that have severity less than
	 * System will be discarded, as the state of the request is now invalid.
	 * 
	 * @param messageTextParm
	 *            message text with optional replacement markers (typically %1$s)
	 * @param replacementsParm
	 *            zero to many replacements (do not pass null)
	 * @return the formatted text
	 * @see {@link java.util.Formatter}, {@link String#format(String, Object...)}
	 */
	public static String addSystem(final String messageTextParm, final Object... replacementsParm) {
		return threadLocal.get().addMessage(messageTextParm, Severity.SYSTEM, replacementsParm);
	}

	/**
	 * Return true if one of the current messages has severity Error or higher; otherwise, false;
	 * <p>
	 * Note that the request is still active and that subsequent activity could cause the returned value to change.
	 * 
	 * @return
	 */
	public static boolean isError() {
		return threadLocal.get().isError();
	}

	/**
	 * Return the current maximum severity.
	 * <p>
	 * Note that the request is still active and that subsequent activity could cause the returned value to change.
	 * 
	 * @return
	 */
	public static Severity getMaxSeverity() {
		return threadLocal.get().getMaxSeverity();
	}

	/**
	 * Return a list of the current messages, sorted by severity descending and then the order they were created.
	 * <p>
	 * Note that the request is still active and that subsequent activity could cause the returned value to change.
	 * 
	 * @return
	 */
	public static List<Message> getMessages() {
		return threadLocal.get().getMessages();
	}

	/**
	 * Return the current completion text.
	 * <p>
	 * Note that the request is still active and that subsequent activity could cause the returned value to change.
	 * 
	 * @return
	 */
	public static String getCompletionText() {
		return threadLocal.get().getCompletionText();
	}

	/**
	 * Set the current completion text.
	 * <p>
	 * Note that the request is still active and that subsequent calls to this method will change the value.
	 * 
	 * @param completionTextParm
	 */
	public static void setCompletionText(final String completionTextParm) {
		threadLocal.get().setCompletionText(completionTextParm);
	}

	/**
	 * Detach the current Messages instance and return it. The instance returned is effectively immutable.
	 * <p>
	 * <b>Warning:</b> Do not call this method until <i>all</i> request processing is complete.
	 * 
	 * @return
	 */
	public static Messages makeFinal() {
		final Messages messages = threadLocal.get();

		threadLocal.remove();

		return messages;
	}

	/**
	 * Destroy the MessageManager. Effectively, this removes the ThreadLocal instance, allowing it to be GC'ed, preventing memory leaks in application
	 * servers.
	 */
	public static void destroy() {
		threadLocal = null;
	}
}