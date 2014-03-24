/**
 * 
 */
package com.example.crystalgame.library.datawarehouse;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Wizard;

/**
 * @author home
 *
 */
public class CollectionStoreTest<DATA extends HasID> {

	private DataWarehouse warehouse;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.datawarehouse.CollectionStore#CollectionStore(com.example.crystalgame.library.datawarehouse.DataWarehouse, java.lang.Class)}.
	 */
	@Test
	public void testCollectionStore() {
		assertNotNull(new CollectionStore<Wizard>(warehouse, Wizard.class)); 
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.datawarehouse.CollectionStore#getList()}.
	 */
	@Test
	public void testGetList() {
		fail("Not yet implemented");
		
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.datawarehouse.CollectionStore#putList(java.lang.Class, java.util.List)}.
	 */
	@Test
	public void testPutList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.datawarehouse.CollectionStore#put(com.example.crystalgame.library.data.HasID)}.
	 */
	@Test
	public void testPut() {
		fail("Not yet implemented");
	}

}
