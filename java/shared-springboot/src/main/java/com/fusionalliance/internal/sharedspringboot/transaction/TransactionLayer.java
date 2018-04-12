/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 25, 2017
 */
package com.fusionalliance.internal.sharedspringboot.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.TreatAsRestricted;

/**
 * This class implements a service supporting transactions. Note that it uses the DefaultTransactionManager. Classes that want to use a different
 * transaction manager can override the {@link #processReadOnly(BusinessProcessor)} and {@link #processUpdateable(BusinessProcessor)} methods,
 * supplying the name of a different transaction manager in the [at}Transactional annotations.
 * <p>
 * This class should not be called directly; use {@link TransactionLayerFacade} instead.
 */
@Component
@TreatAsRestricted("Use TransactionLayerFacade instead")
public class TransactionLayer {
	private static final String TRANSACTION_MANAGER_NAME = "defaultTransactionManager";

	/**
	 * Execute the BusinessProcessor in a updatable transaction.
	 * 
	 * @param processorParm
	 * @return never null
	 */
	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME)
	public BaseOutboundDto<?> processUpdateable(final BusinessProcessor<?> processorParm) {
		return process(processorParm);
	}

	/**
	 * Execute the BusinessProcessor in a read-only transaction.
	 * 
	 * @param processorParm
	 * @return never null
	 */
	@Transactional(transactionManager = TRANSACTION_MANAGER_NAME, readOnly = true)
	public BaseOutboundDto<?> processReadOnly(final BusinessProcessor<?> processorParm) {
		return process(processorParm);
	}

	/**
	 * Execute the BusinessProcessor and return the outbound DTO or an ApplicationException.
	 * <p>
	 * This trivial method avoids redundant logic in its callers, which also may be overridden.
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	protected final BaseOutboundDto<?> process(final BusinessProcessor<?> processorParm) {
		final BaseOutboundDto<?> outboundDto = processorParm.processInternal();

		return outboundDto;
	}
}
