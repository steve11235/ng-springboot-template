/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 17, 2017
 */
package com.fusionalliance.internal.sharedspringboot.api;

import com.fusionalliance.internal.sharedutility.core.TreatAsRestricted;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.Messages;

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
	 * Put a Messages instance into the DTO. This method should only be used by framework classes.
	 * 
	 * @param messagesParm
	 *            required
	 */
	@TreatAsRestricted("Used by framework only")
	public void putMessages(final Messages messagesParm) {
		ValidationUtility.checkObjectNotNull("Messages is null", messagesParm);

		messages = messagesParm;
	}
}
