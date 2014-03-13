package com.example.crystalgame.library.game;

import java.util.ArrayList;
import java.util.List;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.util.RandomNumber;

public class CrystalZoneScatter 
{

	int range = 3;
	private GameLocation gameLocation;
	ArrayList<CrystalZone> crystalZones = new ArrayList<CrystalZone>();
	ArrayList<Location> locationList;
	public CrystalZoneScatter(GameLocation gameLocation)
	{
		this.gameLocation = gameLocation;
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<CrystalZone> generateCrystalZone()
	{
		CrystalZone crystalZone ;
/*		Double CrystalLocationLat = RandomNumber.getRandomDoubleNumber(gameLocation.getMinLattitudeValue()+ 0.02,gameLocation.getMaxLattitudeValue() - 0.02);
		Double CrystalLocationLong = RandomNumber.getRandomDoubleNumber(gameLocation.getMinLongitudeValue() + 0.02,gameLocation.getMaxLongitudeValue() - 0.02);*/
		/*crystalZone.addLocation(new Location(CrystalLocationLat+ 0.01,CrystalLocationLong+ 0.01));
		crystalZone.addLocation(new Location(CrystalLocationLat- 0.01,CrystalLocationLong+ 0.01));
		crystalZone.addLocation(new Location(CrystalLocationLat+ 0.01,CrystalLocationLong- 0.01));
		crystalZone.addLocation(new Location(CrystalLocationLat- 0.01,CrystalLocationLong- 0.01));*/
		double zLow = 0.0 , kLow=0.0, zHigh=0.0,kHigh = 0.0;
		
		for(int i=0;i<range;i++)
		{
			zLow=gameLocation.getMinLongitudeValue();
			zHigh = (gameLocation.getMaxLongitudeValue()-gameLocation.getMinLongitudeValue())/range+zLow;
			
			for(int j=0; j<range;j++)
			{
				kLow = gameLocation.getMinLattitudeValue();
				kHigh = (gameLocation.getMaxLattitudeValue()-gameLocation.getMinLattitudeValue())/range + kLow;
				crystalZone = new CrystalZone();
				locationList = new ArrayList<Location>();
				locationList.add(new Location(kLow, zLow)); 
				locationList.add(	new Location(kHigh, zLow));
				locationList.add(	new Location(kHigh, zHigh));
				locationList.add(	new Location(kLow, zHigh));
				crystalZone.setLocationList(locationList);
				crystalZones.add(crystalZone);
			}
			
		}
		
		return crystalZones;
	}

	

}
