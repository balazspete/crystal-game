/**
 * 
 */
package com.example.crystalgame.game;

import java.util.Date;

import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.location.ZoneChangeEvent;

import android.util.Log;

/**
 * GameAgent who controls the logic and various 
 * processes that happen related to the game
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class GameAgent extends Thread {

	private static GameAgent gameAgent = null;
	private final double ENERGY_NOTIFY_VALUE = 2.0;
	
	// Default value of energy level is 10 units
	private double lastEnergyLevel = 10.0;
	private EnergyEvent energyEvent = null;
	
	private GameAgent() {
		super("GameAgent");
	}
	
	/**
	 * Return a singleton instance of this class
	 * @return GameAgen
	 */
	public static GameAgent getInstance() {
		if(null == gameAgent) {
			gameAgent = new GameAgent();
		}
		
		return gameAgent;
	}
	
	/**
	 * Intialization
	 */
	public void initializeGameAgent() {
		this.start();
	}
	
	public synchronized void setZoneChangeEvent( ZoneChangeEvent zoneChangeEvent)
	{
		GameStateManager.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}

	/**
	 * Thread to keep track of the energy level as the player 
	 * moves around the game locations and otherwise
	 */
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				
				if(
						GameState.getInstance().getEnergyLevel() < ENERGY_NOTIFY_VALUE 
					&&	lastEnergyLevel > ENERGY_NOTIFY_VALUE)
				{
					Log.d("GameAgent", "Energy Low...");
					energyEvent = new EnergyEvent();
					energyEvent.setEnergyLevel(GameState.getInstance().getEnergyLevel());
					energyEvent.setEnergyLowTime(new Date());
					
					raiseEnergyLowEvent(energyEvent);
				}
				
				lastEnergyLevel = GameState.getInstance().getEnergyLevel();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Raise an event when the energy level becomes lower than the threshold
	 * @param energyEvent
	 */
	public synchronized void raiseEnergyLowEvent(EnergyEvent energyEvent) {
		GameStateManager.getInstance().energyLowCallBack(energyEvent);
	}
}
