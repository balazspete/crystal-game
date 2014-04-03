package com.example.crystalgame.server.test.game;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.server.game.CharacterManager;
import com.example.crystalgame.server.groups.Client;
import com.example.crystalgame.server.sequencer.Sequencer;

public class CharacterManagerTest
{

	private CharacterManager characterManager;
	private Sequencer sequencer;
	
	private GameLocation gameLocation = new GameLocation();
	@Before
	public void createCharacterManager()
	{
		List<Location> locations = new LinkedList<Location>();
		locations.add(new Location(1, 2));
		locations.add(new Location(1, 3));
		locations.add(new Location(2, 3));
		locations.add(new Location(2, 2));
		for(Location location : locations) 
		{
			if(location != null)
			{
				this.gameLocation.addLocation(location);
			}
		}
		Client client  = new Client("1222","chen");
		throw new UnsupportedOperationException();
//		Group group = new Group("Chen", client);
//		this.sequencer = new Sequencer(group);
//		characterManager = new CharacterManager(gameLocation, sequencer);
		
		
	}
	
	@Test
	public void testCharacterManager() 
	{
		assertNotNull(characterManager);
	}

	
	@Test
	public void testGetCharacters() 
	{
		assertNotNull(characterManager.getCharacters());
	}
	
	@Test
	public void testInGameLocation()
	{
		//assertTrue(characterManager.inGameLocation());
	}
	
	@Test
	public void testDisqualifyEnergy()
	{
		//assertTrue(characterManager.disqualifyEnergy());
	}

}
