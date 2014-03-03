/**
 * 
 */
package com.example.crystalgame.ui;

import android.util.Log;

import com.example.crystalgame.library.events.ProximityEvent;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameState {

	private static GameState gameState = null;
	
	private GameState() {
		
	}
	
	public static GameState getInstance() {
		if(gameState == null) {
			gameState = new GameState();
		}
		return gameState;
	}
	
	public void itemProximityAlert(ProximityEvent proximityEvent) {
		updateGameState(proximityEvent);
	}
	
	private void updateGameState(ProximityEvent proximityEvent) {
		//TODO : update game state information in local data store
		Log.d("GameState", "Proximity Detected...");
	}
	
	public GameStateInformation getGameState() {
		return new GameStateInformation();
	}
	
	public void zoneProximityAlert(ProximityEvent proximityEvent) {
		updateGameState(proximityEvent);
	}
}
