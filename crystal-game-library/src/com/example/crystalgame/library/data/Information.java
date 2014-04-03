package com.example.crystalgame.library.data;

import java.io.Serializable;

/**
 * An object representing some general information
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class Information implements HasID, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3903033647887846667L;

	private static final String PREKEY = "com.example.crystalgame.library.data.information.";
	
	private String key;
	private Serializable value;
	
	/**
	 * Create an information
	 * @param key The key
	 * @param value The value
	 */
	public Information(String key, Serializable value) {
		this.key = PREKEY + key;
		this.value = value;
	}
	
	@Override
	public String getID() {
		return key;
	}

	/**
	 * Get the value of the information
	 * @return The information
	 */
	public Serializable getValue() {
		return value;
	}
	
	/*******************
	 * PREDEFINED KEYS
	 *******************/
	
	public static final String
		GAME_NAME = "game_name",
		GROUP_NAME = "group_name",
		GROUP_MAX_PLAYERS = "group_max_players";
	
	
}
