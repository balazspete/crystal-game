package com.example.crystalgame.library.data;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

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
	
	private String originalID;

	public Warrior() {
		super(0, 0, CharacterType.WARRIOR, null, null);
	}
	
	/**
	 * Create a warrior
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType The player type
	 */
	protected Warrior(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.WARRIOR, character.playerType, character.getClientId());
		this.originalID = character.id;
	}

	@Override
	public String getID() {
		return originalID;
	}

	public static Warrior create(Character character, double latitude, double longitude) {
		Warrior w = new Warrior(character, latitude, longitude);
		w.playerType = PlayerType.PLAYER;
		w.setLatitude(latitude);
		w.setLongitude(longitude);
		return w;
	}
	
}
