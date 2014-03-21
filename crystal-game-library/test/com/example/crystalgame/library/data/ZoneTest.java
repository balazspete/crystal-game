package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ZoneTest 
{
	public ArrayList<Location> arrayList;
	public Location  location;
	@Before
	public void setUp() throws Exception 
	{
		arrayList = new ArrayList<Location>();
		arrayList.add(new Location(53.34373266956245, -6.247414081275463));
		arrayList.add(new Location(53.34372426266502 ,-6.247033790433407));
		arrayList.add(new Location(53.34363979327044, -6.247001939201355));
		arrayList.add(new Location(53.34363338800144, -6.2474171864748));
		location = new Location(53.349805, -6.260310);
	}


	@Test
	public void testInRadialZone() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testInQuadZone() 
	{
		assertTrue(Zone.inQuadZone(arrayList, location));
	}

}
