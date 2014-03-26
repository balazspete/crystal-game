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
	
	public enum ItemType implements Serializable {
		CRYSTAL, MAGICAL_ITEM
	}
	
	public static final double RADIUS = 100;
	public final ItemType type;
	private String zoneID;
	
	/**
	 * Create an item
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	protected Item(double latitude, double longitude, ItemType type) {
		super(latitude, longitude, RADIUS);
		this.type = type;
	}

	public String getZoneId() {
		return zoneID;
	}
	
	public void setZoneID(String zoneID) {
		this.zoneID = zoneID;
	}
	
	public ItemType getType() {
		return type;
	}
	
}
