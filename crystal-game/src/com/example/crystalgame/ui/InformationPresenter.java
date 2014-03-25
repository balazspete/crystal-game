package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.game.GameManager;
import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.GameStateInformation;
import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.maps.LocalMapInformation;
import com.example.crystalgame.game.maps.MapInformation;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.location.ZoneChangeEvent;

/**
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class InformationPresenter {

	private static InformationPresenter informationPresenter = null;
	
	private InformationPresenter() {
	}
	
	public static InformationPresenter getInstance() {
		if(null == informationPresenter) {
			informationPresenter = new InformationPresenter();
		}
		
		return informationPresenter;
	}
	
	public void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		UIController.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
	
	public  void energyLowCallBack(EnergyEvent energyEvent)
	{
		UIController.getInstance().energyLowCallBack(energyEvent);
		Log.d("InformationPresenter","energyLowCallBack() : "+energyEvent.toString());
	}
	
	public synchronized void energyChangeCallBack(String energyLevel) {
		UIController.getInstance().energyChangeCallBack(energyLevel);
	}
	
	public ArrayList<Location> getGameBoundaryPoints()
	{
		return GameManager.getInstance().getGameBoundaryPoints();
	}
	
	public ArrayList<Location> getGameLocationPoints()
	{
		return GameManager.getInstance().getGameLocationPoints();
	}
	
	public synchronized ArrayList<MagicalItem> getMagicalItemInfoList()
	{
		
		return GameManager.getInstance().getMagicalItemInfoList();
	}
	
	/**
	 * Current Game Character
	 * @return Character
	 */
	public synchronized com.example.crystalgame.library.data.Character getGameCharacter() {
		return GameManager.getInstance().getGameCharacter();
	}
	
	/**
	 * The ThroneRoom
	 * @return ThroneRoom
	 */
	public synchronized ThroneRoom getThroneRoom() {
		return GameManager.getInstance().getThroneRoom();
	}
	
	/**
	 * Get reference to the Application object
	 * @return Application object
	 */
	public CrystalGame getApplicationObj() {
		Activity activity = (Activity)UIController.getInstance().getCurrentActivity();
		if(null != activity) {
			return (CrystalGame)activity.getApplication();
		}
		return null;
	}
	
	/**
	 * Update number of crystals
	 * @param noOfCrystals
	 */
	public synchronized void crystalCaptureCallBack(int noOfCrystals) {
		UIController.getInstance().crystalCaptureCallBack(noOfCrystals);
	}
	
	/**
	 * Update number of magical items
	 * @param noOfMagicalItems
	 */
	public synchronized void magicalItemCaptureCallBack(int noOfMagicalItems) {
		UIController.getInstance().magicalItemCaptureCallBack(noOfMagicalItems);
	}
	
	public synchronized com.example.crystalgame.library.data.Character[] getCharacters() {
		return InventoryManager.getInstance().getCharacters();
	}
	
	public synchronized MagicalItem[] getMagicalItems() {
		return InventoryManager.getInstance().getMagicalItems();
	}
	
	public synchronized Crystal[] getGameCrystals() {
		return InventoryManager.getInstance().getCrystals();
	}
}
