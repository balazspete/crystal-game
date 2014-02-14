package com.example.crystalgame.library.data;

/**
 * Defines a magical item
 * @author 
 *
 */
public class MagicalItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740669597817985828L;

	/**
	 * Create a magical item
	 * @param latitude the latitude 
	 * @param longitude the longitude
	 */
	public MagicalItem(double latitude, double longitude) {
		super(latitude, longitude, ItemType.MAGICAL_ITEM);
	}

}
