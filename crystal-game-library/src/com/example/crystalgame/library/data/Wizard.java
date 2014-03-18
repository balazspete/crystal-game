package com.example.crystalgame.library.data;

/**
 * Describes a character of class wizard
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class Wizard extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1221320162223472002L;

	private String originalID;
	
	/**
	 * Create a Wizard
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param type the player type
	 */
	public Wizard(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.WIZARD, character.playerType, character.getClientId());
		this.originalID = character.id;
	}

	@Override
	public String getID() {
		return originalID;
	}

	
	
	
}
