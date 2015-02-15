package com.molloc.app.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseObject implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8422357657737374476L;

	protected final transient Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}
