package com.fusionalliance.internal.jwt.service;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fusionalliance.internal.jwt.api.AuthorizationInboundDto;
import com.fusionalliance.internal.jwt.business.AuthorizationProcessor;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.RequestTypeHolder;
import com.fusionalliance.internal.sharedspringboot.service.BaseController;
import com.fusionalliance.internal.sharedspringboot.transaction.TransactionLayer;
import com.fusionalliance.internal.sharedspringboot.transaction.TransactionLayerFacade;
import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;

@RestController
@CrossOrigin
public class JwtProviderController extends BaseController {

	private final TransactionLayerFacade transactionLayerFacade = new TransactionLayerFacade(new TransactionLayer());

	@RequestMapping( //
			method = RequestMethod.GET, //
			path = { "/auth/login/{login}/creds/{creds}" }, // 
			produces = "application/json")
	String handleInterviewerListRequest(@PathVariable("login") final String loginParm, @PathVariable("creds") final String credsParm) {

		// Perform request initialization
		MessageManager.initialize();

		final AuthorizationInboundDto inboundDto = new AuthorizationInboundDto() //
				.requestTypeHolder(new RequestTypeHolder(RequestTypeHolder.GET)) //
				.login(loginParm) //
				.creds(credsParm) //
				.build();

		if (inboundDto.isValidationErrors()) {
			addDtoValidationErrorsToMessageManager(inboundDto);

			return generateJsonFromMessageManager(StandardCompletionStatus.INVALID_REQUEST);
		}

		final AuthorizationProcessor processor = new AuthorizationProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.processReadOnly(processor);

		outboundDto.putMessages(MessageManager.makeFinal());

		final String json = GsonHelper.GSON.toJson(outboundDto);

		return json;
	}
}
