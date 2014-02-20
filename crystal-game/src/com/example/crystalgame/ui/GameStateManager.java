/**
 * 
 */
package com.example.crystalgame.ui;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.events.ProximityEvent;
import com.example.crystalgame.library.events.ProximityEvent.ArtifactType;


/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameStateManager {

//	private GameState gameState = GameState;
//	private GameAgent gameAgent = new GameAgent();
	
	private static GameStateManager gameStateManager = null;
	
	private GameStateManager() {
		
	}
	
	public static GameStateManager getInstance() {
		if(null == gameStateManager) {
			gameStateManager = new GameStateManager();
		}
		return gameStateManager;
	}
	
	/**
	 * Start the components that is part of this block
	 */
	public void startComponents() {
		//TODO : Initialize sub-components
	}
	
	/**
	 * Proximity Alert with a character, crystal or magical item
	 */
	public void itemProximityAlert(Artifact item) {
		ProximityEvent proximityEvent = null;
		if(item instanceof Crystal) {
			proximityEvent = new ProximityEvent(ArtifactType.CRYSTAL, item);
		} else if(item instanceof MagicalItem) {
			proximityEvent = new ProximityEvent(ArtifactType.MAGICAL_ITEM, item);
		} else if(item instanceof Character) {
			proximityEvent = new ProximityEvent(ArtifactType.CHARACTER, item);
		}
		
		// Passes the information to the GameState object
		// to update the game information
		GameState.getInstance().itemProximityAlert(proximityEvent);
	}
	
	public void zoneProximityAlert(ZoneChangeEvent zoneChangeEvent)
	{
		GameAgent.getInstance().setZoneChangeEvent(zoneChangeEvent);
	}
	
	public void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		GameManager.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
}
