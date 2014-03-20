package com.example.crystalgame.ui;

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
	public  void energyChangeCallBack(double energyLevel);

	void updateGameMagicalItemInfo(int noOfMagicalItems);

	void updateGameCrystalInfo(int noOfCrystals);
}
