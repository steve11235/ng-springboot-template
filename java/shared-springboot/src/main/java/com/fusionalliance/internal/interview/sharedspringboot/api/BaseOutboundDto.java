/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.interview.sharedspringboot.api;

import com.fusionalliance.internal.interview.sharedutility.core.TreatAsProtected;
import com.fusionalliance.internal.interview.sharedutility.messagemanager.Messages;

/**
 * This class is the base for all outbound DTO classes; these together define the outbound API exposed by the application.
 * <p>
 * BaseOutboundDto has a {@link Messages} field that is inherited by all subclasses. Typically, the service layer will supply it to the DTO after the
 * DTO has been built. Therefore, the putMessages(Messages) method is supplied to handle this special requirement. Messages are typically used only
 * for outgoing DTO; for incoming DTO, the messages field will typically be null.
 * 
 * @see BaseDto
 */
public abstract class BaseOutboundDto<T extends BaseOutboundDto<?>> extends BaseDto<T> {
	/** Messages that will be added to outgoing JSON */
	@SuppressWarnings("unused")
	private Messages messages;

	/**
	 * Put a Messages instance into the DTO. This method should only be used by service and transaction layer classes.
	 * 
	 * @param messagesParm
	 */
	@TreatAsProtected("Used by service, transaction layers only")
	public final void putMessages(final Messages messagesParm) {
		messages = messagesParm;
	}
}