package com.example.crystalgame.ui;

/**
 * 
 *  @author Allen Thomas Varghese, Rajan Verma
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
}
