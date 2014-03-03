/**
 * 
 */
package com.example.crystalgame.ui;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameAgent {

	private static GameAgent gameAgent = null;
	
	private GameAgent() {
		
	}
	
	
	public static GameAgent getInstance() {
		if(null == gameAgent) {
			gameAgent = new GameAgent();
		}
		
		return gameAgent;
	}
	
	public void setZoneChangeEvent( ZoneChangeEvent zoneChangeEvent)
	{
		//TODO : Do logic
		GameStateManager.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
}
