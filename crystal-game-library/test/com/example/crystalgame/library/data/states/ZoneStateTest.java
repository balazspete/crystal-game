/**
 * 
 */
package com.example.crystalgame.library.data.states;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;

/**
 * @author laptop
 *
 */
public class ZoneStateTest {

	Zone zone;
	private Location loc ;
	private LocationStateValue locStateValue;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		zone = new Zone();
		loc = new Location(1,2);
		locStateValue = LocationStateValue.IN_RANGE;
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.data.states.ZoneState#ZoneState(com.example.crystalgame.library.data.Zone, java.lang.Boolean)}.
	 */
	@Test
	public void testZoneState() {
		assertNotNull(new ZoneState(zone, true));
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.data.states.State#State(com.example.crystalgame.library.data.states.StateType, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testState() {
		assertNotNull(new State(StateType.LOCATION_STATE, loc, locStateValue){});
	}

}
