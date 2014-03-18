package com.example.crystalgame.server.game;
/**
 * Crystal Scatter
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.util.RandomNumber;

public class CrystalScatter 
{
	private CrystalZone crystalZone;
	
	public CrystalScatter(CrystalZone crystalZone)
	{
		this.crystalZone = crystalZone;
	}
	
	/**
	 * 
	 * @return
	 */
	private Crystal generateCrystal()
	{
		new RandomNumber();
		Double crystalLat = RandomNumber.getRandomDoubleNumber(crystalZone.getMinLattitudeValue(), 
				crystalZone.getMaxLattitudeValue());
		Double crystalLong = RandomNumber.getRandomDoubleNumber(crystalZone.getMinLongitudeValue(), 
				crystalZone.getMaxLongitudeValue());
		Crystal crystal = new Crystal(crystalLat, crystalLong);
		crystal.setZoneID(crystalZone.getID());
		return crystal;
	}

	/**
	 * 
	 * @param Range The number of generated crystals
	 * @return Generated list of crystals
	 */
	public List<Crystal> generateCrystals(int Range)
	{
		List<Crystal> Crystals = new ArrayList<Crystal>();
		for(int i = 0; i < Range; i++)
		{
			Crystals.add(generateCrystal());
		}
		return Crystals;
	}
	


}
