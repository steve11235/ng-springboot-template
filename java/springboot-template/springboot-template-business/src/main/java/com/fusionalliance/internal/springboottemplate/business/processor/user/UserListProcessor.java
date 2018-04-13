package com.fusionalliance.internal.springboottemplate.business.processor.user;

import java.util.List;

import com.fusionalliance.internal.sharedspringboot.SpringContextHelper;
import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;
import com.fusionalliance.internal.sharedspringboot.business.BusinessProcessor;
import com.fusionalliance.internal.sharedutility.messagemanager.MessageManager;
import com.fusionalliance.internal.springboottemplate.api.user.UserInboundDto;
import com.fusionalliance.internal.springboottemplate.api.user.UserListOutboundDto;
import com.fusionalliance.internal.springboottemplate.api.user.UserOutboundDto;
import com.fusionalliance.internal.springboottemplate.business.dao.UserDao;
import com.fusionalliance.internal.springboottemplate.business.entity.User;

/**
 * This class implements a {@link BusinessProcessor} that lists {@link User}.
 */
public class UserListProcessor extends BusinessProcessor<UserInboundDto> {

	/**
	 * Constructor
	 * 
	 * @param inboundDtoParm
	 *            required
	 */
	public UserListProcessor(final UserInboundDto inboundDtoParm) {
		super(inboundDtoParm);
	}

	@Override
	protected BaseOutboundDto<?> process() {
		final UserInboundDto inboundDto = getInboundDto();
		final UserDao userDao = SpringContextHelper.getBeanByClass(UserDao.class);

		final List<User> userList = userDao.retrieveUserList(inboundDto.isAdmin(), inboundDto.isAdmin());

		final UserListOutboundDto outboundDto = new UserListOutboundDto();
		UserOutboundDto userOutboundDto = null;
		for (User user : userList) {
			userOutboundDto = new UserOutboundDto() //
					.admin(user.isAdmin()) //
					.deactivated(user.isDeactivated()) //
					.description(user.getDescription()) //
					.fullName(user.getFullName()) //
					.login(user.getLogin()) //
					.userKey(user.getUserKey()) //
					.build();
			outboundDto.addUser(userOutboundDto);
		}
		outboundDto.build();

		if (outboundDto.getUserList().isEmpty()) {
			MessageManager.addWarn("No users were found for deactivated = %1$b, admin only = %2$b.", inboundDto.isDeactivated(),
					inboundDto.isAdmin());
		}
		else {
			MessageManager.addInfo("User list generated for  deactivated = %1$b, admin only = %2$b.", inboundDto.isDeactivated(),
					inboundDto.isAdmin());
		}

		return outboundDto;
	}
}
