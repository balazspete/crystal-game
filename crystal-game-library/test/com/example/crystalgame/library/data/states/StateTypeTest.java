package com.example.crystalgame.library.data.states;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StateTypeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(StateType.LOCATION_STATE);
		assertNotNull(StateType.ZONE_STATE);
	}

}
