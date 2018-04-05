/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.sharedspringboot.api;

import org.apache.commons.lang3.StringUtils;

/**
 * This class is the base for all inbound DTO classes; these together define the inbound API exposed by the application.
 * 
 * @see BaseDto
 */
public abstract class BaseInboundDto<T extends BaseInboundDto<?, ?>, R extends RequestTypeHolder> extends BaseDto<T> {

	public static final String LIST = "LIST";
	public static final String GET = "GET";
	public static final String ADD = "add";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";

	/** Required, must be one the constant values, used for validation */
	private transient R requestTypeHolder;

	/**
	 * Get the request type. This is a convenience getter that safely retrieves the request type from the holder.
	 * <p>
	 * It returns EMPTY if the holder was not set to avoid null exceptions during validation. Note that {@link #validate()} will add an error message
	 * in this case.
	 * 
	 * @return never null
	 */
	public String getRequestType() {
		if (requestTypeHolder == null) {
			return StringUtils.EMPTY;
		}

		return requestTypeHolder.getRequestType();
	}

	public final R getRequestTypeHolder() {
		return requestTypeHolder;
	}

	public final T requestTypeHolder(final R requestTypeHolderParm) {
		checkNotBuilt();

		requestTypeHolder = requestTypeHolderParm;

		return fetchThisAsT();
	}

	@Override
	public void validate() {
		super.validate();

		if (requestTypeHolder == null) {
			addValidationError("The request type holder is missing.");
		}
	}
}
