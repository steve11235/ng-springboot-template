/**
 * Copyright 2018 by Steve Page of Fusion Alliance
 *
 * Created Apr 6, 2018
 */
package com.fusionalliance.internal.springboottemplate.api.user;

import java.util.ArrayList;
import java.util.List;

import com.fusionalliance.internal.sharedspringboot.api.BaseOutboundDto;

/**
 * This class implements an outbound DTO that contains a list of {@link UserOutboundDto}.
 */
public class UserListOutboundDto extends BaseOutboundDto<UserListOutboundDto> {
	private final List<UserOutboundDto> userList = new ArrayList<>();

	@Override
	public void validate() {
		super.validate();

		for (UserOutboundDto user : userList) {
			validateChildDto(user);
		}
	}

	public List<UserOutboundDto> getUserList() {
		return new ArrayList<>(userList);
	}

	public UserListOutboundDto addUser(final UserOutboundDto userOutboundDtoParm) {
		checkNotBuilt();

		if (userOutboundDtoParm != null) {
			userList.add(userOutboundDtoParm);
		}

		return this;
	}
}
