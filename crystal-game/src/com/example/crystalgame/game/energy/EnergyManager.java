package com.example.crystalgame.game.energy;

import java.text.DecimalFormat;
import java.util.Date;

import com.example.crystalgame.game.GameManager;
import com.example.crystalgame.game.GameState;
import com.example.crystalgame.game.GameStateManager;
import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.location.LocationManager;

import android.util.Log;

/**
 * Energy Manager to keep track of energy units available
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class EnergyManager extends Thread {

	private static EnergyManager energyManager		= null						;
	private EnergyEvent energyEvent 				= null						;
	
	private final double OUT_LOCATION_MULTIPLIER 	= 1/300						;
	private final double IN_LOCATION_MULTIPLIER 	= 2/300						;
	private final double INITIAL_ENERGY_LEVEL  		= 10.00						;
	private final double ENERGY_NOTIFY_VALUE 		= 2.0						;
	private double energyRemaining 					= INITIAL_ENERGY_LEVEL		;
	private double inLocationTime 					= 0.0						;
	private double outLocationTime 					= 0.0						;
	
	private final int CRYSTAL_ENERGY_GAIN 			= 2							;
	private final int ENERGY_TRACKING_FREQUENCY 	= 3000						;
	
	private boolean inLocation;
	
	
	private EnergyManager() {
		super("EnergyManager");
	}
	
	public static EnergyManager getInstance() {
		if(null == energyManager) {
			energyManager = new EnergyManager();
		}
		
		return energyManager;
	}

	public void startEnergyManager() {
		this.start();
	}
	
	@Override
	public void run() {
		Log.d("EnergyManager", "Thread started");
		while(true)
		{
			try {
				Thread.sleep(ENERGY_TRACKING_FREQUENCY);
				
				inLocation = LocationManager.getInstance().isGameLocation();
				
				// Tracking is done in seconds. Do divide by 1000 to convert from milliseconds to seconds
				if(inLocation) {
					inLocationTime += ENERGY_TRACKING_FREQUENCY/1000;
				} else {
					outLocationTime += ENERGY_TRACKING_FREQUENCY/1000;
				}
				
				energyRemaining -= inLocationTime * IN_LOCATION_MULTIPLIER + outLocationTime * OUT_LOCATION_MULTIPLIER;
				
				if(energyRemaining <= ENERGY_NOTIFY_VALUE) {
					Log.d("GameAgent", "Energy Low...");
					energyEvent = new EnergyEvent();
					energyEvent.setEnergyLowTime(new Date());
					
					raiseEnergyLowEvent(energyEvent);
				}
				
				// Update the inventory manager with energy level of the game player
				InventoryManager.getInstance().setEnergyLevel(getEnergyLevel());
				
				// Pushing the new energy level to Game Manager
				GameManager.getInstance().energyChangeCallBack(energyRemaining);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gain two energy units when capturing a crystal
	 */
	public synchronized void gainEnergy() {
		energyRemaining += CRYSTAL_ENERGY_GAIN;
	}
	
	/**
	 * Gives the remaining energy units formatted to 2 decimal places
	 * 
	 * @return A string
	 */
	public String getEnergyLevel() {
		return new DecimalFormat("#.00").format(energyRemaining);
	}

	/**
	 * Gives the actual value of energy units for saving in data store
	 * 
	 * @return double value of energy remaining
	 */
	public double getEnergyLevelValue() {
		return energyRemaining;
	}

	public void setInLocation(boolean inLocation) {
		this.inLocation = inLocation;
	}
	
	/**
	 * Raise an event when the energy level becomes lower than the threshold
	 * @param energyEvent
	 */
	public synchronized void raiseEnergyLowEvent(EnergyEvent energyEvent) {
		GameStateManager.getInstance().energyLowCallBack(energyEvent);
	}
	
}
