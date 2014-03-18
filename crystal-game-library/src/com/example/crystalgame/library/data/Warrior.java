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
	
	//private String originalID;

	/**
	 * Create a warrior
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType The player type
	 */
	public Warrior(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.WARRIOR, character.playerType, character.getClientId());
		//this.originalID = character.id;
	}
//
//	@Override
//	public String getID() {
//		return originalID;
//	}

}
