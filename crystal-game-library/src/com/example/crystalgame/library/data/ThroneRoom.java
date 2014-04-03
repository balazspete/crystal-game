package com.example.crystalgame.library.data;

/**
 * Describes a throne room
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class ThroneRoom extends Zone {

	private Location location;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6016443257484436243L;

	public ThroneRoom(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
	
}
