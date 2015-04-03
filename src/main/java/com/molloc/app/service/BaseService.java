package com.molloc.app.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author robot
 * @Description: 所有service的基类
 * @Date 2015年2月16日
 * @Version v1.0
 */
public class BaseService implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8467460508711585637L;
	protected transient Logger logger = LoggerFactory.getLogger(getClass());
}
