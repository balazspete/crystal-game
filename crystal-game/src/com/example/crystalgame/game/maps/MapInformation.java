package com.example.crystalgame.game.maps;

import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.GameStateInformation;

public class MapInformation {

	protected GamePlayState type;
	
	public GameStateInformation gameStateInformation;
	
	public MapInformation(GamePlayState type, GameStateInformation gameStateInformation) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.gameStateInformation = gameStateInformation;
	}

}
