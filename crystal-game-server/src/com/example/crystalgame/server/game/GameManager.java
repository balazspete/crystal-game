package com.example.crystalgame.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Character.UnknownPlayerCharacter;
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
	
	private volatile boolean running = true; 
	private ListenerManager<InstructionEventListener, InstructionEvent> manager;

	public GameManager(DataWarehouse dw, Sequencer sequencer, String gameName, List<String> clientIDs, List<Location> locations, ThroneRoom throneRoom) {
		this.dataWarehouse = dw;
		this.sequencer = sequencer;
		
		this.name = gameName;
		this.clientIDs = clientIDs;
		this.gameLocation = new GameLocation();
		this.throneRoom = throneRoom;
		
		for(Location location : locations) {
			this.gameLocation.addLocation(location);
		}
		
	}

	@Override
	public void run() {
		saveGameLocation();
		createCharacters();
		createCrystals();
		sendGameStartSignal();
		
		while(running) {
			synchronized(this) {
				try {
					System.out.println("Infinite looping at GameManager");
					wait(2000);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public void stopGame() {
		running = false;
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
			dataWarehouse.blockingPutList(CrystalZone.class, new ArrayList<HasID>(zones));
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
		
		ArrayList<Crystal> crystals = new ArrayList<Crystal>();
		for (CrystalZone zone : zones) {
			CrystalScatter scatter = new CrystalScatter(zone);
			List<Crystal> _crystals = scatter.generateCrystals((int) (Math.random() * 10));
			crystals.addAll(_crystals);
		}

		try {
			dataWarehouse.blockingPutList(Crystal.class, new ArrayList<HasID>(crystals));
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
			dataWarehouse.blockingPutList(Character.class, characters);
		} catch (DataWarehouseException e) {
			System.err.println("Failed to create characters, retrying...");
			createCharacters();
		}
	}
	
	private void sendGameStartSignal() {
		InstructionRelayMessage message = new InstructionRelayMessage(null);
		message.setData(GameInstruction.createGameStartedSignalInstruction());
		sequencer.sendMessageToAll(message);
	}
	
	private void saveGameLocation() {
		try {
			dataWarehouse.blockingPut(GameLocation.class, gameLocation);
			dataWarehouse.blockingPut(ThroneRoom.class, throneRoom);
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
	}
	
}
