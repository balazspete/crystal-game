package com.example.crystalgame.ui;

import java.text.DecimalFormat;

import android.util.Log;

/**
 * Energy Manager to keep track of energy units available
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class EnergyManager extends Thread {

	private static EnergyManager energyManager = null;
	private final double OUT_LOCATION_MULTIPLIER = 1/300;
	private final double IN_LOCATION_MULTIPLIER = 2/300;
	private final double INITIAL_ENERGY_LEVEL  = 1.5;
	private final int CRYSTAL_ENERGY_GAIN = 2;
	private double energyRemaining = INITIAL_ENERGY_LEVEL;
	private double inLocationTime = 0.0;
	private double outLocationTime = 0.0;
	
	private boolean inLocation;
	private boolean outsideBoundary = false;
	
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
				outsideBoundary = LocationManager.getInstance().isGameBoundary();
				
//				if(!outsideBoundary) {
//					energyRemaining = 0.0;
//					continue;
//				}
				
				Thread.sleep(1000);
				
				inLocation = LocationManager.getInstance().isGameLocation();
				
				if(inLocation) {
					inLocationTime++;
				} else {
					outLocationTime++;
				}
				
				energyRemaining -= inLocationTime * IN_LOCATION_MULTIPLIER + outLocationTime * OUT_LOCATION_MULTIPLIER;
				
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


	public void setOutsideBoundary(boolean outsideBoundary) {
		this.outsideBoundary = outsideBoundary;
	}
	
}
