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
		MapInformation mapInformation = InformationPresenter.getInstance().getGamePlayData(gamePlayState);
		
		return mapInformation;
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
		currentActivity.zoneChanged(zoneChangeEvent);
	}
	
	public  void energyLowCallBack(EnergyEvent energyEvent)
	{
		currentActivity.energyLow(energyEvent);
		Log.d("UIController","energyLowCallBack() : "+energyEvent.toString());
	}
	
	public synchronized void energyChangeCallBack(double energyLevel) {
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
	
}
