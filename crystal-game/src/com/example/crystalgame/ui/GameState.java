/**
 * 
 */
package com.example.crystalgame.ui;

import android.util.Log;

import com.example.crystalgame.library.events.ProximityEvent;

/**
 * Representation of the game state
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameState extends Thread {

	/**
	 * Variables that represent game state
	 */
	private double playerEnergyLevel = 0.0f;
	private String playerID = null;
	
	// Data would be updated in the local data store every 15 seconds
	private final int DATA_PERSISTENCE_FREQUENCY = 15 * 1000;
	
	private static GameState gameState = null;
	
	private GameState() {
		super("GameState");
	}
	
	/**
	 * Return a singleton instance of this class
	 * @return GameState
	 */
	public static GameState getInstance() {
		if(gameState == null) {
			gameState = new GameState();
		}
		return gameState;
	}

	/**
	 * Game State initialization
	 */
	public void initializeGameState() {
		this.start();
	}
	
	/**
	 * Raise an alert when the user is in the proximity of an item
	 * @param proximityEvent
	 */
	public void itemProximityAlert(ProximityEvent proximityEvent) {
		updateGameState(proximityEvent);
	}
	
	private void updateGameState(ProximityEvent proximityEvent) {
		//TODO : update game state information in local data store
		Log.d("GameState", "Proximity Detected...");
	}
	
	public GameStateInformation getGameState() {
		// TODO : Get the latest representation of GameState
		return new GameStateInformation();
	}
	
	/**
	 * Raise an alert when the user is in the proximity of a zone
	 * @param proximityEvent
	 */
	public void zoneProximityAlert(ProximityEvent proximityEvent) {
		updateGameState(proximityEvent);
	}
	
	/**
	 * Update energy level of the player
	 * @param energyLevel
	 */
	public void updateEnergyLevel(double energyLevel) {
		// Save the energy level
		playerEnergyLevel = energyLevel;
		Log.d("GameState", "Energy Level updated : "+energyLevel);
	}
	
	/**
	 * Return the energy level currently available for the user
	 * @return playerEnergyLevel
	 */
	public double getEnergyLevel()
	{
		return playerEnergyLevel;
	}
	
	/**
	 * Dump the current game state in data store
	 */
	public synchronized void saveGameStateInDataStore() {
		// TODO : Dump the current game state in data store
	}

	/**
	 * Thread to save game state to the data store periodically
	 */
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(DATA_PERSISTENCE_FREQUENCY);
				saveGameStateInDataStore();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
