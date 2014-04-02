package com.example.crystalgame.library.data;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

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
	
	public Wizard() {
		super(0, 0, CharacterType.WIZARD, null, null);
	}
	
	/**
	 * Create a Wizard
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param type the player type
	 */
	private Wizard(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.WIZARD, character.playerType, character.getClientId());
	}

	public static Wizard create(Character character, double latitude, double longitude) {
		Wizard w = new Wizard(character, latitude, longitude);
		w.playerType = PlayerType.PLAYER;
		w.setLatitude(latitude);
		w.setLongitude(longitude);
		w.setEnergy(10);
		return w;
	}

	@Override
	public double getVisibilityRange() {
		return 10;
	}

	@Override
	public double getInteractionRange() {
		return 5;
	}
	
}
