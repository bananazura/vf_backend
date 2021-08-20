package com.byelken.auto.carpark.controller;

import static org.junit.Assert.assertEquals;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.byelken.auto.carpark.base.TestBase;
import com.byelken.auto.carpark.model.CarType;
import com.byelken.auto.carpark.model.Garage;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author Berkay.Yelken
 */
public class GarageControllerTest extends TestBase
{
	private String authToken = "";

	@BeforeEach
	public void createToken() throws Exception
	{
		String email = "admin@vfone.com";
		String pass = "pass1234";
		String password = getCryptedPass(email, pass);
		StringBuilder sb = new StringBuilder();

		String authorization = new String(
				Base64.getEncoder().encode(sb.append(email).append(splitter).append(password).toString().getBytes()));

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
				.header(HttpHeaders.AUTHORIZATION, authorization).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String tokenJson = new String(mvcResult.getResponse().getContentAsByteArray());
		@SuppressWarnings("deprecation")
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(tokenJson);
		authToken = "Bearer " + json.get("token");
	}

	@Test
	public void parkCar() throws Exception
	{
		Garage car = new Garage();
		car.setCarType(CarType.TRUCK);
		car.setColor("Red");
		car.setPlaque("55BYE55");

		String json = mapToJson(car);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/rest/garage/park").header("auth-token", authToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	@Test
	public void leave() throws Exception
	{
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/rest/garage/leave").header("auth-token", authToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE).param("plaque", "08BYE55")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void getGarageCars() throws Exception
	{
//		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/rest/garage").header("auth-token", authToken)
//				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		assertEquals(200, status);
	}
}
