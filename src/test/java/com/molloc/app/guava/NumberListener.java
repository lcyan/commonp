package com.molloc.app.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class NumberListener
{

	Logger logger = LoggerFactory.getLogger(NumberListener.class);

	private Number lastMessage;

	@Subscribe
	public void listen(Number integer)
	{
		lastMessage = integer;
		logger.info("Message: {}", lastMessage);
	}

	public Number getLastMessage()
	{
		return lastMessage;
	}
}
