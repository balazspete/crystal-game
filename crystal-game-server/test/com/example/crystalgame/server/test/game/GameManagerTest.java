package com.example.crystalgame.server.test.game;

import static org.junit.Assert.*;

import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.server.datawarehouse.ServerDataWarehouse;
import com.example.crystalgame.server.game.GameManager;
import com.example.crystalgame.server.test.datawarehouse.ServerDataWarehouseTest;

import java.util.List;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;


public class GameManagerTest {

	private GameManager manager;
	private String[] clients = new String[]{ "1", "2", "3" };
	
	@Before
	public void createGameManager() {
		List<String> clientIds = new LinkedList<String>();
		for(String s : clients) {
			clientIds.add(s);
		}
		
		List<Location> locations = new LinkedList<Location>();
		locations.add(new Location(1, 2));
		locations.add(new Location(1, 3));
		locations.add(new Location(2, 3));
		locations.add(new Location(2, 2));
		
		throw new UnsupportedOperationException();
//		manager = new GameManager(new ServerDataWarehouse(), "NAME", clientIds, locations);
	}
	
	
	@Test
	public void testGameManager() {
		assertNotNull(manager);
	}

	@Test
	public void testRun() {
		Thread t = new Thread(manager);
		t.start();
		assertTrue("Thread has started", t.isAlive());
	}

	@Test
	public void testStopGame() {
		Thread t = new Thread(manager);
		t.start();
		manager.stopGame();
		assertFalse("Thread has stopped", t.isAlive());
	}
	
//	@Test
//	public void testRemoveClientFromGame() {
//		assertTrue("Client removed from game", manager.removeClientFromGame("2"));
//		assertTrue("Nonexisting client removal handled", manager.removeClientFromGame("2"));
//	}
	
	@Test
	public void testGetClientsInGame() {
		assertArrayEquals(manager.getClientsInGame().toArray(), clients);
	}
	
	@Test
	public void testGetName() {
		assertEquals("NAME", manager.getName());
	}

}
