package com.example.crystalgame.server.game;
/**
 * Magical Item Scatter
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Item;
import com.example.crystalgame.library.data.Item.ItemType;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.util.RandomNumber;


public class MagicalItemScatter 
{
	private GameLocation gameLocation;
	/**
	 * 
	 * @param gameLocation
	 */
	public MagicalItemScatter(GameLocation gameLocation)
	{
		this.gameLocation = gameLocation;
	}
	

	/**
	 * 
	 * @return
	 */
	public MagicalItem generateMagicItem()
	{
		new RandomNumber();
		double MagicalItemLattitude = RandomNumber.getRandomDoubleNumber(this.gameLocation.getMinLattitudeValue(), 
																	     this.gameLocation.getMaxLattitudeValue());
		double MagicalItemLongitude = RandomNumber.getRandomDoubleNumber(this.gameLocation.getMinLongitudeValue(), 
															             this.gameLocation.getMaxLongitudeValue());
		MagicalItem magicalItem = new MagicalItem(MagicalItemLattitude, MagicalItemLongitude);
		magicalItem.setZoneID(this.gameLocation.getID());
		return magicalItem;
	}

	/**
	 * 
	 * @param Range The number of generated crystals
	 * @return Generated list of crystals
	 */
	public List<MagicalItem> generateMagicItems(int Range)
	{
		List<MagicalItem> MagicalItems = new ArrayList<MagicalItem>();
		for(int i = 0; i < Range; i++)
		{
			MagicalItems.add(generateMagicItem());
		}
		return MagicalItems;
	}

	
}
