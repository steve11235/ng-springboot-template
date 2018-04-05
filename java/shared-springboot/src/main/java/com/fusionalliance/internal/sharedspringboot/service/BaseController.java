/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 22, 2017
 */
package com.fusionalliance.internal.sharedspringboot.service;

import com.fusionalliance.internal.sharedspringboot.api.BaseDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;

/**
 * This abstract class is the base for all service-layer rest controllers. It provides some helper methods.
 */
public abstract class BaseController {

	/**
	 * Generate JSON using the MessagesOnlyOutboundDto. Callers must supply at least one message or a ValidationException is thrown.
	 * 
	 * @param standardCompletionStatusParm
	 *            required
	 * @return
	 */
	public String generateJsonFromMessageManager(final StandardCompletionStatus standardCompletionStatusParm) {
		ValidationUtility.checkObjectNotNull("No StandardCompletionStatus passed.", standardCompletionStatusParm);
		ValidationUtility.checkBadConditionNotMet("The MessageManager is empty.", MessageManager.getMessages().isEmpty());

		MessageManager.setCompletionText(standardCompletionStatusParm.name());

		final MessagesOnlyOutboundDto messagesOnlyDto = new MessagesOnlyOutboundDto().build();
		messagesOnlyDto.putMessages(MessageManager.makeFinal());

		final String messagesOnlyJson = GsonHelper.GSON.toJson(messagesOnlyDto);

		return messagesOnlyJson;
	}

	/**
	 * Add the validation errors from the DTO passed to the MessageManager, then generate JSON using the MessagesOnlyOutboundDto.
	 * 
	 * @param baseDtoParm
	 *            required
	 * @return
	 */
	public void addDtoValidationErrorsToMessageManager(final BaseDto<?> baseDtoParm) {
		ValidationUtility.checkObjectNotNull("The DTO is null.", baseDtoParm);

		final String serviceName = baseDtoParm.getClass().getSimpleName().replaceAll("(?i)OutboundDto", "");

		MessageManager.addError("Request information for %1$s was invalid.", serviceName);
		for (String validationError : baseDtoParm.getValidationErrors()) {
			MessageManager.addError(validationError);
		}
	}
}
