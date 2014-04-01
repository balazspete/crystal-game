package com.example.crystalgame.server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.db4o.foundation.BlockingQueue;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.data.Artifact.ArtifactType;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Item;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Character.UnknownPlayerCharacter;
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
	private GameLocation gameLocation;
	private ThroneRoom throneRoom;
	private DateTime endTime;
	
	private BlockingQueue<Runnable> instructionQueue;
	
	private volatile boolean running = true; 
	private ListenerManager<InstructionEventListener, InstructionEvent> manager;

	public GameManager(DataWarehouse dw, Sequencer sequencer, String gameName, List<String> clientIDs, List<Location> locations, ThroneRoom throneRoom, DateTime endTime) {
		this.dataWarehouse = dw;
		this.sequencer = sequencer;
		this.instructionQueue = new BlockingQueue<Runnable>();
		
		this.name = gameName;
		this.clientIDs = clientIDs;
		this.gameLocation = new GameLocation();
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
			Runnable task = instructionQueue.next(200);
			if (task != null) {
				task.run();
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
	
	private Character getClientsCharacter(String clientID) {
		if (clientID == null) {
			return null;
		}
		
		try {
			List<HasID> characters = dataWarehouse.getList(Character.class);
			for (HasID e : characters) {
				Character character = (Character) e;
				if (clientID.equals(character.getClientId())) {
					return character;
				}
			}
		} catch (DataWarehouseException e) {
			// Handled by returning null
		}
		
		return null;
	}
	
	public void removeClientFromGame(final String clientId) {
		// TODO: remove client from all child components
		instructionQueue.add(new Runnable() {
			public void run() {
				try {
					Character character = getClientsCharacter(clientId);
					if (character == null) {
						return;
					}
					
					List<HasID> zones = dataWarehouse.getList(CrystalZone.class);
					List<Crystal> crystals = character.getCrystals();
							
					if (zones != null && zones.size() > 0 && crystals != null && crystals.size() > 0) {
						List<Item> items = ItemScatter.generate(ArtifactType.CRYSTAL, (CrystalZone) zones.get(0), character.getCrystals().size());
						dataWarehouse.putList(Crystal.class, new ArrayList<HasID>(items));
					}
					
					List<HasID> items = new ArrayList<HasID>();
					List<MagicalItem> magicalItems = character.getMagicalItems();
					
					if (magicalItems != null) {
						for (MagicalItem item : magicalItems) {
							ItemScatter.position(item, gameLocation);
							items.add(item);
						}
						
						dataWarehouse.putList(MagicalItem.class, items);
					}
					
					dataWarehouse.delete(Character.class, character.getID());
				} catch (DataWarehouseException e) {
					// No need to do anything
				}
			}
		});
			
		clientIDs.remove(clientId);
		System.out.println(Arrays.toString(clientIDs.toArray()));
		if (clientIDs.size() == 0) {
			stopGame();
		}
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
			List<Item> _crystals = ItemScatter.generate(ArtifactType.CRYSTAL, zone, (int) (Math.random() * 10));
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
		List<Item> items = ItemScatter.generate(ArtifactType.MAGICAL_ITEM, gameLocation, 6);
		
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
	
	public synchronized void handleItemCaptureRequest(final ArtifactType type, final Serializable[] data) {
		final String clientID = (String) data[0];
		final String characterID = (String) data[1];
		final String itemID = (String) data[2];
		
		if (clientID == null || characterID == null || itemID == null) {
			System.err.println("GameManager|handleCaptureRequest: Null input value(s) ClientID=" + clientID + " CharacterID=" + characterID + " itemID=" + itemID);
			return;
		}
		
		instructionQueue.add(new Runnable(){
			public void run() {
				try {
					System.out.println("GameManager|handleItemCaptureRequest: Item campure request Type=" + type + " ClientID=" + clientID);
					
					HasID playerObj = dataWarehouse.get(Character.class, characterID);
					if (playerObj == null) {
						System.err.println("GameManager|handleItemCaptureRequest: Could not retrieve character. CharacterID=" + characterID);
						return;
					}
					
					Character character = (Character) playerObj;
					
					HasID item;
					switch (type) {
						case CRYSTAL:
							item = dataWarehouse.get(Crystal.class, itemID);
							if (item == null) {
								return;
							}
							
							Crystal crystal = (Crystal) item;
							character.addCrystal(crystal);
							
							// Delete the crystal from the common pool
							dataWarehouse.delete(Crystal.class, crystal.getID());
							break;
						case MAGICAL_ITEM:
							item = dataWarehouse.get(Crystal.class, itemID);
							if (item == null) {
								return;
							}
							
							MagicalItem magicalItem = (MagicalItem) item;
							character.addMagicalItem(magicalItem);
							
							// Delete the magical item from the common pool
							dataWarehouse.delete(MagicalItem.class, magicalItem.getID());
							break;
					}
					
					System.out.println("GameManager|handleItemCaptureRequest: Added item to character. Type=" + type + " Character=" + character.getID());
					
					// Add the updated player back to the data warehouse
					dataWarehouse.put(Character.class, character);
				} catch (DataWarehouseException e) {
					// Ignored. No need to reply to client (Another client might have removed the crystal)
					System.out.println("GameManager|handleItemCaptureRequest: Failed to add item to character. Type=" + type + " Client=" + clientID);
				}
			}
		});
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
