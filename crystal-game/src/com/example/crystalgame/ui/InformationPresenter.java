package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;

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
	
	public MapInformation getGamePlayData(GamePlayState gamePlayState) {
		GameStateInformation gameStateInformation = GameManager.getInstance().getGameState();
		MapInformation mapInformation = null;
		
		// Formatting of information happens here based on the game play state
		if(gamePlayState == GamePlayState.WORLD_MAP) {
			//TODO: Process the information only for world map
		} else if(gamePlayState == GamePlayState.LOCAL_MAP) {
			//TODO: Process the information only for local map
			
			mapInformation = new LocalMapInformation(gameStateInformation);
		} else if(gamePlayState == GamePlayState.INFO_PANEL) {
			//TODO: Process the information only for information panel
			
		}
		return mapInformation;
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
	
	public synchronized void energyChangeCallBack(double energyLevel) {
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
	
}
