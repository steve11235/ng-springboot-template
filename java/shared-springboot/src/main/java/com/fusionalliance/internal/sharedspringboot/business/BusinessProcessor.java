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
import com.fusionalliance.internal.sharedutility.core.TreatAsProtected;
import com.fusionalliance.internal.sharedutility.core.ValidationUtility;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;

/**
 * This abstract class provides the base for business-layer processor classes.
 * <p>
 * Instances of implementing classes are stateful and not thread safe.
 * 
 * @param <TI>
 *            the type of the inbound DTO
 */
public abstract class BusinessProcessor<TI extends BaseInboundDto<?>> {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessProcessor.class);

	private final TI inboundDto;

	/**
	 * Constructor
	 * 
	 * @param inboundDtoParm
	 *            required
	 */
	public BusinessProcessor(final TI inboundDtoParm) {
		ValidationUtility.checkObjectNotNull("Inbound DTO is required.", inboundDtoParm);

		inboundDto = inboundDtoParm;
	}

	/**
	 * Perform business processing on the request information in the inbound DTO, return a non-null outbound DTO containing the response.
	 * <p>
	 * <b>Note:</b> This method may emit {@link ApplicationException}, which indicates that the business process encountered one or more errors that
	 * prevented this method from generating an outbound DTO. There are two main scenarios.
	 * <ul>
	 * <li>An unexpected exception occurred that was caught in the business layer. A System message should be added to the MessageManager, the
	 * exception should be logged, and a ApplicationException thrown.</li>
	 * <li>After applying the inbound request information to entities, one or more validation errors occur, as indicated by
	 * {@link MessageManager#isError()}. Any logging should already have been performed. The MessageManager already contains appropriate error
	 * messages. An ApplicationException should be thrown.<br>
	 * <b>Note:</b> If this method returns an outbound DTO and the MessageManager indicates an error, the DTO will be ignored, and the framework will
	 * generate an ApplicationException. However, extended classes should not rely on this as a feature.</li>
	 * </ul>
	 * Emitting a ApplicationException does not cause the service layer to emit an exception response. Instead, a {@link MessagesOnlyOutboundDto} will
	 * be instantiated, populated from the MessageManager, and a response generated. Clients are required to understand this abbreviated response.
	 * <p>
	 * All other RuntimeExceptions emitted here will lead the service layer to place System messages in the MessageManager.
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
	@TreatAsProtected("Called by the transaction layer classes only")
	public BaseOutboundDto<?> processInternal() throws ApplicationException {
		try {
			final BaseOutboundDto<?> outboundDto = process();

			if (MessageManager.isError()) {
				throw new ApplicationException("Processing completed with errors.");
			}

			return outboundDto;
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
	}

	protected TI getInboundDto() {
		return inboundDto;
	}
}
