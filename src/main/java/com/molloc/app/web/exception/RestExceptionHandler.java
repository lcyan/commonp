package com.molloc.app.web.exception;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.molloc.app.entity.JsonResult;
import com.molloc.app.web.utils.BeanValidators;
import com.molloc.app.web.utils.JsonMapper;
import com.molloc.app.web.utils.MediaTypes;

/**
 * @Description:自定义ExceptionHandler，专门处理Restful异常
 * @author admin
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	private JsonMapper jsonMapper = new JsonMapper();

	/**
	 * 处理RestException.
	 */
	@ExceptionHandler(value = { RestException.class })
	public final ResponseEntity<?> handleException(RestException ex, WebRequest request) {
		JsonResult jr = new JsonResult();
		jr.setResult(0);
		String body = jsonMapper.toJson(jr);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.JSON_UTF_8));
		return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * 处理JSR311 Validation异常.
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public final ResponseEntity<?> handleException(ConstraintViolationException ex, WebRequest request) {
		Map<String, String> errors = BeanValidators.extractPropertyAndMessage(ex.getConstraintViolations());
		String body = jsonMapper.toJson(errors);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
		return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
	}
}
