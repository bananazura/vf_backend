package com.byelken.auto.carpark.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.byelken.auto.carpark.exception.InvalidTokenException;
import com.byelken.auto.carpark.model.User;
import com.byelken.auto.carpark.repository.UserRepository;

/**
 * @author Berkay.Yelken
 */

@RestController
@RequestMapping(value = "/api/auth", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController
{
	@PostMapping("/login")
	@CrossOrigin
	public ResponseEntity<Map<String, String>> login(@RequestHeader(HttpHeaders.AUTHORIZATION)
	String author) throws InvalidTokenException
	{
		return ok(UserRepository.doLogin(author));
	}

	@PostMapping("/signup")
	@CrossOrigin
	public ResponseEntity<Map<String, String>> signup(@RequestBody
	User userModel)
	{
		return ok(UserRepository.doSignup(userModel));
	}

}
