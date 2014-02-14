package com.example.crystalgame.library.data;

/**
 * Describes a warrior
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class Warrior extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3721553326307227208L;

	/**
	 * Create a warrior
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType The player type
	 */
	public Warrior(double latitude, double longitude, PlayerType playerType) {
		super(latitude, longitude, CharacterType.WARRIOR, playerType);
	}

}
