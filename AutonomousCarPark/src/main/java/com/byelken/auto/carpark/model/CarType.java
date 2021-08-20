package com.byelken.auto.carpark.model;

/**
 * @author Berkay.Yelken
 */
public enum CarType {
	CAR {
		@Override
		public int getUnitVolume()
		{
			return 1;
		}
	},
	JEEP {
		@Override
		public int getUnitVolume()
		{
			return 2;
		}
	},
	TRUCK {
		@Override
		public int getUnitVolume()
		{
			return 4;
		}
	};

	public abstract int getUnitVolume();

}
