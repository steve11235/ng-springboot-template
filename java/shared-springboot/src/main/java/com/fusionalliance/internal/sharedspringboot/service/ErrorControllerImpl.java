/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 19, 2017
 */
package com.fusionalliance.internal.sharedspringboot.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;

/**
 * This class implements a RESTful controller that handles errors. The implementation assumes that these will be the result of unmapped paths.
 */
@RestController
public class ErrorControllerImpl extends BaseController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping( //
			path = ERROR_PATH, // 
			produces = "application/json")
	BaseOutboundDto<?> handleError(final HttpServletRequest servletRequestParm) {
		// Perform request intialization
		MessageManager.initialize();

		MessageManager.addError("An error occurred.");

		final RequestAttributes requestAttributes = new ServletRequestAttributes(servletRequestParm);
		MessageManager.addInfo("Status code: %1d", requestAttributes.getAttribute("javax.servlet.error.status_code", 0));
		MessageManager.addInfo("Request path: %1s", requestAttributes.getAttribute("javax.servlet.error.request_uri", 0));
		MessageManager.addInfo("Method: %1s", servletRequestParm.getMethod());

		return generateOutboundDtoFromMessageManager(StandardCompletionStatus.INVALID_REQUEST);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
