package com.example.crystalgame.game.energy;

import java.text.DecimalFormat;
import java.util.Date;

import android.util.Log;

import com.example.crystalgame.game.GameEndActivity.GameEndType;
import com.example.crystalgame.game.GameManager;
import com.example.crystalgame.game.GameStateManager;
import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.location.LocationManager;

/**
 * Energy Manager to keep track of energy units available
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class EnergyManager extends Thread {

	private static EnergyManager energyManager		= null						;
	private EnergyEvent energyEvent 				= null						;
	
	private final double OUT_LOCATION_MULTIPLIER 	= 1.0/300					;
	private final double IN_LOCATION_MULTIPLIER 	= 2.0/300					;
	private final double INITIAL_ENERGY_LEVEL  		= 10.00						;
	private final double ENERGY_NOTIFY_VALUE 		= 2.0						;
	private double energyRemaining 					= INITIAL_ENERGY_LEVEL		;
	
	// 2 energy units gained when capturing a crystal 
	private final int CRYSTAL_ENERGY_GAIN 			= 2							;

	// Energy levels are tracked every 3 seconds 
	private final int ENERGY_TRACKING_FREQUENCY 	= 3000						;
	
	private boolean inLocation;
	private boolean isRunning;
	
	
	// Private Constructor for internal invocation
	private EnergyManager() {
		super("EnergyManager");
	}
	
	public static EnergyManager getInstance() {
		if(null == energyManager) {
			energyManager = new EnergyManager();
		}
		
		return energyManager;
	}

	/**
	 * Initiate the tracking of energy as a thread in the background
	 */
	public synchronized void startEnergyManager() {
		// Checking if the thread has already started
		if(!this.isAlive()) {
			this.start();
		}
	}
	
	/**
	 * Stop the energy manager thread
	 */
	public synchronized void stopEnergyManager() {
		if(this.isAlive()) {
			this.isRunning = false;
		}
	}
	
	@Override
	public synchronized void run() {
		Log.d("EnergyManager", "Energy Tracking thread started...");
		this.isRunning = true;
		while(isRunning)
		{
			try {				
				inLocation = LocationManager.getInstance().isGameLocation();
				
				// Tracking is done in seconds. Do divide by 1000 to convert from milliseconds to seconds
				if(inLocation) {
					energyRemaining -= ((ENERGY_TRACKING_FREQUENCY/1000) * IN_LOCATION_MULTIPLIER);
				} else {
					energyRemaining -= ((ENERGY_TRACKING_FREQUENCY/1000) * OUT_LOCATION_MULTIPLIER);
				}
				
				if(energyRemaining <= ENERGY_NOTIFY_VALUE) {
					Log.d("EnergyManager", "Energy Low...");
					energyEvent = new EnergyEvent();
					energyEvent.setEnergyLowTime(new Date());
					
					raiseEnergyLowEvent(energyEvent);
				}
				
				// Pushing the new energy level to Game Manager
				GameManager.getInstance().energyChangeCallBack(getEnergyLevel());
				
				// Updating the energy level of character
				InventoryManager.getInstance().setEnergyLevel(getEnergyLevel());
				
				// No more energy left, end the game
				if(energyRemaining <= 0.00 && null != GameManager.getInstance().getApplicationObj()) {
					// Invoke the end of game screen
					GameManager.getInstance().endGame(GameEndType.OUT_OF_ENERGY);
					
					// End the thread
					isRunning = false;
				}
				
				Thread.sleep(ENERGY_TRACKING_FREQUENCY);
			} catch (InterruptedException e) {
				Log.e("EnergyManager:run()", "Energy Tracking Thread is interrupted...");
				this.isRunning = false;
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
	public synchronized String getEnergyLevel() {
		return new DecimalFormat("#.00").format(energyRemaining);
	}

	/**
	 * Raise an event when the energy level becomes lower than the threshold
	 * @param energyEvent
	 */
	public synchronized void raiseEnergyLowEvent(EnergyEvent energyEvent) {
		GameStateManager.getInstance().energyLowCallBack(energyEvent);
	}
	
}
