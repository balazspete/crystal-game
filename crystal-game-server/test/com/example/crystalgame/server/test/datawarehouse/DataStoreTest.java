package com.example.crystalgame.server.test.datawarehouse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.util.RandomID;
import com.example.crystalgame.server.datawarehouse.DataStore;

public class DataStoreTest {

	private DataStore ds;
	
	@Before
	public void setup() {
		ds = DataStore.getGameStore(RandomID.getRandomId());
	}

	@Test
	public void testGetGameStore() {
		assertNotNull("Retrieved game DataStore should not be null", DataStore.getGameStore("groupID"));
	}

	@Test
	public void testPut() {
		StringWithID obj = new StringWithID("test test");
		assertNull("Data Warehouse 1 should be empty", ds.get(StringWithID.class, obj.getID()));
		assertTrue("Put should be successful", ds.put(StringWithID.class, obj));
	}

	@Test
	public void testGet() {
		StringWithID obj = new StringWithID("test test");
		ds.put(StringWithID.class, obj);
		HasID _obj = ds.get(StringWithID.class, obj.getID());
		assertNotNull("Data Warehouse 1 should not retur null", _obj);
		assertEquals("Returned object should have same ID", obj.getID(), _obj.getID());
		assertEquals("Returned object should have same value", obj.getValue(), ((StringWithID) _obj).getValue());
	}

	@Test
	public void testGetAll() {
		StringWithID obj = new StringWithID("test test");
		StringWithID obj2 = new StringWithID("test test");
		
		ds.put(StringWithID.class, obj);
		ds.put(StringWithID.class, obj2);
		
		System.out.println(ds.getAll(StringWithID.class).size());
		assertEquals(2, ds.getAll(StringWithID.class).size());
	}

	@Test
	public void testDelete() {
		StringWithID obj = new StringWithID("test test");
		
		ds.put(StringWithID.class, obj);
		assertTrue("Data with key should not exist", ds.delete(StringWithID.class, obj.getID()));
	}

}
