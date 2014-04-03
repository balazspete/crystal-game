package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArtifactTest extends LocationTest {

	protected double radius;
	protected Artifact artifact;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		radius = 234;
		location = artifact = new Artifact(null, latitude, longitude, radius) {
			@Override
			public double getVisibilityRange() {
				return 0;
			}
			@Override
			public double getInteractionRange() {
				return 0;
			}
		};
	}
	
	@Test
	public void testRadius() {
		//assertTrue(radius == artifact.getRadius());
		double r = 3234;
		artifact.setRadius(r);
		assertTrue(r == artifact.getRadius());
	}
	
	@Test
	public void testToString(){
		// No tostring override
	}

}
