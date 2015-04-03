package com.molloc.app.web.controller;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.molloc.app.entity.User;
import com.molloc.app.web.utils.DateUtils;
import com.molloc.app.web.utils.JsonMapper;

public abstract class BaseController implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6085005793397220224L;
	protected transient final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected static final String FILE_UPLOADED_PATH = "/static/uploaded";
	
	protected static final String PAGE_SIZE = "5";
	
	protected static final String SESSION_KEY_LOGIN_USER = "loginUser";
	
	protected static final String SESSION_KEY_REDIRECT_MESSAGE = "message";

	@Autowired
	protected ServletContext servletContext;

	@InitBinder
	protected void initBinder(WebDataBinder binder)
	{
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
		{
			@Override
			public void setAsText(String text)
			{
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 获取Session
	 * 
	 * @return
	 */
	protected Session getSession()
	{
		return SecurityUtils.getSubject().getSession();
	}

	/**
	 *
	 * @Title: setSession
	 * @Description: 将对象存入session
	 * @param @param key
	 * @param @param value
	 * @return void
	 * @throws
	 */
	public void setSession(String key, Object value)
	{
		getSession().setAttribute(key, value);
		if (logger.isDebugEnabled())
		{
			logger.debug("存放[{}]-[{}]到HttpSession.", key, value);
		}
	}

	/**
	 *
	 * @Title: setSession
	 * @Description: 将对象存入session
	 * @param @param key
	 * @param @param value
	 * @return void
	 * @throws
	 */
	public void removeSession(String key)
	{
		getSession().removeAttribute(key);
		if (logger.isDebugEnabled())
		{
			logger.debug("移除[{}]到HttpSession.", key);
		}
	}

	/**
	 * only properties with non-null values are to be included.
	 * 
	 * @param obj 要转换成json格式字符串的对象
	 * @return json格式的字符串
	 */
	protected String toJsonExcludeNullValue(Object obj)
	{
		JsonMapper mapper = JsonMapper.nonEmptyMapper();
		return mapper.toJson(obj);
	}

	/**
	 * 获取Shiro中保存的User
	 *
	 * @return
	 */
	public User getShiroUser()
	{
		return (User) SecurityUtils.getSubject().getPrincipal();
	}

	/**
	 * 获取Shiro中保存的User的ID
	 *
	 * @return
	 */
	public Long getLoginedUserId()
	{
		return getShiroUser().getId();
	}

	/**
	 * 是否是超级管理员
	 *
	 * @param id
	 */
	protected boolean isSupperUser(Long id)
	{
		Validate.notNull(id);
		return (Long.compare(1L, id) == 0);
	}
}
