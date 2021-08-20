package com.byelken.auto.carpark.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties
{
	private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";
	private long validityInMs = 3600000;

	public String getSecretKey()
	{
		return secretKey;
	}

	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}

	public long getValidityInMs()
	{
		return validityInMs;
	}

	public void setValidityInMs(long validityInMs)
	{
		this.validityInMs = validityInMs;
	}

}
