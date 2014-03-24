/**
 * 
 */
package com.example.crystalgame.game;

import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.events.ProximityEvent;
import com.example.crystalgame.library.events.ProximityEvent.ArtifactType;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.InstructionFormatException;
import com.example.crystalgame.location.ZoneChangeEvent;


/**
 * Game State Manager for keeping track of various 
 * processes that happen related to game state and game agent
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameStateManager {
	
	private static GameStateManager gameStateManager = null;
	
	private GameStateManager() {
		
	}
	
	/**
	 * Get the singleton instance of this class
	 * @return GameStateManager
	 */
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
		//GameState.getInstance().initializeGameState();
		GameAgent.getInstance().initializeGameAgent();
	}
	
	/**
	 * Proximity Alert with a character, crystal or magical item
	 */
	public synchronized void itemProximityAlert(Artifact item) {
		ProximityEvent proximityEvent = null;
		CrystalGame gameObj = GameManager.getInstance().getApplicationObj();
		
		if(item instanceof Crystal) {
			proximityEvent = new ProximityEvent(ArtifactType.CRYSTAL, item);
			
			if(null != gameObj) {
				try {
					gameObj.getCommunication()
							.out
							.relayInstructionToServer(
									GameInstruction.createCrystalCaptureRequestInstruction(gameObj.getClientID(), item.getID())
							);
					
					// Send the latest count of crystals
					GameManager.getInstance().crystalCaptureCallBack(InventoryManager.getInstance().getCharacter().getCrystals().size());
				} catch (InstructionFormatException e) {
					Log.e("GameStateManager", e.toString());
				}
			}
		} else if(item instanceof MagicalItem) {
			proximityEvent = new ProximityEvent(ArtifactType.MAGICAL_ITEM, item);
			
			if(null != gameObj) {
				try {
					gameObj.getCommunication()
							.out
							.relayInstructionToServer(
									GameInstruction.createMagicalItemCaptureRequestInstruction(gameObj.getClientID(), item.getID())
							);
					
					// Send the latest count of magical items
					GameManager.getInstance().magicalItemCaptureCallBack(InventoryManager.getInstance().getCharacter().getMagicalItems().size());
				} catch (InstructionFormatException e) {
					Log.e("GameStateManager", e.toString());
				}
			}
		} else if(item instanceof Character) {
			proximityEvent = new ProximityEvent(ArtifactType.CHARACTER, item);
		}
				
	}
	
	/**
	 * Callback function when the player is within a zone
	 * @param zoneChangeEvent
	 */
	public synchronized void zoneProximityAlert(ZoneChangeEvent zoneChangeEvent)
	{
		GameAgent.getInstance().setZoneChangeEvent(zoneChangeEvent);
	}
	
	/**
	 * Callback function when zones are changed
	 * @param zoneChangeEvent
	 */
	public synchronized void zoneChangeCallBack(ZoneChangeEvent zoneChangeEvent)
	{
		GameManager.getInstance().zoneChangeCallBack(zoneChangeEvent);
	}
	
	/**
	 * Pass the current energy level to the game state object
	 * @param energyLevel
	 */
	public synchronized void energyChangeCallBack(double energyLevel) {
		GameState.getInstance().updateEnergyLevel(energyLevel);
	}
	
	/**
	 * Callback function when energy becomes low
	 * @param energyEvent
	 */
	public synchronized void energyLowCallBack(EnergyEvent  energyEvent)
	{
		GameManager.getInstance().energyLowCallBack(energyEvent);
	}
}
