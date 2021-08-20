package com.byelken.auto.carpark.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Berkay.Yelken
 */
public class JwtTokenFilter extends GenericFilterBean
{
	private static final String HEADER_PREFIX = "Bearer ";

	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider)
	{
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException
	{

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

		String token = resolveToken(request);

		if (token != null && jwtTokenProvider.validateToken(token))
		{
			Authentication auth = jwtTokenProvider.getAuthentication(token);

			if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
			{
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(req, res);
	}

	private String resolveToken(HttpServletRequest request)
	{
		String bearerToken = request.getHeader("auth-token");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX))
		{
			return bearerToken.substring(7);
		}
		return null;
	}
}
