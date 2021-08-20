package com.byelken.auto.carpark.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.byelken.auto.carpark.controller.dto.GarageDTO;
import com.byelken.auto.carpark.exception.RestControllerException;
import com.byelken.auto.carpark.model.Garage;
import com.byelken.auto.carpark.repository.GarageRepository;

/**
 * @author Berkay.Yelken
 */
@RestController
@RequestMapping(value = "/api/rest/garage", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GarageController
{
	@GetMapping
	@CrossOrigin
	public ResponseEntity<List<GarageDTO>> getAllCarsOnGarage()
	{
		return ok(GarageRepository.getParkedCars());
	}

	@PostMapping("/park")
	@CrossOrigin
	public ResponseEntity<Map<String, Object>> park(@RequestBody
	Garage car) throws RestControllerException
	{
		return ok(GarageRepository.park(car));
	}

	@DeleteMapping("/leave")
	@CrossOrigin
	public ResponseEntity<Map<String, Object>> leave(@RequestParam("plaque")
	String plaque) throws RestControllerException
	{
		return ok(GarageRepository.leave(plaque));
	}
}
