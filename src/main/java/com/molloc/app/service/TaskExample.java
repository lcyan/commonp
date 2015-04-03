package com.molloc.app.service;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskExample extends Task
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5587900985328285934L;
	
	@Scheduled(cron="* 55 * * * ?")
	public void execute()
	{
		logger.info("task running at [{}]", DateFormatUtils.format(Calendar.getInstance(), Task.TIMESTAMP_PATTERN));
	}
}
