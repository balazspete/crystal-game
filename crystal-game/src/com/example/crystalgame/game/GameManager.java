/**
 * 
 */
package com.example.crystalgame.game;

import java.util.ArrayList;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.energy.EnergyManager;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.location.LocationManager;
import com.example.crystalgame.location.ZoneChangeEvent;
import com.example.crystalgame.ui.InformationPresenter;


/**
 * Game Manager
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
	
	/**
	 * Propagate energy change across different components
	 * @param energyLevel
	 */
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
		Character character = InventoryManager.getInstance().getCharacter();
		if(null != character) {
			return (ArrayList<MagicalItem>)character.getMagicalItems();
		}
		return null;
	}
	
	public synchronized com.example.crystalgame.library.data.Character getGameCharacter() {
		return InventoryManager.getInstance().getCharacter();
	}
	
	/**
	 * Get reference to the Application object
	 * @return Application object
	 */
	public CrystalGame getApplicationObj() {
		return InformationPresenter.getInstance().getApplicationObj();
	}
	
	public synchronized void crystalCaptureCallBack(int noOfCrystals) {
		InformationPresenter.getInstance().crystalCaptureCallBack(noOfCrystals);
	}
	
	public synchronized void magicalItemCaptureCallBack(int noOfMagicalItems) {
		InformationPresenter.getInstance().magicalItemCaptureCallBack(noOfMagicalItems);
	}
	
	public synchronized ThroneRoom getThroneRoom() {
		return InventoryManager.getInstance().getThroneRoom();
	}
}
