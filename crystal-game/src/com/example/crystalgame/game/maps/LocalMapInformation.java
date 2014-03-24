package com.example.crystalgame.game.maps;

import com.example.crystalgame.game.GamePlayState;
import com.example.crystalgame.game.GameStateInformation;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;

/**
 * Holds the representation of the game objects for the map
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class LocalMapInformation extends MapInformation {

	/**
	 * Constructor
	 * @param gameStateInformation
	 */
	public LocalMapInformation(GameStateInformation gameStateInformation) {
		super(GamePlayState.LOCAL_MAP, gameStateInformation);
	}
	
	/**
	 * List of crystals
	 * @return Array of Crystals
	 */
	public Crystal[] getCrystalList() {
		return gameStateInformation.getCrystalList();
	}
	
	/**
	 * List of characters
	 * @return Array of Characters
	 */
	public Character[] getCharacterList() {
		return gameStateInformation.getCharacterList();
	}
	
	/**
	 * List of magical items
	 * @return Array of Magical Items
	 */
	public MagicalItem[] getMagicalItemList() {
		return gameStateInformation.getMagicalItemList();
	}
	
	/**
	 * Game Character
	 * @return Character
	 */
	public Character getGameCharacter() {
		return gameStateInformation.getGameCharacter();
	}
	
}
