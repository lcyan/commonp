package com.molloc.app.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.molloc.app.entity.JsonResult;

public abstract class BaseController implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6085005793397220224L;
	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 返回验证域的错误信息，将第一个放在主错误信息中
	 * 
	 * @param obj
	 * @param result
	 * @return
	 */
	public JsonResult handleJsonValid(JsonResult obj, BindingResult result)
	{
		Map<String, String> errorMap = new HashMap<String, String>();
		List<FieldError> fieldErrors = result.getFieldErrors();
		for (int i = 0, j = fieldErrors.size(); i < j; i++)
		{
			FieldError error = fieldErrors.get(i);
			if (i == 0)
			{
				obj.setErrmsg(error.getDefaultMessage());// 存放第一个错误消息
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("在[{}]字段上发生校验错误,错误信息:[{}]", error.getField(), error.getDefaultMessage());
			}
			errorMap.put(error.getField(), error.getDefaultMessage());
		}
		obj.set("errfields", errorMap);
		obj.setResult(1);
		return obj;

	}
}
