/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.sharedspringboot.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.LoggerUtility;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.Messages;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;

/**
 * This class implements a facade in front of {@link TransactionLayer}. This necessary because Spring AOP will not work correctly if a method in a
 * class calls an @Transactional method in the same class. The facade methods traps ApplicationException, ensuring that an outbound DTO is always
 * returned.
 */
@Component
public class TransactionLayerFacade {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionLayerFacade.class);

	private final TransactionLayer transactionLayer;

	/**
	 * Constructor
	 * 
	 * @param transactionLayerParm
	 *            required, determines which transaction manager will be used
	 */
	public TransactionLayerFacade(final TransactionLayer transactionLayerParm) {
		ValidationUtility.checkObjectNotNull("No TransactionLayer passed.", transactionLayerParm);

		transactionLayer = transactionLayerParm;
	}

	/**
	 * Execute the BusinessProcessor in a updatable transaction.
	 * 
	 * @param processorParm
	 *            required
	 * @param updatableParm
	 *            run in updatable transaction?
	 * @return never null
	 */
	public BaseOutboundDto<?> process(final BusinessProcessor<?> processorParm, final boolean updatableParm) {
		ValidationUtility.checkObjectNotNull("The processor is null", processorParm);

		BaseOutboundDto<?> outboundDto = null;

		try {
			if (updatableParm) {
				outboundDto = transactionLayer.processUpdateable(processorParm);
			}
			else {
				outboundDto = transactionLayer.processReadOnly(processorParm);
			}
		}
		catch (final ApplicationException ae) {
			outboundDto = new MessagesOnlyOutboundDto().build();

			if (!MessageManager.isError()) {
				MessageManager.addSystem();

				LoggerUtility.logException(LOG, "Caught ApplicationException, no MessageManager error added.", ae);
			}
		}

		MessageManager.setCompletionText(StandardCompletionStatus.determineFromMessageManagerSeverity().name());
		final Messages messages = MessageManager.makeFinal();
		outboundDto.putMessages(messages);

		return outboundDto;
	}
}
