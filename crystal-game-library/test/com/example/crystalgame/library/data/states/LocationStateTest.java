package com.example.crystalgame.library.data.states;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Location;

public class LocationStateTest extends StateTest{
	
	LocationState locationState;
	Location location;
	@Before
	public void setUp() throws Exception {
		location = new Location(1, 2);
		
	}

	@Test
	public void testGetInRangeState() {
		assertNotNull(LocationState.getInRangeState(location));
	}

	@Test
	public void testGetOutOfRangeState() {
		assertNotNull(LocationState.getOutOfRangeState(location));
	}

}
