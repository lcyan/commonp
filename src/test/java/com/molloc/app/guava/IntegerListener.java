package com.molloc.app.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class IntegerListener
{
	Logger logger = LoggerFactory.getLogger(IntegerListener.class);
	private Integer lastMessage;

	@Subscribe
	public void listen(Integer integer)
	{
		lastMessage = integer;
		logger.info("Message: {}", lastMessage);
	}

	public Integer getLastMessage()
	{
		return lastMessage;
	}
}
