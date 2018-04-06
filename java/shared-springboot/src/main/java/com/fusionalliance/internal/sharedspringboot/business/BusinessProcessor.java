/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 22, 2017
 */
package com.fusionalliance.internal.sharedspringboot.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fusionalliance.internal.sharedspringboot.api.BaseInboundDto;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.core.TreatAsRestricted;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;

/**
 * This abstract class provides the base for business-layer processor classes.
 * <p>
 * Instances of implementing classes are stateful and not thread safe.
 * 
 * @param <T>
 *            the type of the inbound DTO
 */
public abstract class BusinessProcessor<T extends BaseInboundDto<?>> {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessProcessor.class);

	private final T inboundDto;

	/**
	 * Constructor
	 * 
	 * @param inboundDtoParm
	 *            required
	 */
	public BusinessProcessor(final T inboundDtoParm) {
		ValidationUtility.checkObjectNotNull("Inbound DTO is required.", inboundDtoParm);

		inboundDto = inboundDtoParm;
	}

	/**
	 * Perform business processing on the request information in the inbound DTO. If processing is successful, return a outbound DTO containing the
	 * response. Validation of the outbound DTO is handled by the framework. Otherwise,
	 * <ul>
	 * <li>For errors related to invalid request values, add Error entries to the MessageManager</li>
	 * <li>For application errors, add a System entry to the MessageManager</li>
	 * </ul>
	 * Throw an {@link ApplicationException}. Messages added to the MessageManager will still be returned via a {@link MessagesOnlyOutboundDto}.
	 * <p>
	 * <b>Note:</b> If this method returns an outbound DTO and the MessageManager indicates an error, the DTO will be ignored, and the framework will
	 * generate an ApplicationException. However, extending classes should <i>not</i> rely on this as a feature.</li>
	 * <p>
	 * All other RuntimeExceptions emitted here will lead the service layer to place a System message in the MessageManager.
	 * 
	 * @param inboundDto
	 *            required
	 * @return never null
	 */
	protected abstract BaseOutboundDto<?> process() throws ApplicationException;

	/**
	 * This method is a facade to invoke {@link #process(BaseInboundDto)}. It then checks the {@link MessageManager} for errors. If errors, it throws
	 * an {@link ApplicationException}; if no errors, it returns the BaseOutboundDto.
	 * <p>
	 * This method should be invoked by the transaction layer only.
	 * 
	 * @param inboundDtoParm
	 * @return
	 * @throws ApplicationException
	 */
	@TreatAsRestricted("Called by TransactionLayer.process() only")
	public final BaseOutboundDto<?> processInternal() throws ApplicationException {
		final BaseOutboundDto<?> outboundDto;
		try {
			outboundDto = process();
		}
		catch (final ApplicationException ae) {
			// We expect that logging and MessageManager were already handled
			throw ae;
		}
		catch (final Exception e) {
			LOG.error("An unexpected exception occurred during processing: ", e);

			MessageManager.addSystem();

			throw new ApplicationException("Processing failed.");
		}

		if (MessageManager.isError()) {
			throw new ApplicationException("Processing completed with errors.");
		}

		// Outbound DTO should be valid, treat as System failure if not
		if (outboundDto.isValidationErrors()) {
			for (String validationError : outboundDto.getValidationErrors()) {
				LOG.error(validationError);
			}

			MessageManager.addSystem();

			throw new ApplicationException("Validation errors found.");
		}

		return outboundDto;
	}

	protected T getInboundDto() {
		return inboundDto;
	}
}
