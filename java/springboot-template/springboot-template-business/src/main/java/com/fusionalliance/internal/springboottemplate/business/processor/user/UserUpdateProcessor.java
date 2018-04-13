/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 12, 2018
 */
package com.fusionalliance.internal.springboottemplate.business.processor.user;

import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.api.MessagesOnlyOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.application.ApplicationException;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.springboottemplate.api.user.UserInboundDto;
import com.fusionalliance.internal.springboottemplate.business.dao.DefaultDao;
import com.fusionalliance.internal.springboottemplate.business.entity.User;

/**
 * This class implements a {@link BusinessProcessor} for {@link User} updates.
 */
public class UserUpdateProcessor extends BusinessProcessor<UserInboundDto> {

	/**
	 * Constructor
	 * 
	 * @param userInboundDtoParm
	 *            required
	 */
	public UserUpdateProcessor(final UserInboundDto userInboundDtoParm) {
		super(userInboundDtoParm);
	}

	@Override
	protected BaseOutboundDto<?> process() throws ApplicationException {
		final UserInboundDto inboundDto = getInboundDto();

		final User user = SpringContextHelper.getBeanByClass(DefaultDao.class).get(User.class, inboundDto.getUserKey());
		if (user == null) {
			MessageManager.addError("The user was not found.");

			throw new ApplicationException("User not found");
		}

		user //
				.admin(inboundDto.isAdmin()) //
				// .creds(inboundDto.getCreds()) do not modify
				.deactivated(inboundDto.isDeactivated()) //
				.description(inboundDto.getDescription()) //
				.fullName(inboundDto.getFullfullName()) //
				.login(inboundDto.getLogin()) //
		// .userKey(inboundDto.getUserKey()) do not modify 
		;

		if (!user.validate()) {
			throw new ApplicationException("User validation failed");
		}

		MessageManager.addInfo("User login %1$s (%2$s) was updated.", user.getLogin(), user.getFullName());

		return new MessagesOnlyOutboundDto().build();
	}
}
