package com.example.crystalgame.library.data.states;

import com.example.crystalgame.library.data.Location;

public class LocationState extends State<Location, LocationStateValue> {

	private LocationState(Location subject, LocationStateValue value) {
		super(StateType.LOCATION_STATE, subject, value);
	}

	public static LocationState getInRangeState(Location location) {
		return new LocationState(location, LocationStateValue.IN_RANGE);
	}
	
	public static LocationState getOutOfRangeState(Location location) {
		return new LocationState(location, LocationStateValue.OUT_OF_RANGE);
	}
	
}
