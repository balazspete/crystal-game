/**
 * 
 */
package com.example.crystalgame.game;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.location.ZoneChangeEvent;


/**
 * Game State Manager for keeping track of various 
 * processes that happen related to game state and game agent
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class GameStateManager extends Thread {
	
	private LinkedBlockingQueue<Runnable> instructions;
	
	private static GameStateManager gameStateManager = null;
	private int crystalCount = 0;
	private int magicalItemCount = 0;
	
	private GameStateManager() {
		instructions = new LinkedBlockingQueue<Runnable>();
	}
	
	/**
	 * Get the singleton instance of this class
	 * @return GameStateManager
	 */
	public static GameStateManager getInstance() {
		if(null == gameStateManager) {
			gameStateManager = new GameStateManager();
			gameStateManager.start();
		}
		
		return gameStateManager;
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
	
	@Override
	public void run() {
		while (true) {
			try {
				Runnable instruction = instructions.poll(Long.MAX_VALUE, TimeUnit.SECONDS);
				if (instruction != null) {
					instruction.run();
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	public boolean captureCrystal(final Character character, final Crystal crystal) {
		try {
			instructions.put(new Runnable() {
				@Override
				public void run() {
					try {
						ClientDataWarehouse.getInstance().delete(Crystal.class, crystal.getID());
						System.out.println("Crystal deleted successfully : " + crystal);
					} catch (DataWarehouseException e) {
						System.out.println("Failed to save crystal : " + crystal);
						return;
					}

					character.addCrystal(crystal);
					try {
//						ClientDataWarehouse.getInstance().delete(Character.class, character.getID());
						ClientDataWarehouse.getInstance().put(Character.class, character);
						System.out.println("Character updated successfully : " + character);
					} catch (DataWarehouseException e) {
						System.out.println("Failed to save character : " + character);
						
						try {
							ClientDataWarehouse.getInstance().put(Crystal.class, crystal);
							System.out.println("Crystal restored successfully : " + crystal);
						} catch (DataWarehouseException e1) {
							System.out.println("Restoring crystal failed : " + crystal);
						}
					}
				}
			});
		} catch (InterruptedException e) {
			Log.e("GameStateManager", "Failed to pick up item");
			return false;
		}
		
		return true;
	}
	
	public boolean captureMagicalItem(final Character character, final MagicalItem item) {
		try {
			instructions.put(new Runnable() {
				@Override
				public void run() {
					try {
						ClientDataWarehouse.getInstance().delete(MagicalItem.class, item.getID());
						System.out.println("Magical Item deleted successfully : " + item);
					} catch (DataWarehouseException e) {
						System.out.println("Failed to save Magical Item : " + item);
						return;
					}

					character.addMagicalItem(item);
					try {
//						ClientDataWarehouse.getInstance().delete(Character.class, character.getID());
						ClientDataWarehouse.getInstance().put(Character.class, character);
						System.out.println("Character updated successfully : " + character);
					} catch (DataWarehouseException e) {
						System.out.println("Failed to save character : " + character);
						
						try {
							ClientDataWarehouse.getInstance().put(MagicalItem.class, item);
							System.out.println("Magical Item restored successfully : " + item);
						} catch (DataWarehouseException e1) {
							System.out.println("Restoring Magical Item failed : " + item);
						}
					}
				}
			});

		} catch (InterruptedException e) {
			Log.e("GameStateManager", "Failed to pick up item");
			return false;
		}
		
		return true;
	}
	
}
