package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

public class WarriorTest extends CharacterTest {

	@Test
	public void test() {
		Character sage = new Character(12, 12, CharacterType.SAGE,PlayerType.PLAYER ,"1234" ) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}
		};
		assertNotNull(new Warrior(sage,1,2));
	}
	

}
