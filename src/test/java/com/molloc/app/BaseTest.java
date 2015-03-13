package com.molloc.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest
{
	protected transient Logger logger = LoggerFactory.getLogger(getClass());

	protected void log(Object log)
	{
		logger.info(log.toString());
		//logger.info(ToStringBuilder.reflectionToString(log, ToStringStyle.SHORT_PREFIX_STYLE));
	}
}
