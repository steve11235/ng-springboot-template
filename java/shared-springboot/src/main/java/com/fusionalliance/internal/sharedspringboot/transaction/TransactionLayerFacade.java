/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.sharedspringboot.transaction;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;

/**
 * This class implements a facade in front of {@link TransactionLayer}. This necessary because Spring AOP will not work correctly if a method in a
 * class calls an @Transactional method in the same class. The facade methods traps ApplicationException, ensuring that an outbound DTO is always
 * returned.
 */
public final class TransactionLayerFacade {

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
	 * @return never null
	 */
	public BaseOutboundDto<?> processUpdateable(final BusinessProcessor<?> processorParm) {
		try {
			return transactionLayer.processUpdateable(processorParm);
		}
		catch (final ApplicationException ae) {
			return new MessagesOnlyOutboundDto();
		}
		finally {
			updateCompletionStatus();
		}
	}

	/**
	 * Execute the BusinessProcessor in a read-only transaction.
	 * 
	 * @param processorParm
	 * @return never null
	 */
	public BaseOutboundDto<?> processReadOnly(final BusinessProcessor<?> processorParm) {
		try {
			return transactionLayer.processReadOnly(processorParm);
		}
		catch (final ApplicationException ae) {
			return new MessagesOnlyOutboundDto();
		}
		finally {
			updateCompletionStatus();
		}
	}

	private void updateCompletionStatus() {
		MessageManager.setCompletionText(StandardCompletionStatus.determineFromMessageManagerSeverity().name());
	}
}
