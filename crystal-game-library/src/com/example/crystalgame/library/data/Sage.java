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
	
	private final String originalID;

	/**
	 * Create a sage
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType the player type
	 */
	public Sage(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.SAGE, character.playerType, character.getClientId());
		this.originalID = character.id;
	}

	@Override
	public String getID() {
		return originalID;
	}
	
}
