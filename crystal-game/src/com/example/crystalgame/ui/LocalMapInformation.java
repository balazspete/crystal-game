package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.MagicalItem;

/**
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class LocalMapInformation extends MapInformation {

	public LocalMapInformation(GameStateInformation gameStateInformation) {
		super(GamePlayState.LOCAL_MAP, gameStateInformation);
	}
	
	public ArrayList<Crystal> getCrystalList() {
		return gameStateInformation.getCrystalList();
	}
	
	public ArrayList<Character> getCharacterList() {
		return gameStateInformation.getCharacterList();
	}
	
	public ArrayList<MagicalItem> getMagicalItemList() {
		return gameStateInformation.getMagicalItemList();
	}
}
