package com.example.crystalgame.library.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Character
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
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
	public enum PlayerType implements Serializable {
		PLAYER, NPC
	}
	
	/**
	 * Describes the character's class
	 * @author 
	 *
	 */
	public enum CharacterType implements Serializable {
		WARRIOR, SAGE, WIZARD, UNKNOWN
	}
	
	public final static double RADIUS = 100;
	
	public final PlayerType playerType;
	public final CharacterType characterType;
	public final String clientId;

	private List<Crystal> crystalList = new ArrayList<Crystal>();
	private List<MagicalItem> magicalItemList = new ArrayList<MagicalItem>();
	
	private double energy = 10;
	private String energyLevel = null;
	
	/**
	 * Create a Character
	 * @param latitude The latitude
	 * @param longitude The longitude
	 * @param type the character's type
	 */
	protected Character(double latitude, double longitude, CharacterType characterType, PlayerType playerType, String clientId) {
		super(latitude, longitude, RADIUS);
		this.playerType = playerType;
		this.characterType = characterType;
		this.clientId = clientId;
	}

	/**
	 * 
	 * @return
	 */
	public double getEnergy() {
		return energy;
	}

	/**
	 * 
	 * @param energy
	 */
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	@Override
	public String getID() 
	{
		return id;
	}

	public String getClientId() {
		return clientId;
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
	
	/**
	 * Add a crystal to the possession of the character
	 * @param crystal A crystal object
	 * @return true if addition of crystal is successful
	 */
	public boolean addMagicalItem(MagicalItem magicalItem) {
		if(null != magicalItem) {
			return this.magicalItemList.add(magicalItem);
		}
		
		return false;
	}
	
	/**
	 * Retrieves the list of crystals available with the character
	 * @return List of crystals
	 */
	public List<MagicalItem> getMagicalItems() {
		return this.magicalItemList;
	}
	
	/**
	 * Set energy level of the player
	 * @param energy level of the player
	 */
	public void setEnergyLevel(String energyLevel) {
		this.energyLevel = energyLevel;
	}
	
	/**
	 * Get the energy level of the player
	 * @return energy level of the player
	 */
	public String getEnergyLevel() {
		return this.energyLevel;
	}
	
	/**
	 * A character representation for a client until more information is known
	 * @author Balazs Pete, Allen Thomas Varghese
	 *
	 */
	public static class UnknownPlayerCharacter extends Character {
		public UnknownPlayerCharacter(String clientId) {
			super(0, 0, CharacterType.UNKNOWN, PlayerType.PLAYER, clientId);
		}
	}
}
