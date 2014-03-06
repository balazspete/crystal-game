/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.util.Log;

import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;


/**
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameManager {

	private static GameManager gameManagerInstance = null;
	
	private GameManager() {	}

	public static GameManager getInstance() {
		if(null == gameManagerInstance) {
			gameManagerInstance = new GameManager();
		}
		return gameManagerInstance;
	}

	public String getData() {
		return ("GameManager" + "From GetData function()..");
	}
	
	/**
	 * Method to start all the components that are part of Game Manager
	 */
	public void startComponents() {
		GameStateManager.getInstance().startComponents();
		//LocationManager.getInstance().startComponents();
		
		// Player has to be inside the game boundary before the following executes
		EnergyManager.getInstance().startEnergyManager();
	}
	
	public GameStateInformation getGameState() {
		return GameState.getInstance().getGameState();
	}
	
	public synchronized void saveGameBoundary(GameBoundary gameBoundary) {
		LocationManager.getInstance().saveGameBoundary(gameBoundary);
	}
	
	public synchronized void saveGameLocation(GameBoundary gameBoundary) {
		LocationManager.getInstance().saveGameLocation(gameBoundary);
	}
	
	public synchronized void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		InformationPresenter.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
	
	public synchronized void energyChangeCallBack(double energyLevel) {
		GameStateManager.getInstance().energyChangeCallBack(energyLevel);
		InformationPresenter.getInstance().energyChangeCallBack(energyLevel);
	}
	
	public synchronized void energyLowCallBack(EnergyEvent energyEvent)
	{
		InformationPresenter.getInstance().energyLowCallBack(energyEvent);
	}
	
	public ArrayList<Location> getGameBoundaryPoints()
	{
		return LocationManager.getInstance().getGameBoundaryPoints();
		
	}
	public synchronized ArrayList<Location> getGameLocationPoints()
	{
		return LocationManager.getInstance().getGameLocationPoints();
	}
	public synchronized ArrayList<MagicalItem> getMagicalItemInfoList()
	{
		return LocationManager.getInstance().getMagicalItemInfoList();
	}
}
