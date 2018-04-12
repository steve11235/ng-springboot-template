/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 11, 2018
 */
package com.fusionalliance.internal.springboottemplate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedspringboot.service.BaseController;
import com.fusionalliance.internal.sharedspringboot.service.LoginInfo;
import com.fusionalliance.internal.sharedspringboot.transaction.TransactionLayerFacade;
import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.sharedutility.messagemanager.StandardCompletionStatus;
import com.fusionalliance.internal.springboottemplate.api.user.UserInboundDto;
import com.fusionalliance.internal.springboottemplate.api.user.UserRequestTypeHolder;
import com.fusionalliance.internal.springboottemplate.business.entity.User;
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserAddProcessor;
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserListProcessor;

/**
 * This class implements a RESTful controller for {@link User}-related endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/rs/users")
public class UserController extends BaseController {

	private final TransactionLayerFacade transactionLayerFacade;

	/**
	 * Constructor
	 */
	public UserController(@Autowired final TransactionLayerFacade transactionLayerFacadeParm) {
		transactionLayerFacade = transactionLayerFacadeParm;
	}

	/**
	 * Return a User list. Requires admin.
	 * <p>
	 * The path query part must specify two params, includeDeativated=true|false, includeAdminOnly=true|false.
	 * 
	 * @param loginInfoParm
	 * @param includeDeativatedParm
	 * @param adminOnlyParm
	 * @return
	 */
	@GetMapping( //
			path = { "" }, //
			produces = "application/json" //
	)
	String handleUserListRequest( //
			@RequestAttribute("loginInfo") final LoginInfo loginInfoParm, //
			@RequestParam("includeDeactivated") final boolean includeDeativatedParm, //
			@RequestParam("includeAdminOnly") final boolean includeAdminOnlyParm //
	) {
		MessageManager.initialize();

		if (!loginInfoParm.isAdmin()) {
			MessageManager.addError("Login '%1$s' ('%2$s') does not have permission to list users.", loginInfoParm.getLogin(),
					loginInfoParm.getUserName());

			final MessagesOnlyOutboundDto noAuthOutboundDto = generateOutboundDtoFromMessageManager(StandardCompletionStatus.INVALID_REQUEST);
			final String json = noAuthOutboundDto.toJson();

			return json;
		}

		final UserInboundDto inboundDto = new UserInboundDto() //
				.requestTypeHolder(new UserRequestTypeHolder(UserRequestTypeHolder.LIST)) //
				.loginInfo(loginInfoParm) //
				.admin(includeAdminOnlyParm) //
				.deactivated(includeDeativatedParm) //
				.build();

		final UserListProcessor processor = new UserListProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.process(processor, false);
		final String json = outboundDto.toJson();

		return json;
	}

	/**
	 * Add a User.
	 * <p>
	 * The path query part must specify two params, includeDeativated=true|false, includeAdminOnly=true|false.
	 * 
	 * @param loginInfoParm
	 * @param includeDeativatedParm
	 * @param adminOnlyParm
	 * @return
	 */
	@PostMapping( //
			path = { "" }, //
			produces = "application/json" //
	)
	String handleUserAddRequest( //
			@RequestAttribute("loginInfo") final LoginInfo loginInfoParm, //
			@RequestBody() final String requestJsonParm //
	) {
		MessageManager.initialize();

		if (!loginInfoParm.isAdmin()) {
			MessageManager.addError("Login '%1$s' ('%2$s') does not have permission to add users.", loginInfoParm.getLogin(),
					loginInfoParm.getUserName());

			final MessagesOnlyOutboundDto noAuthOutboundDto = generateOutboundDtoFromMessageManager(StandardCompletionStatus.INVALID_REQUEST);
			final String json = noAuthOutboundDto.toJson();

			return json;
		}

		final UserInboundDto inboundDto = GsonHelper.GSON.fromJson(requestJsonParm, UserInboundDto.class) //
				.requestTypeHolder(new UserRequestTypeHolder(UserRequestTypeHolder.ADD)) //
				.loginInfo(loginInfoParm) //
				.build();

		final UserAddProcessor processor = new UserAddProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.process(processor, true);
		final String json = outboundDto.toJson();

		return json;
	}
}
