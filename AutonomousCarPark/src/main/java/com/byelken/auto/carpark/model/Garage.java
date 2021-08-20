package com.byelken.auto.carpark.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Berkay.Yelken
 */

@Entity(name = "Garage")
@Table(name = "GARAGE")
public class Garage implements Comparable<Garage>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAR_ID")
	private int cId;

	@Column(name = "CAR_PLAQUE", unique = true)
	private String plaque;

	@Column(name = "PARK_SLOT")
	private int parkSlot;

	@Column(name = "CAR_TYPE")
	@Enumerated(EnumType.STRING)
	private CarType carType;

	@Column(name = "CAR_COLOR")
	private String color;

	public int getId()
	{
		return cId;
	}

	public void setCId(int id)
	{
		this.cId = id;
	}

	public String getPlaque()
	{
		return plaque;
	}

	public void setPlaque(String plaque)
	{
		this.plaque = plaque;
	}

	public int getParkSlot()
	{
		return parkSlot;
	}

	public void setParkSlot(int parkSlot)
	{
		this.parkSlot = parkSlot;
	}

	public CarType getCarType()
	{
		return carType;
	}

	public void setCarType(CarType carType)
	{
		this.carType = carType;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	@Override
	public int compareTo(Garage o)
	{
		return Integer.valueOf(getParkSlot()).compareTo(o.getParkSlot());
	}

}
