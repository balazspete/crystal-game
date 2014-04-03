package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InformationTest {

	private Information info;
	private String key, value;
	private static final String PREKEY = "com.example.crystalgame.library.data.information.";
	@Before
	public void setup() {
		key = "THEKEY";
		value = "THEVALUE";
		info = new Information(key, value);
	}
	
	@Test
	public void testGetID() {
		assertNotNull(info.getID());
		assertEquals(PREKEY+key, info.getID());
	}

	@Test
	public void testGetValue() {
		assertNotNull(info.getValue());
		assertEquals(value, info.getValue());
	}

}
