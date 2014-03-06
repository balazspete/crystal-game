package com.example.crystalgame.library.game;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;

public class MagicalItemScatterTest {

	private MagicalItemScatter magicalItemScatter;
	private GameLocation gameLocation = new GameLocation();
	
	@Before
	public void createMagicalItemScatter()
	{
		List<Location> locations = new LinkedList<Location>();
		locations.add(new Location(1, 2));
		locations.add(new Location(1, 3));
		locations.add(new Location(2, 3));
		locations.add(new Location(2, 2));
		for(Location location : locations) 
		{
			if(location != null)
			{
				this.gameLocation.addLocation(location);
			}
		}
		magicalItemScatter = new MagicalItemScatter(gameLocation);
	}
	
	@Test
	public void testMagicalItemScatter() 
	{
		assertNotNull(magicalItemScatter);
	}

	
	@Test
	public void testGenerateMagicItem() 
	{
		assertNotNull(magicalItemScatter.generateMagicItem());
	}
	
}
