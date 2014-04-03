package com.example.crystalgame.library.datawarehouse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.StringWithID;

public class DataWrapperTest {
	
	StringWithID str;
	String c;
	DataWrapper<HasID> dw;

	@Before
	public void setup() {
		str = new StringWithID("string");
		c = StringWithID.class.toString();
		dw = new DataWrapper<HasID>(c, str);
	}
	
	@Test
	public void testGetValue() {
		assertNotNull(dw);
		assertNotNull(dw.getValue());
		assertEquals(str, dw.getValue());
		assertEquals(str, dw.getValue());
	}

	@Test
	public void testSetValue() {
		StringWithID str2 = new StringWithID("string");
		try {
			dw.setValue(str2);

			
			fail("We should fail here, as objects have different IDs");
		} catch (DataWarehouseException e) {
			// Normal behaviour
		}	

		assertNotNull(dw.getValue());
		
		String val = "hello";
		str.setValue(val);
		assertEquals(str, dw.getValue());
		assertEquals(val, ((StringWithID)dw.getValue()).getValue());
	}

	@Test
	public void testGetVersion() {
		assertTrue(dw.getVersion() >= 0);
	}

	@Test
	public void testGetType() {
		assertNotNull(dw.getType());
		assertEquals(c, dw.getType());
	}

}
