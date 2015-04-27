package com.molloc.app.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class MultipleListener
{
	Logger logger = LoggerFactory.getLogger(MultipleListener.class);

	public Integer lastInteger;
	public Long lastLong;

	@Subscribe
	public void listenInteger(Integer event)
	{
		lastInteger = event;
		logger.info("event Integer: {}", lastInteger);
	}

	@Subscribe
	public void listenLong(Long event)
	{
		lastLong = event;
		logger.info("event Long: {}", lastLong);
	}

	public Integer getLastInteger()
	{
		return lastInteger;
	}

	public Long getLastLong()
	{
		return lastLong;
	}
}
