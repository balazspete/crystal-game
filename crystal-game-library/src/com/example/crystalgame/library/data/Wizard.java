package com.example.crystalgame.library.data;

/**
 * Describes a character of class wizard
 * @author 
 *
 */
public class Wizard extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1221320162223472002L;

	/**
	 * Create a Wizard
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param type the player type
	 */
	public Wizard(double latitude, double longitude, PlayerType type) {
		super(latitude, longitude, CharacterType.WIZARD, type);
	}

}
