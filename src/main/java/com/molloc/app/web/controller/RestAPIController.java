package com.molloc.app.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.molloc.app.base.BaseInterface;
import com.molloc.app.entity.Version;

@RestController
@RequestMapping("/rest")
public class RestAPIController
{

	private static final Logger logger = LoggerFactory.getLogger(RestAPIController.class);

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public Version showVersion()
	{
		Version version = new Version();
		version.setVersion("V1.0");
		version.setDescription("first initial project.");
		if (logger.isDebugEnabled())
		{
			logger.debug("{}", version);
		}
		return version;
	}

	@RequestMapping("/json")
	public String json()
	{
		Group group = new Group();
		group.setId(1001L);
		group.setName(null);

		User guestUser = new User();
		guestUser.setId(2L);
		guestUser.setBirthday(new Date());
		guestUser.setName("guest");

		User rootUser = new User();
		rootUser.setId(3L);
		rootUser.setBirthday(new DateTime().plusDays(10).toDate());
		rootUser.setName("root");
		
		Map<String, User> maps = Maps.newHashMap();
		maps.put("u1", rootUser);
		maps.put("u2", rootUser);

		group.getUsers().add(guestUser);
		group.getUsers().add(rootUser);
		return JSON.toJSONString(maps, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullStringAsEmpty);

	}
}

class Group
{

	private Long id;
	private String name;
	private List<User> users = Lists.newArrayList();

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}

class User
{

	private Long id;
	private String name;
	private Date birthday;

	@JSONField(ordinal = 2)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@JSONField(ordinal = 3)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JSONField(name = "user_birthday", ordinal = 1, format = BaseInterface.FORMAT_TIMESTAMP_PATTERN)
	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

}
