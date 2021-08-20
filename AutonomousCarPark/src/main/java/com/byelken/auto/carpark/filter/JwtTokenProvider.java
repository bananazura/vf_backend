package com.byelken.auto.carpark.filter;

import static java.util.stream.Collectors.joining;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.byelken.auto.carpark.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider
{
	private static final String AUTHORITIES_KEY = "roles";

	private static final JwtProperties jwtProperties = new JwtProperties();

	private SecretKey secretKey;

	{
		String secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
		secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createToken(Authentication authentication)
	{

		String username = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Claims claims = Jwts.claims().setSubject(username);
		if (!authorities.isEmpty())
			claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validity = now.plus(jwtProperties.getValidityInMs(), ChronoField.MILLI_OF_DAY.getBaseUnit());

		return Jwts.builder().setClaims(claims).setIssuedAt(convertDate(now)).setExpiration(convertDate(validity))
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();

	}

	private Date convertDate(LocalDateTime local)
	{
		return Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
	}

	public Authentication getAuthentication(String token)
	{
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

		Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

		Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
				? AuthorityUtils.NO_AUTHORITIES
				: AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

		User principal = new User();

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public String getEmail(String token)
	{
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token)
	{
		try
		{
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		}
		catch (JwtException | IllegalArgumentException e)
		{
			//TODO:
		}
		return false;
	}
}
