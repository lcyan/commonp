package com.molloc.app.entity.data;

import org.apache.commons.lang3.RandomUtils;

import com.molloc.app.entity.User;

public class UserData
{

	public static User randomNewUser()
	{
		User user = new User();
		user.setLoginName(RandomData.randomName("user"));
		user.setName(RandomData.randomName("User"));
		user.setLoginPwd(RandomData.randomName("password"));
		user.setEmail(RandomData.randomEmail("Email"));
		user.setNickName(RandomData.randomName("nickName"));
		user.setAge(RandomUtils.nextInt(1, 1000));
		return user;
	}
}
