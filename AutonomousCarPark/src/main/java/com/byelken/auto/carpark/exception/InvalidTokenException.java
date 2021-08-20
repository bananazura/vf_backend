package com.byelken.auto.carpark.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Berkay.Yelken
 */
public class InvalidTokenException extends AuthenticationException
{
	private static final long serialVersionUID = -4610435535279429935L;

	public InvalidTokenException(String msg)
	{
		super(msg);
	}

	public InvalidTokenException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

}
