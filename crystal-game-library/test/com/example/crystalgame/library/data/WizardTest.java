package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;

public class WizardTest extends CharacterTest {

	@Test
	public void test() {
		assertNotNull(Wizard.create(new Character(latitude, longitude, CharacterType.WIZARD, PlayerType.PLAYER, "ClientID1") {

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}}, latitude, longitude));
		assertNotNull(Wizard.create(new Character(latitude, longitude, CharacterType.WIZARD, PlayerType.NPC, "ClientID2") {

			@Override
			public double getVisibilityRange() {
				return 0;
			}

			@Override
			public double getInteractionRange() {
				return 0;
			}}, latitude, longitude));
	}

}
