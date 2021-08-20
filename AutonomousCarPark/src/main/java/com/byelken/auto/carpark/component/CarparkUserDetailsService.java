package com.byelken.auto.carpark.component;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.byelken.auto.carpark.exception.EmailNotFoundException;
import com.byelken.auto.carpark.repository.IUserRepository;

/**
 * @author Berkay.Yelken
 */
public class CarparkUserDetailsService implements UserDetailsService
{
	private IUserRepository user;

	public CarparkUserDetailsService(IUserRepository user)
	{
		this.user = user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws EmailNotFoundException
	{
		return user.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email: " + email + " not found"));
	}

}
