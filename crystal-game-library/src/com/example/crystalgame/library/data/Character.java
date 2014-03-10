package com.example.crystalgame.library.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Character
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public abstract class Character extends Artifact {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6302171104196819575L;
	private List<Crystal> crystalList = new ArrayList<Crystal>();

	/**
	 * Describes the type of the player
	 * @author 
	 *
	 */
	public enum PlayerType {
		PLAYER, NPC
	}
	
	/**
	 * Describes the character's class
	 * @author 
	 *
	 */
	public enum CharacterType {
		WARRIOR, SAGE, WIZARD
	}
	
	public final static double RADIUS = 100;
	
	public final PlayerType playerType;
	public final CharacterType characterType;
	
	/**
	 * Create a Character
	 * @param latitude The latitude
	 * @param longitude The longitude
	 * @param type the character's type
	 */
	protected Character(double latitude, double longitude, CharacterType characterType, PlayerType playerType) {
		super(latitude, longitude, RADIUS);
		this.playerType = playerType;
		this.characterType = characterType;
	}

	/**
	 * Add a crystal to the possession of the character
	 * @param crystal A crystal object
	 * @return true if addition of crystal is successful
	 */
	public boolean addCrystal(Crystal crystal) {
		if(null != crystal) {
			return this.crystalList.add(crystal);
		}
		
		return false;
	}
	
	/**
	 * Retrieves the list of crystals available with the character
	 * @return List of crystals
	 */
	public List<Crystal> getCrystals() {
		return this.crystalList;
	}
}
