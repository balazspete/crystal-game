package com.example.crystalgame.ui;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.GameManager;
import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.maps.MapInformation;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.location.ZoneChangeEvent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;


/**
 * UI Controller for the game
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class UIController {

	private static UIController uiController = null;
	private UIControllerHelper currentActivity = null;
	
	private UIController() {
		
	}
	
	public static UIController getInstance() {
		if(null == uiController) {
			uiController = new UIController();
		}
		
		return uiController;
	}
	
	public synchronized void startComponents() {
		GameManager.getInstance().startComponents();
	}

	public synchronized void stopComponents() {
		GameManager.getInstance().stopComponents();
	}
	
	public synchronized UIControllerHelper getCurrentActivity() {
		return currentActivity;
	}

	public synchronized void setCurrentActivity(UIControllerHelper currentActivity) {
		this.currentActivity = currentActivity;
		System.out.println("Activity Reference : "+this.currentActivity);
	}
	
	public synchronized void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		System.out.println("Calling Activity : "+currentActivity);
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.zoneChanged(zoneChangeEvent);
	}
	
	public synchronized void energyLowCallBack(EnergyEvent energyEvent)
	{
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.lowEnergyWarning(energyEvent);
		Log.d("UIController","energyLowCallBack() : "+energyEvent.toString());
	}
	
	public synchronized void energyChangeCallBack(String energyLevel) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.updateGameEnergyInfo(energyLevel);
	}
	
	public synchronized ArrayList<Location> getGameBoundaryPoints()
	{
		return InformationPresenter.getInstance().getGameBoundaryPoints();
		
	}
	
	public synchronized ArrayList<Location> getGameLocationPoints()
	{
		return InformationPresenter.getInstance().getGameLocationPoints();
	}
	
	public synchronized  ArrayList<MagicalItem> getMagicalItemInfoList()
	{
		
		return InformationPresenter.getInstance().getMagicalItemInfoList();
	}

	/**
	 * Current game character
	 * @return Character
	 */
	public synchronized com.example.crystalgame.library.data.Character getGameCharacter() {
		return InformationPresenter.getInstance().getGameCharacter();
	}

	/**
	 * The ThroneRoom
	 * @return ThroneRoom
	 */
	public synchronized ThroneRoom getThroneRoom() {
		return InformationPresenter.getInstance().getThroneRoom();
	}
	
	/**
	 * Update number of crystals
	 * @param noOfCrystals
	 */
	public synchronized void crystalCaptureCallBack(int noOfCrystals) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.updateGameCrystalInfo(noOfCrystals);
	}
	
	/**
	 * Update number of magical items
	 * @param noOfMagicalItems
	 */
	public synchronized void magicalItemCaptureCallBack(int noOfMagicalItems) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.updateGameMagicalItemInfo(noOfMagicalItems);
	}
	
	public synchronized void removeCrystalFromMap(Crystal item) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.removeCrystalFromMap(item);
	}
	

	public synchronized void removeMagicalItemFromMap(MagicalItem item) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.removeMagicalItemFromMap(item);
	}
	
	/**
	 * Get list of all characters
	 * @return Array of characters
	 */
	public synchronized com.example.crystalgame.library.data.Character[] getGameCharacters() {
		return InformationPresenter.getInstance().getCharacters();
	}
	
	/**
	 * Get the list of all magical items
	 * @return Array of Magical Items
	 */
	public synchronized MagicalItem[] getGameMagicalItems() {
		return InformationPresenter.getInstance().getMagicalItems();
	}
	
	/**
	 * Get the list of all Crystals
	 * @return Array of Crystals
	 */
	public synchronized Crystal[] getGameCrystals() {
		return InformationPresenter.getInstance().getGameCrystals();
	}
	
	public synchronized CrystalZone[] getCrystalZones(){
		return InformationPresenter.getInstance().getCrystalZones();
	}

	public void timeChangeCallback(String newTime) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.timeChangeCallback(newTime);
	}
	
	public GameLocation getGameLocation() {
		List<HasID> locs;
		try {
			locs = ClientDataWarehouse.getInstance().getList(GameLocation.class);
			if (locs != null && locs.size() > 0) {
				return (GameLocation) locs.get(0);
			}
		} catch (DataWarehouseException e) {
			// returning null
		}

		return null;
	}
	
}
