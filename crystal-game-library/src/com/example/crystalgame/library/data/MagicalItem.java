package com.example.crystalgame.library.data;

/**
 * Defines a magical item
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class MagicalItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740669597817985828L;
	private String zoneID;
	/**
	 * Create a magical item
	 * @param latitude the latitude 
	 * @param longitude the longitude
	 */
	public MagicalItem(double latitude, double longitude) {
		super(latitude, longitude, ItemType.MAGICAL_ITEM);
	}
	
	public void setZoneID(String zoneID)
	{
		this.zoneID = zoneID;
	}

}
