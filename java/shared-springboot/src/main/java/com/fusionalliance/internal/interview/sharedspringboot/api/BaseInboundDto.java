/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.interview.sharedspringboot.api;

/**
 * This class is the base for all inbound DTO classes; these together define the inbound API exposed by the application.
 * 
 * @see BaseDto
 */
public abstract class BaseInboundDto<T extends BaseInboundDto<?>> extends BaseDto<T> {

	public static final String LIST = "LIST";
	public static final String GET = "GET";
	public static final String ADD = "add";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";

	/** Required, must be one the constant values, used for validation */
	private transient String requestType;

	protected final String getRequestType() {
		return requestType;
	}

	public final T requestType(String requestTypeParm) {
		checkNotBuilt();

		requestType = requestTypeParm;

		return fetchThisAsT();
	}
}
