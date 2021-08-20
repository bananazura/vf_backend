package com.byelken.auto.carpark.repository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.byelken.auto.carpark.config.HibernateConfig;
import com.byelken.auto.carpark.controller.dto.GarageDTO;
import com.byelken.auto.carpark.exception.RestControllerException;
import com.byelken.auto.carpark.model.Garage;

/**
 * @author Berkay.Yelken
 */
@Repository
public class GarageRepository
{
	private static SessionFactory sessionFactory;
	private static final Locale locale = new Locale("NEUT");

	static
	{
		sessionFactory = HibernateConfig.getSessionFactory();
	}

	public static List<GarageDTO> getParkedCars()
	{
		Session session = sessionFactory.openSession();
		List<Garage> garageCars = session.createQuery("SELECT a FROM Garage a", Garage.class).getResultStream().sorted()
				.collect(Collectors.toList());
		session.close();

		List<GarageDTO> dto = garageCars.stream().map(GarageRepository::convertDto).collect(Collectors.toList());
		return dto;
	}

	public static Map<String, Object> park(Garage car) throws RestControllerException
	{
		handleCarPlaque(car);
		findSlot(car);

		try (Session session = sessionFactory.openSession())
		{
			Transaction tx = session.beginTransaction();
			try
			{
				session.persist(car);
			}
			catch (Exception e)
			{
				tx.rollback();
				throwException("Car " + car.getPlaque() + " cannot be parked due to problem: " + e.getMessage());
			}
			tx.commit();
		}

		Map<String, Object> model = new ConcurrentHashMap<>();
		model.put("message", "Allocated slot " + car.getParkSlot() + ".");
		model.put("car", convertDto(car));
		return model;
	}

	public static Map<String, Object> leave(String plaque) throws RestControllerException
	{
		plaque = handlePlaque(plaque);
		Map<String, Object> model = new ConcurrentHashMap<>();
		try (Session session = sessionFactory.openSession())
		{
			Transaction tx = session.beginTransaction();
			try
			{
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Garage> criteria = builder.createQuery(Garage.class);
				Root<Garage> root = criteria.from(Garage.class);

				criteria.select(root).where(root.get("plaque").in(plaque));
				TypedQuery<Garage> query = session.createQuery(criteria);
				Garage car = query.getSingleResult();
				session.delete(car);
				String message = "leave " + car.getParkSlot();
				model.put("message", message);
			}
			catch (Exception e)
			{
				tx.rollback();
				throwException("Car " + plaque + " cannot be leaved due to problem: " + e.getMessage());
			}

			tx.commit();
		}

		model.put("cars", getParkedCars());

		return model;
	}

	private static void findSlot(Garage car) throws RestControllerException
	{
		Session session = sessionFactory.openSession();
		List<Garage> garageCars = session.createQuery("SELECT a FROM Garage a", Garage.class).getResultStream().sorted()
				.collect(Collectors.toList());
		session.close();
		if (garageCars == null || garageCars.size() == 0)
		{
			car.setParkSlot(1);
			return;
		}

		boolean existCar = garageCars.stream().filter(garageCar -> garageCar.getPlaque().equals(car.getPlaque())).count() > 0;

		if (existCar)
			throwException("The car has already been parked.");

		int unavailableSlotCount = 0;
		Garage prevCar = null;
		for (Garage parkedCar : garageCars)
		{
			if (prevCar != null)
			{
				int parkingSlot = prevCar.getCarType().getUnitVolume() + prevCar.getParkSlot() + 1;
				int space = parkedCar.getParkSlot() - parkingSlot;
				if (space > parkedCar.getCarType().getUnitVolume())
				{
					car.setParkSlot(parkingSlot);
					return;
				}
			}
			prevCar = parkedCar;
			unavailableSlotCount += parkedCar.getCarType().getUnitVolume() + 1;
		}

		if (unavailableSlotCount == 10)
			throwException("Garage is full.");

		int slotNeeds = 10 - unavailableSlotCount - car.getCarType().getUnitVolume() - 1;
		if (slotNeeds < 0)
			throwException("Garage does not have enough space for your car. Your car plaque is: " + car.getPlaque());

		car.setParkSlot(unavailableSlotCount + 1);
	}

	private static GarageDTO convertDto(Garage car)
	{
		GarageDTO dto = new GarageDTO();

		dto.setCarType(car.getCarType().name());
		dto.setColor(car.getColor());
		dto.setcId(car.getId());
		dto.setPlaque(car.getPlaque());

		StringBuilder slot = new StringBuilder();
		slot.append(car.getParkSlot());
		int start = car.getParkSlot() + 1;
		int end = start + car.getCarType().getUnitVolume() - 1;

		for (int i = start; i < end; i++)
			slot.append(", ").append(i);

		dto.setParkSlot(slot.toString());

		return dto;
	}

	private static void handleCarPlaque(Garage car) throws RestControllerException
	{
		String plaque = car.getPlaque();
		car.setPlaque(handlePlaque(plaque));
	}

	private static String handlePlaque(String plaque) throws RestControllerException
	{
		if (plaque.contains("-"))
		{
			String[] parts = plaque.split("-");
			if (parts.length == 3)
			{
				String city = parts[0].trim();
				String textCode = parts[1].trim();
				String numericCode = parts[2].trim();
				if (city.matches(".*\\D.*") || textCode.matches(".*\\d.*") || numericCode.matches(".*\\D.*"))
					throwException("Plaque is invalid. Plaque: " + plaque);

				plaque = city + "-" + textCode + "-" + numericCode;
				return plaque;
			}
			else
				plaque = plaque.replaceAll("-", "");
		}

		StringBuilder sb = new StringBuilder();
		int partCount = 1;
		String prev = "1";
		for (int i = 0; i < plaque.length(); i++)
		{
			String c = String.valueOf(plaque.charAt(i));
			if (c.matches(".*\\s.*"))
				continue;
			if (c.matches(".*\\D.*"))
			{
				if (prev.matches(".*\\d.*") && partCount > 2)
					throwException("Plaque is invalid. Plaque: " + plaque);
				if (prev.matches(".*\\d.*") && partCount == 1)
				{
					partCount++;
					sb.append("-").append(c.toUpperCase(locale));
				}
				else
					sb.append(c.toUpperCase(locale));
			}
			else
			{
				if (prev.matches(".*\\D.*") && partCount == 2)
				{
					partCount++;
					sb.append("-").append(c);
				}
				else
					sb.append(c);
			}
			prev = c;
		}
		if (partCount != 3)
			throwException("Plaque is invalid. Plaque: " + plaque);

		return sb.toString();
	}

	private static void throwException(String msg) throws RestControllerException
	{
		throw new RestControllerException(msg);
	}
}
