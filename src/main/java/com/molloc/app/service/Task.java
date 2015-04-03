package com.molloc.app.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task implements Serializable
{
	
	protected transient Logger logger = LoggerFactory.getLogger(getClass());
	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private long id = 1L;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5546244255530819089L;


	protected long getId()
	{
		return id;
	}

	protected void setId(long id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
