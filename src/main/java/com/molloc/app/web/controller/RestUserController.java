package com.molloc.app.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molloc.app.entity.JsonResult;
import com.molloc.app.entity.User;

@RestController
@RequestMapping("/user")
public class RestUserController extends BaseController
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -242704800870479783L;
	private transient final Logger logger = LoggerFactory.getLogger(RestUserController.class);

	@RequestMapping("/add")
	public Object addUser(@Valid User user, BindingResult result)
	{

		if (result.hasErrors())
		{
			JsonResult jsonResult = new JsonResult();
			handleJsonValid(jsonResult, result);
			if (logger.isDebugEnabled())
			{
				logger.debug("{}", jsonResult);
			}
			return jsonResult;
		}

		return user;
	}
}
