package com.example.crystalgame.ui;

import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.location.ZoneChangeEvent;

/**
 * This interface is to have a set of common methods
 * in all activities to manage information flow from 
 * the lower layers
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public interface UIControllerHelperInter {
	
	/* Invoked when zone change happens */
	public void zoneChanged(ZoneChangeEvent zoneChangeEvent);
	
	/* Invoked when energy levels are low */
	public void energyLow(EnergyEvent energyEvent);
	//public void updateGameEnergy(double noOfEnergyUnits);
	public  void energyChangeCallBack(String energyLevel);

	void updateGameMagicalItemInfo(int noOfMagicalItems);

	void updateGameCrystalInfo(int noOfCrystals);

	public void removeCrystalFromMap(Crystal item);

	public void removeMagicalItemFromMap(MagicalItem item);

	public void timeChangeCallback(String newTime);
}
