package com.byelken.auto.carpark.controller;

import static org.junit.Assert.assertEquals;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.byelken.auto.carpark.base.TestBase;
import com.byelken.auto.carpark.model.User;

public class UserControllerTest extends TestBase
{
	@Test
	public void signup() throws Exception
	{
		User user = new User();
		String email = "admin@vfone.com";
		String pass = "pass1234";
		String password = getCryptedPass(email, pass);
		user.setEmail(email);
		user.setPassword(password);
		user.setName("Test Man");
		user.setSurname("JUnit");

		String inputJson = mapToJson(user);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void login() throws Exception
	{
		String email = "admin@vfone.com";
		String pass = "pass1234";
		String password = getCryptedPass(email, pass);
		StringBuilder sb = new StringBuilder();

		String authorization = new String(
				Base64.getEncoder().encode(sb.append(email).append(splitter).append(password).toString().getBytes()));

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
				.header(HttpHeaders.AUTHORIZATION, authorization).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

}
