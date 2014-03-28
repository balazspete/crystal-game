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
	 * Proximity Alert with a character, crystal or magical item
	 */
	public synchronized void itemProximityAlert(Artifact item) {
		ProximityEvent proximityEvent = null;
		CrystalGame gameObj = GameManager.getInstance().getApplicationObj();
		
		if(item instanceof Crystal) {
			Log.i("GameStateManager","Crystal captured : "+item.getID());
			proximityEvent = new ProximityEvent(ArtifactType.CRYSTAL, item);
			
			if(null != gameObj) {
				try {
					System.out.println("GameStateManager : Sending crystal capture request to server...");
					CrystalGame.getCommunication()
							.out
							.relayInstructionToServer(
									GameInstruction.createCrystalCaptureRequestInstruction(CrystalGame.getClientID(), item.getID())
							);
					
					System.out.println("GameStateManager : Received crystal capture response from server..");
					
					// Send the latest count of crystals
					GameManager.getInstance().crystalCaptureCallBack(InventoryManager.getInstance().getCharacter().getCrystals().size());
					
					System.out.println("GameStateManager : Initiating crystal capture callback...");
				} catch (InstructionFormatException e) {
					Log.e("GameStateManager", e.toString());
				}
			}
		} else if(item instanceof MagicalItem) {
			proximityEvent = new ProximityEvent(ArtifactType.MAGICAL_ITEM, item);
			Log.i("GameStateManager","Magical Item captured : "+item.getID());
			
			if(null != gameObj) {
				try {
					System.out.println("GameStateManager : Sending magical item capture request to server...");
					
					CrystalGame.getCommunication()
							.out
							.relayInstructionToServer(
									GameInstruction.createMagicalItemCaptureRequestInstruction(CrystalGame.getClientID(), item.getID())
							);
					
					System.out.println("GameStateManager : Received magical item capture response from server..");
					
					// Send the latest count of magical items
					GameManager.getInstance().magicalItemCaptureCallBack(InventoryManager.getInstance().getCharacter().getMagicalItems().size());
					
					System.out.println("GameStateManager : Initiating magical item capture callback...");
				} catch (InstructionFormatException e) {
					Log.e("GameStateManager", e.toString());
				}
			}
		} else if(item instanceof Character) {
			Character c  = (Character) item;
			proximityEvent = new ProximityEvent(ArtifactType.CHARACTER, item);
			InteractionManager.getInstance().initiateInteraction(CrystalGame.getCommunication().out, c.getClientId());
		}
				
	}
	
	/**
	 * Callback function when the player is within a zone
	 * @param zoneChangeEvent
	 */
	public synchronized void zoneProximityAlert(ZoneChangeEvent zoneChangeEvent)
	{
		zoneChangeCallBack(zoneChangeEvent);
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
	 * Callback function when energy becomes low
	 * @param energyEvent
	 */
	public synchronized void energyLowCallBack(EnergyEvent  energyEvent)
	{
		GameManager.getInstance().energyLowCallBack(energyEvent);
	}
}
