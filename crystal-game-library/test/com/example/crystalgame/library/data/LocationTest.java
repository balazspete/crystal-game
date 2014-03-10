package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LocationTest {

	protected Location location;
	protected double latitude, longitude;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		location = new Location(latitude, longitude);
	}

	@Test
	public void testGetLatitude() {
		assertNotNull(location.getLatitude());
		assertTrue(latitude == location.getLatitude());
	}

	@Test
	public void testSetLatitude() {
		latitude = 324;
		location.setLatitude(latitude);
		assertTrue(latitude == location.getLatitude());
	}

	@Test
	public void testGetLongitude() {
		assertNotNull(location.getLongitude());
		assertTrue(longitude == location.getLongitude());
	}

	@Test
	public void testSetLongitude() {
		longitude = 322;
		location.setLongitude(longitude);
		assertTrue(longitude == location.getLongitude());
	}

	@Test
	public void testToString() {
		assertNotNull(location.toString());
		assertEquals(latitude+":"+longitude, location.toString());
	}

	@Test
	public void testMarkerID() {
		assertNull(location.getMarkerID());
		String marker = "fwefwefwefwef";
		location.setMarkerID(marker);
		assertEquals(marker, location.getMarkerID());
	}

	@Test
	public void testGetID() {
		assertNotNull(location.getID());
		assertFalse(location.getID().isEmpty());
	}
	
}
