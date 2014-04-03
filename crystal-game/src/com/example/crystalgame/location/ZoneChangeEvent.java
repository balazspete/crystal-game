package com.example.crystalgame.location;

import java.io.Serializable;

public class ZoneChangeEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5688897998471694705L;

	public static enum LocationState implements Serializable {
		IN, OUT
	}
	
	public static enum ZoneType implements Serializable {
		GAME_BOUNDARY, GAME_LOCATION
	}
	
	private LocationState locationState;
	private ZoneType zoneType;
	
	public ZoneChangeEvent() {
		
	}

	public LocationState getLocationState() {
		return locationState;
	}

	public void setLocationState(LocationState locationState) {
		this.locationState = locationState;
	}

	public ZoneType getZoneType() {
		return zoneType;
	}

	public void setZoneType(ZoneType zoneType) {
		this.zoneType = zoneType;
	}

}
