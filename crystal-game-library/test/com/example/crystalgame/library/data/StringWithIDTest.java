package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringWithIDTest {

	private String val;
	protected StringWithID str;
	
	@Before
	public void setup() {
		val = "fwefwef";
		str = new StringWithID(val);
	}
	
	@Test
	public void testGetValue() {
		assertNotNull(str.getValue());
		assertEquals(val, str.getValue());
	}

	@Test
	public void testSetValue() {
		String val2 = "wfegrjfngdk";
		str.setValue(val2);
		assertEquals(val2, str.getValue());
	}

	@Test
	public void testGetID() {
		assertNotNull(str.getID());
		assertFalse(str.getID().isEmpty());
	}

}
