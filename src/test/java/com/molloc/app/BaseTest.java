package com.molloc.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class BaseTest
{
	protected transient Logger logger = LoggerFactory.getLogger(getClass());

	protected void log(Object log)
	{
		logger.info("{}", log);
	}

	protected void logLine()
	{
		logger.info("{}", Strings.repeat("--", 30));
	}
}
