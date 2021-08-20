package com.byelken.auto.carpark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.byelken.auto.carpark.exception.EmailNotFoundException;
import com.byelken.auto.carpark.filter.JwtTokenFilter;
import com.byelken.auto.carpark.filter.JwtTokenProvider;
import com.byelken.auto.carpark.repository.IUserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{

	@Bean
	public SecurityFilterChain springWebFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception
	{
		return http.cors().and().httpBasic(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.authorizeRequests(c -> c.antMatchers("/api/rest/**").authenticated())
				.addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public UserDetailsService customUserDetailsService(IUserRepository users)
	{
		return (email) -> users.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email: " + email + " not found"));
	}

	@Bean
	public AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder encoder)
	{
		return authentication -> {
			String username = authentication.getPrincipal() + "";
			String password = authentication.getCredentials() + "";

			UserDetails user = userDetailsService.loadUserByUsername(username);

			if (!encoder.matches(password, user.getPassword()))
			{
				throw new BadCredentialsException("Bad credentials");
			}

			if (!user.isEnabled())
			{
				throw new DisabledException("User account is not active");
			}

			return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
		};
	}
}
