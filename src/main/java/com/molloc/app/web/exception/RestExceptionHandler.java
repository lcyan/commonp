package com.molloc.app.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.molloc.app.entity.JsonResult;
import com.molloc.app.web.utils.JsonMapper;

/**
 * @CopyRight: SinoSoft
 * @Description:自定义ExceptionHandler，专门处理Restful异常
 * @Author: yanleichang
 * @Create: 2014年12月26日
 */
@ControllerAdvice
public class RestExceptionHandler
{
	private JsonMapper jsonMapper = new JsonMapper();

	/**
	 * 处理RestException.
	 * @throws Exception 
	 */
	@ExceptionHandler(value = { RestException.class })
	public final void handleException(RestException ex, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JsonResult jr = new JsonResult();
		jr.setResult(0);
		jr.setErrmsg(ex.getMessage());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().write(jsonMapper.toJson(jr).getBytes());
	}
}
