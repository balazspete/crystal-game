package com.example.crystalgame.library.data;

import java.io.Serializable;

/**
 * Describes an item
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public abstract class Item extends Artifact {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807516195771430780L;
	
	public static final double RADIUS = 100;
	private String zoneID;
	
	/**
	 * Create an item
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	protected Item(ArtifactType type, double latitude, double longitude) {
		super(type, latitude, longitude, RADIUS);
	}

	public String getZoneId() {
		return zoneID;
	}
	
	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
	}
	
}
