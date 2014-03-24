package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.util.Log;

import com.example.crystalgame.game.GameManager;
import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.maps.MapInformation;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.location.ZoneChangeEvent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;


/**
 * UI Controller for the game
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class UIController {

	private static UIController uiController = null;
	private UIControllerHelperInter currentActivity = null;
	
	private UIController() {
		
	}
	
	public static UIController getInstance() {
		if(null == uiController) {
			uiController = new UIController();
		}
		
		return uiController;
	}
	
	public MapInformation loadGamePlayData(GamePlayState gamePlayState) {
		return InformationPresenter.getInstance().getGamePlayData(gamePlayState);
	}
	
	public void startComponents() {
		GameManager.getInstance().startComponents();
	}

	public UIControllerHelperInter getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(UIControllerHelperInter currentActivity) {
		this.currentActivity = currentActivity;
		System.out.println("Activity Reference : "+this.currentActivity);
	}
	
	public void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		System.out.println("Calling Activity : "+currentActivity);
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.zoneChanged(zoneChangeEvent);
	}
	
	public void energyLowCallBack(EnergyEvent energyEvent)
	{
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.energyLow(energyEvent);
		Log.d("UIController","energyLowCallBack() : "+energyEvent.toString());
	}
	
	public synchronized void energyChangeCallBack(double energyLevel) {
		if (currentActivity == null) {
			return;
		}
		
		currentActivity.energyChangeCallBack(energyLevel);
	}
	
	public ArrayList<Location> getGameBoundaryPoints()
	{
		return InformationPresenter.getInstance().getGameBoundaryPoints();
		
	}
	
	public ArrayList<Location> getGameLocationPoints()
	{
		return InformationPresenter.getInstance().getGameLocationPoints();
	}
	
	public  ArrayList<MagicalItem> getMagicalItemInfoList()
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
	
}
