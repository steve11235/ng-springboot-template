/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.sharedspringboot.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.StackFrameInformationUtility;
import com.fusionalliance.internal.sharedutility.core.TreatAsProtected;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;

/**
 * This class implements a service supporting transactions. Note that it uses the DefaultTransactionManager. Classes that want to use a different
 * transaction manager can override the {@link #processReadOnly(BusinessProcessor)} and {@link #processUpdateable(BusinessProcessor)} methods,
 * supplying the name of a different transaction manager in the [at}Transactional annotations.
 * <p>
 * This class should not be called directly; use {@link TransactionLayerFacade} instead.
 */
@TreatAsProtected("Use TransactionLayerFacade instead")
public class TransactionLayer {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionLayer.class);

	/**
	 * Execute the BusinessProcessor in a updatable transaction.
	 * 
	 * @param processorParm
	 * @return never null
	 */
	@Transactional(transactionManager = "DefaultTransactionManager")
	public BaseOutboundDto<?> processUpdateable(final BusinessProcessor<?> processorParm) {
		return process(processorParm);
	}

	/**
	 * Execute the BusinessProcessor in a read-only transaction.
	 * 
	 * @param processorParm
	 * @return never null
	 */
	@Transactional(transactionManager = "DefaultTransactionManager", readOnly = true)
	public BaseOutboundDto<?> processReadOnly(final BusinessProcessor<?> processorParm) {
		return process(processorParm);
	}

	/**
	 * Execute the BusinessProcessor in the transaction context, validate the outbound DTO returned.
	 * <p>
	 * This method traps unexpected exceptions (not ApplicationException,) logs them, adds a System message to the MessageManager, and throws
	 * ApplicationException. This allows roll back of transactions. ApplicationException are simply re-thrown.
	 * 
	 * @param processorParm
	 * @return
	 * @throws ApplicationException
	 */
	protected final BaseOutboundDto<?> process(final BusinessProcessor<?> processorParm) {

		try {
			final BaseOutboundDto<?> outboundDto = processorParm.processInternal();

			if (outboundDto == null) {
				LOG.error("Null outbound DTO returned from: " + processorParm.getClass().getName());
				MessageManager.addSystem();

				throw new ApplicationException("Outbound DTO null.");
			}

			// Outbound DTO should always be valid
			// Treat invalid DTO as system failures
			outboundDto.validate();
			if (outboundDto.isValidationErrors()) {
				LOG.error("Validation errors in DTO: " + outboundDto.getClass().getName());
				for (String validationError : outboundDto.getValidationErrors()) {
					LOG.warn(validationError);
				}

				MessageManager.addSystem();

				throw new ApplicationException("Validation errors found.");
			}

			return outboundDto;
		}
		catch (final ApplicationException ae) {
			throw ae;
		}
		catch (final Exception e) {
			LOG.error("An unexpected exception occurred: " + e.getMessage() + StackFrameInformationUtility.retrievePartialStackTrace(e));
			MessageManager.addSystem();

			throw new ApplicationException("Processing failed.");
		}
	}
}
