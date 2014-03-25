package com.example.crystalgame.game.maps;

import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.GameStateInformation;

/**
 * Stores map information
 * @author Allen Thomas Varghese
 */
public class MapInformation {

	protected GamePlayState type;
	
	public GameStateInformation gameStateInformation;
	
	public MapInformation(GamePlayState type, GameStateInformation gameStateInformation) {
		this.type = type;
		this.gameStateInformation = gameStateInformation;
	}

}
