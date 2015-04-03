package com.molloc.app.web.controller;

import javax.annotation.Resource;
import javax.validation.Validator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molloc.app.entity.User;
import com.molloc.app.web.exception.RestException;

@RestController
@RequestMapping("/user")
public class RestUserController extends BaseController
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -242704800870479783L;
	//private transient final Logger logger = LoggerFactory.getLogger(RestUserController.class);

	@Resource
	private Validator validator;

	@RequestMapping("/add")
	public Object addUser(User user)
	{
		throw new RestException(HttpStatus.NOT_FOUND, "该用户不存在");
	}
}
