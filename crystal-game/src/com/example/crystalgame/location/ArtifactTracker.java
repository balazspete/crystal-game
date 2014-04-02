/**
 * 
 */
package com.example.crystalgame.location;

import java.util.ArrayList;

import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Item;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.Zone;

/**
 * Tracker class for crystals and magical items
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class ArtifactTracker {
	
	private static ArtifactTracker artifactTracker = null;
	
	/* Game items information */
	private Crystal[] crystalList = null;
	private MagicalItem[] magicalItemList = null;
	private Character[] characterList = null;
	
	private ArtifactTracker() {
	}
	
	public static ArtifactTracker getInstance() {
		if(null == artifactTracker) {
			artifactTracker = new ArtifactTracker();
		}
		
		return artifactTracker;
	}
	
//	/**
//	 * Finds the artifact/character in proximity.
//	 * @return the first artifact in proximity
//	 */
//	public synchronized Artifact getArtifactsInProximity(Location location) {
//		Artifact artifactItem = null;
////		
////		crystalList = InventoryManager.getInstance().getCrystals();
////		
////		// Checking the crystal list
////		for(Crystal item : crystalList) {
////			if(location.
////					Zone.inRadialZone(item.getLocation(), location)) {
////				return item;
////			}
////		}
////		
////		magicalItemList = InventoryManager.getInstance().getMagicalItems();
////		
////		// Checking the magical item list
////		for(MagicalItem item : magicalItemList) {
////			if(Zone.inRadialZone(item.getLocation(), location)) {
////				return item;
////			}
////		}
////		
////		characterList = InventoryManager.getInstance().getCharacters();
////		
////		// Checking the character list
////		for(Character item : characterList) {
////			if(Zone.inRadialZone(item.getLocation(), location)) {
////				return item;
////			}
////		}
////		
//		return artifactItem;
//	}
//	
	public void markItem(Item item) {
		
	}
}
