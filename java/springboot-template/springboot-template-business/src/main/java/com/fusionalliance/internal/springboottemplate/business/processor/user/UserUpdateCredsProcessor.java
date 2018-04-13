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
 * This class implements a {@link BusinessProcessor} for {@link User} creds updates.
 */
public class UserUpdateCredsProcessor extends BusinessProcessor<UserInboundDto> {

	/**
	 * Constructor
	 * 
	 * @param userInboundDtoParm
	 *            required
	 */
	public UserUpdateCredsProcessor(final UserInboundDto userInboundDtoParm) {
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

		if (!user.getCreds().equals(inboundDto.getExistingCreds())) {
			MessageManager.addError("User login %1$s (%2$s) existing creds do not match.", user.getLogin(), user.getFullName());

			throw new ApplicationException("Creds do not match.");
		}

		user //
				.creds(inboundDto.getUpdateToCreds()) //
		;

		if (!user.validate()) {
			throw new ApplicationException("User validation failed");
		}

		MessageManager.addInfo("User login %1$s (%2$s) creds were updated.", user.getLogin(), user.getFullName());

		return new MessagesOnlyOutboundDto().build();
	}
}
