/**
 * 
 */
package com.example.crystalgame.library.data.states;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author laptop
 *
 */
public class LocationStateValueTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(LocationStateValue.IN_RANGE);
		assertNotNull(LocationStateValue.OUT_OF_RANGE);
	}

}
