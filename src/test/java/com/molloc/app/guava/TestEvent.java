package com.molloc.app.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEvent
{

	Logger logger = LoggerFactory.getLogger(TestEvent.class);

	private final int message;

	public TestEvent(int message)
	{
		this.message = message;
		logger.info("event message: {}", message);
	}

	public int getMessage()
	{
		return message;
	}

}
