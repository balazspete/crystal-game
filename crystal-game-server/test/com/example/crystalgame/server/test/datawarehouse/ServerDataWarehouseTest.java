package com.example.crystalgame.server.test.datawarehouse;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.util.RandomID;
import com.example.crystalgame.server.datawarehouse.ServerDataWarehouse;

public class ServerDataWarehouseTest {
	
	private ServerDataWarehouse dw1;
	
	public ServerDataWarehouseTest() {
		
	}
//	
//	@Before
//	public void setup() {
//		String randomStamp = RandomID.getRandomId();
//		dw1 = ServerDataWarehouse.getWarehouseForGroup("group1" + randomStamp, new ArrayList<String>());
//	}
//	
//	@Test
//	public void testPut() {
//		StringWithID obj = new StringWithID("test test");
//		try {
//			assertNull("Data Warehouse 1 should be empty", dw1.get(StringWithID.class, obj.getID()));
//			assertTrue("Put should be successful", dw1.put(StringWithID.class, obj));
//		} catch (DataWarehouseException e) {
//			fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testGet() {
//		StringWithID obj = new StringWithID("test test");
//		try {
//			dw1.put(StringWithID.class, obj);
//			HasID _obj = dw1.get(StringWithID.class, obj.getID());
//			
//			assertNotNull("Data Warehouse 1 should not retur null", _obj);
//			assertEquals("Returned object should have same ID", obj.getID(), _obj.getID());
//			assertEquals("Returned object should have same value", obj.getValue(), ((StringWithID) _obj).getValue());
//		} catch (DataWarehouseException e) {
//			fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testDelete() {
//		StringWithID obj = new StringWithID("test test");
//		
//		try {
//			dw1.put(StringWithID.class, obj);
//			assertTrue("Data with key should not exist", dw1.delete(StringWithID.class, obj.getID()));
//		} catch (DataWarehouseException e) {
//			fail(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testGetAll() {
//		StringWithID obj = new StringWithID("test test");
//		StringWithID obj2 = new StringWithID("test test");
//		
//		try {
//			dw1.put(StringWithID.class, obj);
//			dw1.put(StringWithID.class, obj2);
//			
//			assertEquals(2, dw1.getAll(StringWithID.class).size());
//		} catch (DataWarehouseException e) {
//			fail(e.getMessage());
//		}
//	}

}
