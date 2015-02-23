package com.molloc.app.web.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.molloc.app.entity.User;

/**
 * @author robot
 * @Description: 首页相关控制器
 * @Date 2015年2月20日
 * @Version v1.0
 */
public class HomeController extends BaseController
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4460187877837176432L;

	public String home(RedirectAttributes redirectAttributes)
	{
		// 登陆成功
		User user = getShiroUser();
		if(null != user){
			setSession(SESSION_KEY_LOGIN_USER, user);
			logger.info(String.format("用户[%s]登陆成功...", user.getLoginName()));
		}
		redirectAttributes.addAttribute(SESSION_KEY_REDIRECT_MESSAGE, "登陆成功.");
		return "home";
	}

}
