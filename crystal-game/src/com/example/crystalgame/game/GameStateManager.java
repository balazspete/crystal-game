/**
 * 
 */
package com.example.crystalgame.game;

import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
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
	private int crystalCount = 0;
	private int magicalItemCount = 0;
	
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
	public synchronized void itemProximityAlert(final Artifact item) {
		ProximityEvent proximityEvent = null;
		final CrystalGame gameObj = GameManager.getInstance().getApplicationObj();
		
		if(item instanceof Crystal) {
			Log.i("GameStateManager","Crystal captured : "+item.getID());
			proximityEvent = new ProximityEvent(ArtifactType.CRYSTAL, item);
			
			System.out.println("Application Object is : "+gameObj);
			
			if(null != gameObj) {
				final Character c = InventoryManager.getInstance().getCharacter();
				
				Log.i("GameStateManager","Character : "+c);
				
				if(null!=c && c.getClientId() != null) {
					System.out.println("GameStateManager : Sending crystal capture request to server...");
					
					new Thread(new Runnable() {
						@Override
						public void run() {
//							try {
//								gameObj.getCommunication()
//										.out
//										.relayInstructionToServer(
//												GameInstruction.createCrystalCaptureRequestInstruction(c.getClientId(), c.getID(), item.getID())
//										);
//							} catch (InstructionFormatException e) {
//								Log.e("GameStateManager", e.toString());
//							}
//							
							// Check if deleting a crystal fails
							synchronized (GameStateManager.this) {
								try {
									ClientDataWarehouse.getInstance().delete(Crystal.class, item.getID());
									System.out.println("Crystal deleted successfully : "+item);
								} catch (DataWarehouseException e) {
									System.out.println("Failed to save crystal : "+item);
									return;
								}
	
								c.addCrystal((Crystal)item);
								try {
									ClientDataWarehouse.getInstance().delete(Character.class, c.getID());
									ClientDataWarehouse.getInstance().put(Character.class, c);
									System.out.println("Character updated successfully : "+c);
								} catch (DataWarehouseException e) {
									System.out.println("Failed to save character : "+c);
									
									try {
										ClientDataWarehouse.getInstance().put(Crystal.class, item);
										System.out.println("Crystal restored successfully : "+item);
									} catch (DataWarehouseException e1) {
										System.out.println("Restoring crystal failed : "+item);
									}
									return;
								}
							}
							//System.out.println("GameStateManager : Received crystal capture response from server..");
							
							// Send the latest count of crystals
							GameManager.getInstance().crystalCaptureCallBack(InventoryManager.getInstance().getCharacter().getCrystals().size());
							//GameManager.getInstance().crystalCaptureCallBack(++crystalCount);
							GameManager.getInstance().removeCrystalFromMap((Crystal)item);
							
							System.out.println("GameStateManager : Initiating crystal capture callback...");
						}
					}).start();
				}
			}
		} else if(item instanceof MagicalItem) {
			proximityEvent = new ProximityEvent(ArtifactType.MAGICAL_ITEM, item);
			Log.i("GameStateManager","Magical Item captured : "+item.getID());
			
			System.out.println("Application Object is : "+gameObj);
			
			if(null != gameObj) {
//				try {
					final Character c = InventoryManager.getInstance().getCharacter();
					//final Character c = CrystalGame.getGameCharacter();
				
					Log.i("GameStateManager","Character : "+c);
					
					
					if(null!=c && c.getClientId() != null) {
						System.out.println("GameStateManager : Sending magical item capture request to server...");
						
//						gameObj.getCommunication()
//								.out
//								.relayInstructionToServer(
//										GameInstruction.createMagicalItemCaptureRequestInstruction(c.getClientId(), c.getID(), item.getID())
//								);

						// Check if deleting a crystal fails
						new Thread(new Runnable() {
							@Override
							public void run() {
								synchronized (GameStateManager.this) {
							
									try {
										ClientDataWarehouse.getInstance().delete(MagicalItem.class, item.getID());
										System.out.println("Magical Item deleted successfully : "+item);
									} catch (DataWarehouseException e) {
										System.out.println("Failed to save Magical Item : "+item);
										return;
									}
			
									c.addMagicalItem((MagicalItem)item);
									try {
										ClientDataWarehouse.getInstance().delete(Character.class, c.getID());
										ClientDataWarehouse.getInstance().put(Character.class, c);
										System.out.println("Character updated successfully : "+c);
									} catch (DataWarehouseException e) {
										System.out.println("Failed to save character : "+c);
										
										try {
											ClientDataWarehouse.getInstance().put(MagicalItem.class, item);
											System.out.println("Magical Item restored successfully : "+item);
										} catch (DataWarehouseException e1) {
											System.out.println("Restoring Magical Item failed : "+item);
										}
										return;
									}

									//System.out.println("GameStateManager : Received magical item capture response from server..");
									
									// Send the latest count of magical items
									GameManager.getInstance().magicalItemCaptureCallBack(InventoryManager.getInstance().getCharacter().getMagicalItems().size());
									//GameManager.getInstance().magicalItemCaptureCallBack(++magicalItemCount);
									GameManager.getInstance().removeMagicalItemFromMap((MagicalItem)item);
									
									System.out.println("GameStateManager : Initiating magical item capture callback...");
								}
							}
						}).start();
//				} catch (InstructionFormatException e) {
//					Log.e("GameStateManager", e.toString());
//				}
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
