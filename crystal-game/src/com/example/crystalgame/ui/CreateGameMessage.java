/**
 * POJO class for saving the game creation information 
 * @author Chen Shen, Allen Thomas Varghese
 */
package com.example.crystalgame.ui;

import java.io.Serializable;

import com.example.crystalgame.library.data.Zone;

public class CreateGameMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6848851065944952762L;
	private String playerID;
	private String groupID;
	private Zone gameLocation;
	private String gameStartTime;
	private String gameDuration;
	
	public String getPlayerID() {
		return playerID;
	}
	
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	
	public String getGroupID() {
		return groupID;
	}
	
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	public Zone getGameLocation() {
		return gameLocation;
	}
	
	public void setGameLocation(Zone gameLocation) {
		this.gameLocation = gameLocation;
	}
	
	public String getGameStartTime() {
		return gameStartTime;
	}
	
	public void setGameStartTime(String gameStartTime) {
		this.gameStartTime = gameStartTime;
	}
	
	public String getGameDuration() {
		return gameDuration;
	}
	
	public void setGameDuration(String gameDuration) {
		this.gameDuration = gameDuration;
	}
	
}
