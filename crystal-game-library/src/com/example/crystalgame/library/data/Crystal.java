package com.example.crystalgame.library.data;

/**
 * Defines a crystal
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class Crystal extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426488079048008002L;

	/**
	 * Create a crystal
	 * @param latitude The latitude
	 * @param longitude The longitude
	 */
	public Crystal(double latitude, double longitude) {
		super(latitude, longitude, ItemType.CRYSTAL);
	}

}
