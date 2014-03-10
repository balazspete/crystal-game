package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;

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
}
