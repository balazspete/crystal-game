/**
 * 
 */
package com.example.crystalgame.game;

import java.util.List;

import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;

/**
 * Inventory Manager that mirrors the client datawarehouse
 * @author Allen Thomas Varghese
 */
public class InventoryManager {

	private static InventoryManager inventoryManager = null; 

	private String clientID, playerCharacterID, energyLevel;
	
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
			List<HasID> items = ClientDataWarehouse.getInstance().getList(Crystal.class);
			if (items == null || items.size() == 0) {
				return new Crystal[0];
			}
			
			return items.toArray(new Crystal[0]);
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
			List<HasID> items = ClientDataWarehouse.getInstance().getList(MagicalItem.class);
			if (items == null || items.size() == 0) {
				return new MagicalItem[0];
			}
			
			return items.toArray(new MagicalItem[0]);
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
			List<HasID> characters = ClientDataWarehouse.getInstance().getList(Character.class);
			if (characters == null || characters.size() == 0) {
				return new Character[0];
			}
			
			System.out.println("InventoryManager | getCharacters() : "+characters);
			
			return characters.toArray(new Character[0]);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getCharacters()",e.toString());
			return new Character[0];
		}
	}
	
	/**
	 * Returns the player playing the game
	 */
	public synchronized Character getCharacter() {
		Character character = null;
		Character[] characters;
		
		try {
			characters = ClientDataWarehouse.getInstance().getList(Character.class).toArray(new Character[0]);
		} catch (DataWarehouseException e1) {
			characters = new Character[0];
		}
		
		for (Character c : characters) {
			if (CrystalGame.getClientID().equals(c.getClientId()) && c.getCharacterType() != CharacterType.UNKNOWN) {
				character = c;
				break;
			}
		}
		
		return character;
	}
	
	public synchronized Character getUnknownCharacter() {
		Character character = null;
		Character[] characters;
		
		try {
			characters = ClientDataWarehouse.getInstance().getList(Character.class).toArray(new Character[0]);
		} catch (DataWarehouseException e1) {
			characters = new Character[0];
		}
		
		for (Character c : characters) {
			if (CrystalGame.getClientID().equals(c.getClientId()) && c.getCharacterType() == CharacterType.UNKNOWN) {
				character = c;
				break;
			}
		}
		
		return character;
	}
	
	/**
	 * Update the energy level of the game player
	 * @param energyLevel
	 */
	public synchronized void setEnergyLevel(String energyLevel) {
		this.energyLevel = energyLevel;
	}
	
	/**
	 * Update the location of the game character
	 * @param location
	 */
	public synchronized void setCharacterLocation(Location location) {
		Character c;
		try {
			c = getCharacter();
			if(null == c || null == location) {
				return;
			}
			
			// Set the new location for the character
			c.setLatitude(location.getLatitude());
			c.setLongitude(location.getLongitude());
			
			// Saving energy of character
			c.setEnergyLevel(energyLevel);
			
			// Remove the character
//			ClientDataWarehouse.getInstance().delete(Character.class, c.getID());
			
			// Add the characer back
			ClientDataWarehouse.getInstance().put(Character.class, c);
			
			Log.d("InventoryManager:setCharacterLocation(val)","Updated location to : Lat="+location.getLatitude()+" Lng="+location.getLongitude());
		} catch(DataWarehouseException e) {
			Log.d("InventoryManager:setCharacterLocation(val)",e.toString());
		}
	}
	
	/**
	 * Get ThroneRoom information 
	 * @return ThroneRoom
	 */
	public synchronized ThroneRoom getThroneRoom() {
		try {
			List<HasID> rooms = ClientDataWarehouse.getInstance().getList(ThroneRoom.class);
			if (rooms == null || rooms.size() == 0) {
				return null;
			}
			
			return (ThroneRoom) rooms.get(0);
		} catch (DataWarehouseException e) {
			Log.e("InventoryManager:getThroneRoom()",e.toString());
		}
		
		return null;
	}
	
	/**
	 * Returns the list of crystal zones
	 */
	public synchronized CrystalZone[] getCrystalZones() {
		CrystalZone[] crystalZones;
		
		try {
			crystalZones = ClientDataWarehouse.getInstance().getList(CrystalZone.class).toArray(new CrystalZone[0]);
		} catch (DataWarehouseException e1) {
			crystalZones = new CrystalZone[0];
		}
		
		return crystalZones;
	}
}
