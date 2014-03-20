/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.Warrior;
import com.example.crystalgame.library.data.Wizard;

/**
 * Abstraction of game state information 
 * @author Allen Thomas Varghese, Chen Shen
 */
public class GameStateInformation {

	/*
	private ArrayList<Crystal> crystalList = new ArrayList<Crystal>();
	private ArrayList<MagicalItem> magicalItemList = new ArrayList<MagicalItem>();
	private ArrayList<Character> characterList = new ArrayList<Character>();
	*/
	
	private Crystal[] crystalList = null;
	private MagicalItem[] magicalItemList = null;
	private Character[] characterList = null;
	private Character gameCharacter = null;
	
	/**
	 * 
	 */
	public GameStateInformation() {
		// TODO Auto-generated constructor stub
		// Adding location of lab
		//crystalList.add(new Crystal(53.343833,-6.246786));
		//crystalList.add(new Crystal(53.343679826179724, -6.246770583093166));
		//crystalList.add(new Crystal(53.34368663177058 ,-6.2467313557863235));
		
		//magicalItemList.add(new MagicalItem(53.34373266956245 ,-6.246914081275463));
		//magicalItemList.add(new MagicalItem(53.34363979327044, -6.246601939201355));
		
//		characterList.add(new Wizard(53.34363338800144 ,-6.2468171864748, PlayerType.PLAYER,"C343")); 
//		characterList.add(new Warrior(53.34374948335228 ,-6.246790029108524, PlayerType.PLAYER, "C454"));

		// Get list of crystals
		crystalList = InventoryManager.getInstance().getCrystals();
		
		// Get list of magical items
		magicalItemList = InventoryManager.getInstance().getMagicalItems();
		
		// Get list of characters
		characterList = InventoryManager.getInstance().getCharacters();
		
		// Get the game character
		gameCharacter = InventoryManager.getInstance().getCharacter();
	}
	
	
	public Crystal[] getCrystalList() {
		return crystalList;
	}

	/**
	 * Get list of magical items
	 * @return
	 */
	public MagicalItem[] getMagicalItemList() {
		return magicalItemList;
	}
	
	/**
	 * Get list of characters
	 * @return Array of Character
	 */
	public Character[] getCharacterList() {
		return characterList;
	}
	
	/**
	 * Get game character
	 * @return Character
	 */
	public Character getGameCharacter() {
		return gameCharacter;
	}
	
}
