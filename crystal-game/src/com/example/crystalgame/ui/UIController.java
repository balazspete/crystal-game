package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;

import android.util.Log;


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
}
