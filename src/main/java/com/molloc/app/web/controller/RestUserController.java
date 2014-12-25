package com.molloc.app.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molloc.app.entity.User;

@RestController
@RequestMapping("/user")
public class RestUserController {

	private transient final Logger logger = LoggerFactory.getLogger(RestUserController.class);

	@RequestMapping("/add")
	public User addUser(@Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			logger.debug("{}", result.getFieldError("age").getDefaultMessage());
		}

		return user;
	}
}
