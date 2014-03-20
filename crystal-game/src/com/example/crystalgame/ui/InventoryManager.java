/**
 * 
 */
package com.example.crystalgame.ui;

import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;

/**
 * Inventory Manager that mirrors the client datawarehouse
 * @author Allen Thomas Varghese
 */
public class InventoryManager {

	private static InventoryManager inventoryManager = null; 

	private String clientID, playerCharacterID;
	
	/**
	 * Private constructor
	 */
	private InventoryManager() {
	}

	/**
	 * Get access to singleton instance of the class
	 * @return InventoryManager
	 */
	public static InventoryManager getInstance() {
		if(null == inventoryManager) {
			inventoryManager = new InventoryManager();
		}
		
		return inventoryManager;
	}
	
	/**
	 * Get all the crystals
	 * @return List of crystals
	 */
	public synchronized Crystal[] getCrystals() {
		try {
			return ClientDataWarehouse.getInstance().getList(Crystal.class).toArray(new Crystal[0]);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getCrystals()",e.toString());
			return new Crystal[0];
		}
	}
	
	/**
	 * Get all the magical items
	 * @return List of magical items
	 */
	public synchronized MagicalItem[] getMagicalItems() {
		try {
			return ClientDataWarehouse.getInstance().getList(MagicalItem.class).toArray(new MagicalItem[0]);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getMagicalItems()",e.toString());
			return new MagicalItem[0];
		}
	}
	
	/**
	 * Get all game characters
	 * @return List of characters
	 */
	public synchronized Character[] getCharacters() {
		try {
			return ClientDataWarehouse.getInstance().getList(Character.class).toArray(new Character[0]);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getCharacters()",e.toString());
			return new Character[0];
		}
	}
	
	/**
	 * Returns the player playing the game
	 */
	public synchronized Character getCharacter() {
		if(playerCharacterID == null) {
			for(Character gamePlayer : getCharacters()) {
				if(gamePlayer.getClientId().equals(CrystalGame.getClientID())) {
					playerCharacterID = gamePlayer.getID();
					return gamePlayer;
				}
			}
		} else  {
			try {
				Character c = (Character) ClientDataWarehouse.getInstance().get(Character.class, playerCharacterID);
				if (c == null) {
					playerCharacterID = null;
					c = getCharacter();
				}
				return c;
			} catch (DataWarehouseException e) {
				Log.e("InventoryManager:getGamePlayer()",e.toString());
			}
		}
		return null;
	}
	
	/**
	 * Update the energy level of the game player
	 * @param energyLevel
	 */
	public synchronized void setEnergyLevel(String energyLevel) {
		Character c;
		try {
			c = getCharacter();
			if (c == null) {
				return;
			}
			c.setEnergyLevel(energyLevel);
			ClientDataWarehouse.getInstance().put(Character.class, c);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:setEnergyLevel(val)",e.toString());
		}
		
	}
	
	/**
	 * Get ThroneRoom information 
	 * @return ThroneRoom
	 */
	public synchronized ThroneRoom getThroneRoom() {
		ThroneRoom throneRoom = null;
		try {
			throneRoom = (ThroneRoom)ClientDataWarehouse.getInstance().getList(ThroneRoom.class).get(0);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getThroneRoom()",e.toString());
		}
		
		return throneRoom;
	}
}
