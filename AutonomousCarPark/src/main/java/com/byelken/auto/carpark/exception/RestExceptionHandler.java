package com.byelken.auto.carpark.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Berkay.Yelken
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler({ InvalidTokenException.class, EmailNotFoundException.class })
	public ResponseEntity<String> handleTemplateException(HttpServletRequest req, AuthenticationException e)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler({ RestControllerException.class })
	public ResponseEntity<String> handleTemplateException(HttpServletRequest req, RestControllerException e)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleTemplateException(HttpServletRequest req, Exception e)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

}
