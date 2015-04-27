package com.molloc.app.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class EventListener
{

	Logger logger = LoggerFactory.getLogger(EventListener.class);

	public int lastMessage = 0;

	@Subscribe
	public void listen(TestEvent event)
	{
		lastMessage = event.getMessage();
		logger.info("Message : {}", lastMessage);
	}

	public int getLastMessage()
	{
		return lastMessage;
	}

}
