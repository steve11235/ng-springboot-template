/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 6, 2018
 */
package com.fusionalliance.internal.springboottemplate.api.user;

import java.util.Set;

import com.fusionalliance.internal.sharedspringboot.api.RequestTypeHolder;
import com.google.common.collect.ImmutableSet;

/**
 * This class extends {@link RequestTypeHolder} to provide a UPDATE_CREDS request type.
 */
public class UserRequestTypeHolder extends RequestTypeHolder {
	public static final String UPDATE_CREDS = "UPDATE_CREDS";

	private static final Set<String> USER_REQUEST_TYPES = ImmutableSet.of(UPDATE_CREDS);

	/**
	 * Constructor
	 * 
	 * @param requestTypeParm
	 *            required, must be one of the defined request types
	 */
	public UserRequestTypeHolder(final String requestTypeParm) {
		super(requestTypeParm);
	}

	@Override
	protected boolean isKnownRequestType(String requestTypeParm) {
		if (super.isKnownRequestType(requestTypeParm)) {
			return true;
		}

		return USER_REQUEST_TYPES.contains(requestTypeParm);
	}
}
