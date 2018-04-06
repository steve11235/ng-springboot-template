/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.sharedspringboot.api;

import org.apache.commons.lang3.StringUtils;

import com.fusionalliance.internal.sharedutility.core.GenericCastUtility;

/**
 * This class is the base for all inbound DTO classes; these together define the inbound API exposed by the application.
 * 
 * @see BaseDto
 */
public abstract class BaseInboundDto<T extends BaseInboundDto<?>> extends BaseDto<T> {

	/** Required, must be one the constant values, used for validation */
	private transient RequestTypeHolder requestTypeHolder;

	/**
	 * Get the request type. This is a convenience getter that safely retrieves the request type from the holder.
	 * <p>
	 * It returns EMPTY if the holder was not set to avoid null exceptions during validation. Note that {@link #validate()} will add an error message
	 * in this case.
	 * 
	 * @return never null
	 */
	public final String getRequestType() {
		if (requestTypeHolder == null) {
			return StringUtils.EMPTY;
		}

		return requestTypeHolder.getRequestType();
	}

	public final T requestTypeHolder(final RequestTypeHolder requestTypeHolderParm) {
		checkNotBuilt();

		requestTypeHolder = requestTypeHolderParm;

		return GenericCastUtility.cast(this);
	}

	@Override
	public void validate() {
		super.validate();

		if (requestTypeHolder == null) {
			addValidationError("The request type holder is missing.");
		}
	}
}
