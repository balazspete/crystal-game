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
	
	protected PlayerType playerType;
	protected CharacterType characterType;
	protected String clientId;
	protected String name;

	private List<Crystal> crystalList;
	private List<MagicalItem> magicalItemList;
	
	private double energy = 10;
	private String energyLevel = null;
	
	/**
	 * Create a Character
	 * @param latitude The latitude
	 * @param longitude The longitude
	 * @param type the character's type
	 */
	protected Character(double latitude, double longitude, CharacterType characterType, PlayerType playerType, String clientId) {
		super(ArtifactType.CHARACTER, latitude, longitude, RADIUS);
		this.playerType = playerType;
		this.characterType = characterType;
		this.clientId = clientId;
		
		this.crystalList = new ArrayList<Crystal>();
		this.magicalItemList = new ArrayList<MagicalItem>();
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

	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Add a crystal to the possession of the character
	 * @param crystal A crystal object
	 * @return true if addition of crystal is successful
	 */
	public synchronized boolean addCrystal(Crystal crystal) {
		if(null != crystal) {
			return this.crystalList.add(crystal);
		}
		
		return false;
	}
	
	public boolean removeOneCrystal() {
		return crystalList.remove(0) != null;
	}
	
	/**
	 * Retrieves the list of crystals available with the character
	 * @return List of crystals
	 */
	public synchronized List<Crystal> getCrystals() {
		return crystalList;
	}
	
	/**
	 * Add a crystal to the possession of the character
	 * @param crystal A crystal object
	 * @return true if addition of crystal is successful
	 */
	public synchronized boolean addMagicalItem(MagicalItem magicalItem) {
		if(null != magicalItem) {
			return this.magicalItemList.add(magicalItem);
		}
		
		return false;
	}
	
	/**
	 * Retrieves the list of crystals available with the character
	 * @return List of crystals
	 */
	public synchronized List<MagicalItem> getMagicalItems() {
		return magicalItemList;
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
	 * @return the playerType
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * @return the characterType
	 */
	public CharacterType getCharacterType() {
		return characterType;
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

		@Override
		public double getVisibilityRange() {
			return 0;
		}

		@Override
		public double getInteractionRange() {
			return 0;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
