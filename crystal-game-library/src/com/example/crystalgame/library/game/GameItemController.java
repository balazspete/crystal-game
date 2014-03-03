package com.example.crystalgame.library.game;

import com.example.crystalgame.library.data.CrystalLocation;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;

/**
 * @author Shen Chen
 *
 */
public class GameItemController 
{
	private GameLocation gameLocation;
	public GameItemController()
	{
	}
	/**
	 * 
	 * @param gameLocation
	 */
	public GameItemController(GameLocation gameLocation)
	{
		this.gameLocation = gameLocation;
	}

	
}
