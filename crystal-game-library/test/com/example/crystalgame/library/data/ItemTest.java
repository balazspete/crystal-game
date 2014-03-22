package com.example.crystalgame.library.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.Character.PlayerType;
import com.example.crystalgame.library.data.Item.ItemType;

public class ItemTest extends ArtifactTest {

	protected Item item;
	
	@Before
	public void setup() {
		latitude = 345;
		longitude = 523;
		radius = 234;
		location = artifact = item = new Item(latitude, longitude, ItemType.CRYSTAL) {
		};
	}
	
	@Test
	public void test() {
		assertNotNull(new Item(latitude, longitude, ItemType.CRYSTAL){});
	}

}
