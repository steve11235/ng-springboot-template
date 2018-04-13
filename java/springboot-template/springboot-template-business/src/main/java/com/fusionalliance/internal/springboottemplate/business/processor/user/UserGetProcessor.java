/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 12, 2018
 */
package com.fusionalliance.internal.springboottemplate.business.processor.user;

import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.springboottemplate.api.user.UserInboundDto;
import com.fusionalliance.internal.springboottemplate.api.user.UserOutboundDto;
import com.fusionalliance.internal.springboottemplate.business.dao.DefaultDao;
import com.fusionalliance.internal.springboottemplate.business.entity.User;

/**
 * This class implements a {@link BusinessProcessor} for {@link User} gets.
 */
public class UserGetProcessor extends BusinessProcessor<UserInboundDto> {

	/**
	 * Constructor
	 * 
	 * @param userInboundDtoParm
	 *            required
	 */
	public UserGetProcessor(final UserInboundDto userInboundDtoParm) {
		super(userInboundDtoParm);
	}

	@Override
	protected BaseOutboundDto<?> process() throws ApplicationException {
		final UserInboundDto inboundDto = getInboundDto();

		final User user = SpringContextHelper.getBeanByClass(DefaultDao.class).get(User.class, inboundDto.getUserKey());

		if (user == null) {
			MessageManager.addError("The request user was not found.");

			throw new ApplicationException("User not found");
		}

		final UserOutboundDto outboundDto = new UserOutboundDto() //
				.admin(user.isAdmin()) //
				.deactivated(user.isDeactivated()) //
				.description(user.getDescription()) //
				.fullName(user.getFullName()) //
				.login(user.getLogin()) //
				.userKey(user.getUserKey()) //
				.build();

		return outboundDto;
	}
}
