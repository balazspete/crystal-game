package com.example.crystalgame.ui;


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
	}
	
	public void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		currentActivity.zoneChanged(zoneChangeEvent);
	}
}
