package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Artifact.ArtifactType;
import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

public class ItemTest extends ArtifactTest {

	protected Item item;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		radius = 234;
		location = artifact = item = new Item(ArtifactType.CRYSTAL, latitude, longitude) {

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
	public void test() {
		assertNotNull(new Item(ArtifactType.CRYSTAL, latitude, longitude){

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}});
	}

}
