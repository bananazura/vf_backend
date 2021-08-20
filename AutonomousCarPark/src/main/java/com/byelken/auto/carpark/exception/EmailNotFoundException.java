package com.byelken.auto.carpark.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Berkay.Yelken
 */
public class EmailNotFoundException extends AuthenticationException
{
	private static final long serialVersionUID = 260523028989074622L;

	public EmailNotFoundException(String msg)
	{
		super(msg);
	}

	public EmailNotFoundException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
