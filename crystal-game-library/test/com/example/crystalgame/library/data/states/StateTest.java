/**
 * 
 */
package com.example.crystalgame.library.data.states;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Location;

/**
 * @author laptop
 *
 */
public class StateTest {

	private Location loc ;
	private LocationStateValue locStateValue;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		loc = new Location(1,2);
		locStateValue = LocationStateValue.IN_RANGE;
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.data.states.State#State(com.example.crystalgame.library.data.states.StateType, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testState() {
		assertNotNull(new State(StateType.LOCATION_STATE, loc, locStateValue){});
	}

}
