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
	
	private String originalID;

	public Sage() {
		super(0, 0, CharacterType.SAGE, null, null);
	}
	
	/**
	 * Create a sage
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param playerType the player type
	 */
	protected Sage(Character character, double latitude, double longitude) {
		super(latitude, longitude, CharacterType.SAGE, character.playerType, character.getClientId());
		this.originalID = character.id;
	}

	@Override
	public String getID() {
		return originalID;
	}
	
	public static Sage create(Character character, double latitude, double longitude) {
		Sage s = new Sage();
		s.playerType = PlayerType.PLAYER;
		s.setLatitude(latitude);
		s.setLongitude(longitude);
		s.setEnergy(10);
		return s;
	}
	
}
