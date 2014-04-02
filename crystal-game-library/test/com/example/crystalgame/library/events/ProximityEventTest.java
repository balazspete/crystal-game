/**
 * 
 */
package com.example.crystalgame.library.events;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Artifact.ArtifactType;

/**
 * @author Allen Thomas Varghese
 *
 */
public class ProximityEventTest {

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
	 * Test method for {@link com.example.crystalgame.library.events.ProximityEvent#ProximityEvent(com.example.crystalgame.library.events.ProximityEvent.ArtifactType, com.example.crystalgame.library.data.Artifact)}.
	 */
	@Test
	public void testProximityEvent() {
		assertNotNull(new ProximityEvent(ArtifactType.CHARACTER, new Artifact(ArtifactType.CHARACTER, 1,2,3) {

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}}));
		assertNotNull(new ProximityEvent(ArtifactType.CRYSTAL, new Artifact(ArtifactType.CRYSTAL, 1,2,3) {

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}}));
		assertNotNull(new ProximityEvent(ArtifactType.MAGICAL_ITEM, new Artifact(ArtifactType.MAGICAL_ITEM, 1,2,3) {

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}}));
	}

}
