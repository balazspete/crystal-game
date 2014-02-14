package com.example.crystalgame.library.data.states;

import com.example.crystalgame.library.data.Zone;

public class ZoneState extends State<Zone, Boolean> {

	public ZoneState(Zone subject, Boolean value) {
		super(StateType.ZONE_STATE, subject, value);
	}

}
