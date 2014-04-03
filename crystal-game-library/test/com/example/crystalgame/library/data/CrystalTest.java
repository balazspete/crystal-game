package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CrystalTest extends ItemTest {

	protected Crystal crystal;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		radius = 234;
		location = artifact = item = crystal = new Crystal(latitude, longitude);
	}
	
	@Test
	public void testSetZoneID() {
		//assertNull(crystal.getZoneId());
		String id = "wefwefwef";
		crystal.setZoneID(id);
		assertNotNull(crystal.getZoneId());
		assertEquals(id, crystal.getZoneId());
	}

}
