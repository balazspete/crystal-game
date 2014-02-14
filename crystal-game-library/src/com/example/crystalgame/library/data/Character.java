package com.example.crystalgame.library.data;

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
	
}
