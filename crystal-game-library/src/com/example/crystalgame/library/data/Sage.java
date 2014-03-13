package com.example.crystalgame.library.data;

/**
 * Describes a sage
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class Sage extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -540232457389629765L;

	/**
	 * Create a sage
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType the player type
	 */
	public Sage(double latitude, double longitude, PlayerType playerType, String playerID) {
		super(latitude, longitude, CharacterType.SAGE, playerType, playerID);
	}

}
