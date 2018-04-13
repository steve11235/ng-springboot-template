/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 11, 2018
 */
package com.fusionalliance.internal.springboottemplate.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserGetProcessor;
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserListProcessor;
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserUpdateCredsProcessor;
import com.fusionalliance.internal.springboottemplate.business.processor.user.UserUpdateProcessor;

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
			@RequestParam("includeDeactivated") final Optional<Boolean> includeDeativatedParm, //
			@RequestParam("includeAdminOnly") final Optional<Boolean> includeAdminOnlyParm //
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
				.admin(includeAdminOnlyParm.orElse(Boolean.FALSE)) //
				.deactivated(includeDeativatedParm.orElse(Boolean.FALSE)) //
				.build();

		final UserListProcessor processor = new UserListProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.process(processor, false);
		final String json = outboundDto.toJson();

		return json;
	}

	/**
	 * Return a User. Requires admin.
	 * 
	 * @param loginInfoParm
	 * @param userKeyParm
	 * @return
	 */
	@GetMapping( //
			path = { "/userKey/{userKey}" }, //
			produces = "application/json" //
	)
	String handleUserGetRequest( //
			@RequestAttribute("loginInfo") final LoginInfo loginInfoParm, //
			@PathVariable("userKey") final long userKeyParm //
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
				.requestTypeHolder(new UserRequestTypeHolder(UserRequestTypeHolder.GET)) //
				.loginInfo(loginInfoParm) //
				.userKey(userKeyParm) //
				.build();

		final UserGetProcessor processor = new UserGetProcessor(inboundDto);
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

	/**
	 * Update a User.
	 * <p>
	 * The path userKey overrides any userKey supplied in the JSON.
	 * 
	 * @param loginInfoParm
	 * @param includeDeativatedParm
	 * @param adminOnlyParm
	 * @return
	 */
	@PatchMapping( //
			path = { "/userKey/{userKey}" }, //
			produces = "application/json" //
	)
	String handleUserUpdateRequest( //
			@RequestAttribute("loginInfo") final LoginInfo loginInfoParm, //
			@PathVariable("userKey") final long userKeyParm, //
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
				.requestTypeHolder(new UserRequestTypeHolder(UserRequestTypeHolder.UPDATE)) //
				.loginInfo(loginInfoParm) //
				.userKey(userKeyParm) //
				.build();

		final UserUpdateProcessor processor = new UserUpdateProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.process(processor, true);
		final String json = outboundDto.toJson();

		return json;
	}

	/**
	 * Update a User's creds.
	 * 
	 * @param loginInfoParm
	 * @param includeDeativatedParm
	 * @param adminOnlyParm
	 * @return
	 */
	@PatchMapping( //
			path = { "/userKey/{userKey}/creds" }, //
			produces = "application/json" //
	)
	String handleUserCredsUpdateRequest( //
			@RequestAttribute("loginInfo") final LoginInfo loginInfoParm, //
			@PathVariable("userKey") final long userKeyParm, //
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
				.requestTypeHolder(new UserRequestTypeHolder(UserRequestTypeHolder.UPDATE_CREDS)) //
				.userKey(userKeyParm) //
				.build();

		final UserUpdateCredsProcessor processor = new UserUpdateCredsProcessor(inboundDto);
		final BaseOutboundDto<?> outboundDto = transactionLayerFacade.process(processor, true);
		final String json = outboundDto.toJson();

		return json;
	}
}
