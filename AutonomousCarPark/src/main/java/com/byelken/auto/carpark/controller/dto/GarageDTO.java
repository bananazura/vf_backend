package com.byelken.auto.carpark.controller.dto;

/**
 * @author Berkay.Yelken
 */
public class GarageDTO
{
	private int cId;

	private String plaque;

	private String parkSlot;

	private String carType;

	private String color;

	public int getcId()
	{
		return cId;
	}

	public void setcId(int cId)
	{
		this.cId = cId;
	}

	public String getPlaque()
	{
		return plaque;
	}

	public void setPlaque(String plaque)
	{
		this.plaque = plaque;
	}

	public String getParkSlot()
	{
		return parkSlot;
	}

	public void setParkSlot(String parkSlot)
	{
		this.parkSlot = parkSlot;
	}

	public String getCarType()
	{
		return carType;
	}

	public void setCarType(String carType)
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

}
