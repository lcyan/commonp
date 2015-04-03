package com.molloc.app.web.exception;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4323287145201896977L;

	public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	public RestException()
	{
	}

	public RestException(HttpStatus status)
	{
		this.status = status;
	}

	public RestException(String message)
	{
		super(message);
	}

	public RestException(HttpStatus status, String message)
	{
		super(message);
		this.status = status;
	}

}
