package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ZoneTest {

	protected Zone zone;
	ArrayList<Location> locations;
	Location itemLocation ;
	Location testPosiLocation;
	Location quadLocation;
	
	@Before
	public void setup() {
		zone = new Zone();
		final double QUARTERPI = Math.PI / 4.0;

		// Radius of earth
		final double EARTH_RADIUS = 6378.1 * 1000;
		
		// Entry radius around an item expressed in meters
		final double RADIUS_OF_ENTRY = 100;
		testPosiLocation = new Location(53.343749, -6.247112);
		itemLocation = new Location(53.343724, -6.24694);
		locations  = new ArrayList<Location>();
		Location location1 = new Location(0, 0);
		location1.setMarkerID("123");
		Location location2 = new Location(0, 1);
		location1.setMarkerID("124");
		Location location3 = new Location(1, 1);
		location1.setMarkerID("125");
		Location location4 = new Location(1, 0);
		location1.setMarkerID("126");
		locations.add(location1);
		locations.add(location2);
		locations.add(location3);
		locations.add(location4);
		zone.setLocationList(locations);
		quadLocation = new Location(0.5, 0.5);
		
	}
	
	@Test
	public void testInQuadZone() {
		assertTrue(Zone.checkIfWithin(locations, quadLocation));
	}

	@Test
	public void testGetLocationInt() {
		assertNotNull(zone.getLocation(0));
	}

	@Test
	public void testGetLocationString() {
		assertNotNull(zone.getLocation("126"));
	}

	@Test
	public void testGetLocationList() {
		assertNotNull(zone.getLocationList());
	}

	@Test
	public void testAddLocation() {
		assertFalse(zone.addLocation(quadLocation));
	}

	@Test
	public void testGetLength() {
		assertNotNull(zone.getLength());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(zone.isEmpty());
	}

	
	@Test
	public void testgetMinLattitudeValue(){
		assertNotNull(zone.getMinLattitudeValue());
	}
	
	@Test
	public void testgetMinLongitudeValue(){
		assertNotNull(zone.getMinLongitudeValue());
	}
	
	@Test
	public void testgetMaxLattitudeValue(){
		assertNotNull(zone.getMaxLattitudeValue());
	}
	
	@Test
	public void getMaxLongitudeValue(){
		assertNotNull(zone.getMaxLongitudeValue());
	}
	
	@Test
	public void testSetLocationList(){
	zone.setLocationList(locations);
	assertNotNull(zone.getLocationList());
	}
	
}
