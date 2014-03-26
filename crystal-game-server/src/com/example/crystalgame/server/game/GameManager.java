package com.example.crystalgame.server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Item;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Character.UnknownPlayerCharacter;
import com.example.crystalgame.library.data.Item.ItemType;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.server.sequencer.Sequencer;

public class GameManager implements Runnable {

	private DataWarehouse dataWarehouse;
	private Sequencer sequencer;
	
	private final String name;
	private List<String> clientIDs;
	private HashMap<String, String> clientIDtoCharacterID;
	private GameLocation gameLocation;
	private ThroneRoom throneRoom;
	private DateTime endTime;
	
	private volatile boolean running = true; 
	private ListenerManager<InstructionEventListener, InstructionEvent> manager;

	public GameManager(DataWarehouse dw, Sequencer sequencer, String gameName, List<String> clientIDs, List<Location> locations, ThroneRoom throneRoom, DateTime endTime) {
		this.dataWarehouse = dw;
		this.sequencer = sequencer;
		
		this.name = gameName;
		this.clientIDs = clientIDs;
		this.gameLocation = new GameLocation();
		clientIDtoCharacterID = new HashMap<String, String>();
		this.throneRoom = throneRoom;
		
		for(Location location : locations) {
			this.gameLocation.addLocation(location);
		}
		
		this.endTime = endTime;
		
	}

	@Override
	public void run() {
		saveGameLocation();
		createCharacters();
		createCrystals();
		createMagicalItems();
		sendGameStartSignal();
		
		while(running) {
			synchronized(this) {
				try {
					System.out.println("Infinite looping at GameManager");
					wait(2000);
				} catch (InterruptedException e) {
				}
			}
			
			if (isOutOfTime()) {
				stopGame();
			}
		}
	}
	
	public boolean isOutOfTime() {
		return DateTime.now().isAfter(endTime);
	}
	
	public void stopGame() {
		running = false;
		sendOutOfTimeSignal();
		// TODO: stop child threads here
	}
	
	public void relayInstruction(Instruction instruction) {
		// TODO: relay instruction
	}
	
	public void addInstructionEventListener(InstructionEventListener listener) {
		manager.addEventListener(listener);
	}
	
	public void removeInstructionEventListener(InstructionEventListener listener) {
		manager.removeEventListener(listener);
	}
	
	public boolean removeClientFromGame(String clientId) {
		// TODO: remove client from all child components
		
		try {
			HasID c = dataWarehouse.get(Character.class, clientIDtoCharacterID.get(clientId));
			if (c != null) {
				Character character = (Character) c;
				List<HasID> zones = dataWarehouse.getList(CrystalZone.class);
				if (zones.size() > 0) {
					List<Item> items = ItemScatter.generate(Crystal.class, (CrystalZone) zones.get(0), character.getCrystals().size());
					dataWarehouse.putList(Crystal.class, new ArrayList<HasID>(items));
				}
				
				List<HasID> items = new ArrayList<HasID>();
				for (MagicalItem item : character.getMagicalItems()) {
					ItemScatter.position(item, gameLocation);
					items.add(item);
				}
				
				dataWarehouse.putList(MagicalItem.class, items);
				dataWarehouse.delete(Character.class, clientIDtoCharacterID.get(clientId));
			}
		} catch (DataWarehouseException e) {
			System.err.println("Failed to rescatter items of player. ClientID=" + clientId);
		}
		
		clientIDs.remove(clientId);
		System.out.println(Arrays.toString(clientIDs.toArray()));
		if (clientIDs.size() == 0) {
			stopGame();
		}
		return !clientIDs.contains(clientId);
	}
	
	public List<String> getClientsInGame() {
		return clientIDs;
	}
	
	public String getName() {
		return name;
	}
	
	public GameLocation getLocations(){
		return gameLocation;
	}
	
	public void createCrystals() {
		CrystalZoneScatter crystalZoneScatter = new CrystalZoneScatter(gameLocation, 5);
		List<CrystalZone> zones = crystalZoneScatter.generateCrystalZones(3);
		try {
			dataWarehouse.putList(CrystalZone.class, new ArrayList<HasID>(zones));
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
		
		ArrayList<HasID> crystals = new ArrayList<HasID>();
		for (CrystalZone zone : zones) {
			List<Item> _crystals = ItemScatter.generate(Crystal.class, zone, (int) (Math.random() * 10));
			for (Item c : _crystals) {
				crystals.add(c);
			}
		}

		try {
			dataWarehouse.putList(Crystal.class, new ArrayList<HasID>(crystals));
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
	}
	
	public void createMagicalItems() {
		List<Item> items = ItemScatter.generate(MagicalItem.class, gameLocation, 6);
		
		try {
			dataWarehouse.putList(MagicalItem.class, new ArrayList<HasID>(items));
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
	}
 	
	public void createCharacters() {
		List<HasID> characters = new ArrayList<HasID>();
		for (String client : clientIDs) {
			characters.add(new UnknownPlayerCharacter(client));
		}
		
		try {
			dataWarehouse.putList(Character.class, characters);
		} catch (DataWarehouseException e) {
			System.err.println("Failed to create characters, retrying...");
			createCharacters();
		}
		
		try {
			List<HasID> chars = dataWarehouse.getList(Character.class);
			System.out.println(chars.size());
		} catch (DataWarehouseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void sendGameStartSignal() {
		InstructionRelayMessage message = new InstructionRelayMessage(null);
		message.setData(GameInstruction.createGameStartedSignalInstruction());
		sequencer.sendMessageToAll(message);
	}
	
	private void sendOutOfTimeSignal() {
		InstructionRelayMessage message = new InstructionRelayMessage(null);
		message.setData(GameInstruction.createGameEndedSignalInstruction());
		sequencer.sendMessageToAll(message);
	}
	
	private void saveGameLocation() {
		try {
			dataWarehouse.put(GameLocation.class, gameLocation);
			dataWarehouse.put(ThroneRoom.class, throneRoom);
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
	}
	
}
