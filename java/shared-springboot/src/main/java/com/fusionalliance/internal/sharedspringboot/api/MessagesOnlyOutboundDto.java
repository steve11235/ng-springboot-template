/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 19, 2017
 */
package com.fusionalliance.internal.sharedspringboot.api;

/**
 * This class implements an outbound DTO that contains only a Messages instance inherited from BaseOutboundDto.
 * <p>
 * Typical usage is when an incoming request contains invalid information and cannot generate a full response.
 */
public final class MessagesOnlyOutboundDto extends BaseOutboundDto<MessagesOnlyOutboundDto> {

	@Override
	public void validate() {
		super.validate();

		// No further validations
	}
}
