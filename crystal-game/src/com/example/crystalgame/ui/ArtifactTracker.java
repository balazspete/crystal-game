/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Zone;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class ArtifactTracker {
	
	private static ArtifactTracker artifactTracker = null;
	private ArrayList<Crystal> crystalList = new ArrayList<Crystal>();
	private ArrayList<MagicalItem> magicalItemList = new ArrayList<MagicalItem>();
	private ArrayList<Character> characterList = new ArrayList<Character>();
	
	private ArtifactTracker() {
		
	}
	
	public static ArtifactTracker getInstance() {
		if(null == artifactTracker) {
			artifactTracker = new ArtifactTracker();
		}
		
		return artifactTracker;
	}
	
	public void addCrystal(Crystal crystalItem) {
		crystalList.add(crystalItem);
	}
	
	public void setCrystalList(ArrayList<Crystal> crystalList) {
		this.crystalList = crystalList;
	}
	
	public void addMagicalItem(MagicalItem magicalItem) {
		magicalItemList.add(magicalItem);
	}
	
	public void setMagicalItemList(ArrayList<MagicalItem> magicalItemList) {
		

		
		this.magicalItemList = magicalItemList;
	}
	
	public void addCharacter(Character character) {
		characterList.add(character);
	}
	
	public void setCharacterList(ArrayList<Character> characterList) {
		this.characterList = characterList;
	}
	
	
	public ArrayList<MagicalItem> getMagicalItemInfoList()
	{
		for(int i=0;i<5;i++)
		{
			MagicalItem item = new MagicalItem(23.0, 43.0);
			item.setMagicalItemDescription("Item"+i);
			magicalItemList.add(item);
		}
		return magicalItemList;
	}
	
	/**
	 * Finds the artifact/character in proximity.
	 * @return the first artifact in proximity
	 */
	public Artifact getArtifactsInProximity(Location location) {
		Artifact artifactItem = null;
		
		/*
		// Checking the crystal list
		for(Crystal item : crystalList) {
			if(Zone.inRadialZone(item.getLocation(), location)) {
				return item;
			}
		}
		
		// Checking the magical item list
		for(MagicalItem item : magicalItemList) {
			if(Zone.inRadialZone(item.getLocation(), location)) {
				return item;
			}
		}
		
		// Checking the character list
		for(Character item : characterList) {
			if(Zone.inRadialZone(item.getLocation(), location)) {
				return item;
			}
		}
		
		return artifactItem;
		*/
		return new Crystal(location.getLatitude(), location.getLongitude());
	}
}
