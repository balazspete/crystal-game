/**
 * 
 */
package com.example.crystalgame.game;

import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
/**
 * Abstraction of game state information 
 * @author Allen Thomas Varghese, Chen Shen
 */
public class GameStateInformation {


	public Crystal[] getCrystalList() {
		return InventoryManager.getInstance().getCrystals();
	}

	/**
	 * Get list of magical items
	 * @return
	 */
	public MagicalItem[] getMagicalItemList() {
		return InventoryManager.getInstance().getMagicalItems();
	}
	
	/**
	 * Get list of characters
	 * @return Array of Character
	 */
	public Character[] getCharacterList() {
		return InventoryManager.getInstance().getCharacters();
	}
	
	/**
	 * Get game character
	 * @return Character
	 */
	public Character getGameCharacter() {
		return InventoryManager.getInstance().getCharacter();
	}
	
}
