package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

public class CharacterTest extends ArtifactTest {

	protected Character character;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		radius = 234;
		location = artifact = character = new Character(latitude, longitude, CharacterType.SAGE, PlayerType.PLAYER, "C343") {

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
		assertNotNull(new Character(latitude, longitude, CharacterType.SAGE, PlayerType.PLAYER, "C343"){

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}} );
	}

}
