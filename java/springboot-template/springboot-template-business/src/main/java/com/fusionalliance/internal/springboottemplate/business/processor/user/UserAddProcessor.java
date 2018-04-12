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
import com.fusionalliance.internal.springboottemplate.api.user.UserInboundDto;
import com.fusionalliance.internal.springboottemplate.business.dao.DefaultDao;
import com.fusionalliance.internal.springboottemplate.business.entity.User;

/**
 * This class implements a {@link BusinessProcessor} for {@link User} adds.
 */
public class UserAddProcessor extends BusinessProcessor<UserInboundDto> {

	/**
	 * Constructor
	 * 
	 * @param userInboundDtoParm
	 *            required
	 */
	public UserAddProcessor(final UserInboundDto userInboundDtoParm) {
		super(userInboundDtoParm);
	}

	@Override
	protected BaseOutboundDto<?> process() throws ApplicationException {
		final UserInboundDto inboundDto = getInboundDto();

		final User user = new User() //
				.admin(inboundDto.isAdmin()) //
				.creds(inboundDto.getCreds()) //
				.deactivated(inboundDto.isDeactivated()) //
				.description(inboundDto.getDescription()) //
				.fullName(inboundDto.getFullfullName()) //
				.login(inboundDto.getLogin()) //
				.userKey(inboundDto.getUserKey()) //
		;

		if (!user.validate()) {
			throw new ApplicationException("User validation failed");
		}

		SpringContextHelper.getBeanByClass(DefaultDao.class).persist(user);

		return new MessagesOnlyOutboundDto().build();
	}
}
