package com.example.crystalgame.library.game;

import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;

/**
 * @author Shen Chen
 *
 */
public class CrystalController 
{	
	private DB4OInterface store;
	private Crystal crystal;
	private GameLocation gameLocation;
	
	public CrystalController() 
	{
		
	}

	public CrystalController(Crystal crystal, GameLocation gameLocation) 
	{
		this.crystal = crystal;
		this.gameLocation = gameLocation;
	}
	/**
	 * 
	 * @return
	 */
	public boolean storeCrystal()
	{
		return store.put(Crystal.class, crystal) != null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Crystal getCrystal()
	{
		return (Crystal) store.get(Crystal.class, crystal.getID());
	}

}
