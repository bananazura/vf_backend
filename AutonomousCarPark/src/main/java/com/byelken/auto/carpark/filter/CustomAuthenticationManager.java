package com.byelken.auto.carpark.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.byelken.auto.carpark.model.User;
import com.byelken.auto.carpark.repository.UserRepository;

public class CustomAuthenticationManager implements AuthenticationManager
{
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String email = authentication.getPrincipal() + "";
		String password = authentication.getCredentials() + "";
		User user = UserRepository.findUserByEmail(email);
		if (user == null || !password.equals(user.getPassword()))
			throw new BadCredentialsException("1000");
		return new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());
	}
}
