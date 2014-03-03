/**
 * 
 */
package com.example.crystalgame.ui;

import com.example.crystalgame.library.data.GameBoundary;


/**
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
		LocationManager.getInstance().startComponents();
	}
	
	public GameStateInformation getGameState() {
		return GameState.getInstance().getGameState();
	}
	
	public void saveGameBoundary(GameBoundary gameBoundary) {
		LocationManager.getInstance().saveGameBoundary(gameBoundary);
	}
	
	public void saveGameLocation(GameBoundary gameBoundary) {
		LocationManager.getInstance().saveGameLocation(gameBoundary);
	}
	
	public void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		InformationPresenter.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
}
