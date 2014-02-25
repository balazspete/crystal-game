package com.example.crystalgame.library.game;

import com.example.crystalgame.library.data.CrystalLocation;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
/**
 * @author Shen Chen
 *
 */
public class CrystalLocationController 
{
	private DB4OInterface store;
	private GameLocation gameLocation;
	
	public CrystalLocationController(GameLocation gameLocation)
	{
		this.gameLocation = gameLocation;
	}
	/**
	 * 
	 * @return
	 */
	private CrystalLocation generateCrystalLocation()
	{
		return null;
	}
	/**
	 * 
	 * @param LocationNumbers The number of generated crystal locations
	 * @return Generated crystal locations
	 */
	public boolean generateCrystalLocations(int LocationNumbers)
	{
		for(int i = 0; i < LocationNumbers; i++)
		{
			storeCystalLocation(generateCrystalLocation());
		}
		return true;
	}
	
	/**
	 * 
	 * @param crystalLocation
	 */
	private void storeCystalLocation(CrystalLocation crystalLocation)
	{
		this.store.put(CrystalLocation.class, crystalLocation);	
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean generateCrystal()
	{
		return false;
	}

}
