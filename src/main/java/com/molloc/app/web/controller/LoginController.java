package com.molloc.app.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014859571896163262L;

	@RequestMapping(method = RequestMethod.GET)
	public String login()
	{
		Subject subject = SecurityUtils.getSubject();
		if (null != subject && subject.isAuthenticated())
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("dealing with logout...");
			}
			subject.logout();
		}
		return "account/login";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model)
	{
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		return "account/login";
	}

}
