package com.fusionalliance.internal.sharedspringboot.api;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.google.common.collect.ImmutableSet;

/**
 * This class acts as a holder for a request type. The primary use of this class is {@link BaseInboundDto}, where it allows
 * {@link BaseInboundDto#validate()} to determine which fields are required. The request type is determined by the RESTful controller creating the
 * inbound DTO, so there is no reason for errors.
 * <p>
 * This approach was chosen over an <code>enum</code> because it is extensible, allowing additional request types to be defined for specific inbound
 * DTO's. Extending classes should override {@link #isKnownRequestType(String)}, calling super.isKnownRequestType(String) first and then performing
 * additional checks if the response is false.
 */
public class RequestTypeHolder {
	public static final String LIST = "LIST";
	public static final String GET = "GET";
	public static final String ADD = "ADD";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";

	private static final Set<String> REQUEST_TYPES = ImmutableSet.of(LIST, GET, ADD, UPDATE, DELETE);

	private final String requestType;

	/**
	 * Constructor
	 * 
	 * @param requestTypeParm
	 *            required, must be one the constant values defined by this class or classes extending it.
	 */
	public RequestTypeHolder(final String requestTypeParm) {
		ValidationUtility.checkGoodConditionMet("Unknown request type: " + requestTypeParm, isKnownRequestType(requestTypeParm));

		requestType = requestTypeParm;
	}

	/**
	 * Return true if the request type is known, false otherwise.
	 * <p>
	 * This method is called by the constructor and by overriding methods; it should not be called otherwise. Extending classes should override it so
	 * that their additional request types are accepted.
	 * 
	 * @param requestTypeParm
	 *            required
	 * @return
	 */
	protected boolean isKnownRequestType(String requestTypeParm) {
		ValidationUtility.checkBadConditionNotMet("Request type is null.", StringUtils.isBlank(requestTypeParm));

		return REQUEST_TYPES.contains(requestTypeParm);
	}

	public final String getRequestType() {
		return requestType;
	}
}
