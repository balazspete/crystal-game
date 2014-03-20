package com.example.crystalgame.server.game;
/**
 * Crystal Zone Scatter
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.ArrayList;
import java.util.List;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.util.RandomNumber;

public class CrystalZoneScatter 
{

	private int range;

	private GameLocation gameLocation;
	ArrayList<CrystalZone> crystalMetrics = new ArrayList<CrystalZone>();
	ArrayList<CrystalZone> crystalZones= new ArrayList<CrystalZone>();
	ArrayList<Location> locationList;
	
	public CrystalZoneScatter(GameLocation gameLocation, int range)
	{
		this.gameLocation = gameLocation;
		this.range = range;
	}
	
	
	public ArrayList<CrystalZone> generateCrystalMetrics()
	{
		CrystalZone crystalZone ;
		double zLow = 0.0 , kLow=0.0, zHigh=0.0,kHigh = 0.0;
		zLow=gameLocation.getMinLongitudeValue();
		
		for(int i=0;i<range;i++)
		{
			kLow = gameLocation.getMinLattitudeValue();
			zHigh = (gameLocation.getMaxLongitudeValue()-gameLocation.getMinLongitudeValue())/range+zLow;

			if(zHigh>gameLocation.getMaxLongitudeValue())
			{
				break;
			}
			for(int j=0; j<range;j++)
			{
				kHigh = (gameLocation.getMaxLattitudeValue()-gameLocation.getMinLattitudeValue())/range + kLow;
				if(kHigh>gameLocation.getMaxLattitudeValue())
				{
					kLow = gameLocation.getMinLattitudeValue();
					kHigh = gameLocation.getMaxLattitudeValue();
					zLow = zHigh;
					break;
				}
				crystalZone = new CrystalZone();
				locationList = new ArrayList<Location>();
				locationList.add(new Location(kLow, zLow)); 
				locationList.add(new Location(kHigh, zLow));
				locationList.add(new Location(kHigh, zHigh));
				locationList.add(new Location(kLow, zHigh));
				crystalZone.setLocationList(locationList);
				crystalMetrics.add(crystalZone);
				kLow = kHigh;
				
			}
			kLow = gameLocation.getMinLattitudeValue();
			kHigh = gameLocation.getMaxLattitudeValue();
			zLow = zHigh;
			
		}
		return crystalMetrics;
	}
	
	public void printOut()
	{
		for(CrystalZone crystalZone: crystalMetrics)
		{
			System.out.println("locations  ...............");
			System.out.println(crystalZone.getLocation(0));
			System.out.println(crystalZone.getLocation(1));
			System.out.println(crystalZone.getLocation(2));
			System.out.println(crystalZone.getLocation(3));
		}
		
	}
	
	public ArrayList<CrystalZone> generateCrystalZones(int range)
	{
		CrystalZone crystalZone;
		crystalMetrics = generateCrystalMetrics();
		for(int i = 0; i < range; i++)
		{
			crystalZone =  crystalMetrics.get(RandomNumber.getRandomIntNumber(1, crystalMetrics.size()));
			if(!(crystalZones.contains(crystalZone)))
			{
				crystalZones.add(crystalZone);
			}
			else
			{
				i = i-1;
			}
			
		}
		return crystalZones;
	}

	

}
