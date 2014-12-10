package com.molloc.app.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.molloc.app.entity.Version;

@RestController
@RequestMapping("/rest")
public class RestAPIController
{

	private static final Logger logger = LoggerFactory.getLogger(RestAPIController.class);

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public Version showVersion()
	{
		Version version = new Version();
		version.setVersion("V1.0");
		version.setDescription("first initial project.");
		if (logger.isDebugEnabled())
		{
			logger.debug("{}", version);
		}
		return version;
	}
}
