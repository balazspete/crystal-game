package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class ThroneRoomTest extends ZoneTest {

	
	@Test
	public void test() {
		Location location =new Location(1, 2);
		assertNotNull(new ThroneRoom(location));
	}

}
