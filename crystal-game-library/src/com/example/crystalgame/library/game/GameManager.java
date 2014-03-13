package com.example.crystalgame.library.game;

import java.util.Arrays;
import java.util.List;

import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.instructions.Instruction;


public class GameManager implements Runnable {

	private final String name;
	private List<String> clientIDs;
	private GameLocation gameLocations;
	
	private volatile boolean running = true; 
	private ListenerManager<InstructionEventListener, InstructionEvent> manager;
	
	public GameManager(String gameName, List<String> clientIDs, List<Location> locations) {
		this.name = gameName;
		this.clientIDs = clientIDs;
		this.gameLocations = new GameLocation();
		for(Location location : locations) 
		{
			if(location != null)
			{
				this.gameLocations.addLocation(location);
			}
		}
	}

	@Override
	public void run() {
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
		return !clientIDs.contains(clientId);
	}
	
	public List<String> getClientsInGame() {
		return clientIDs;
	}
	
	public String getName() {
		return name;
	}
	
	public GameLocation getLocations(){
		return gameLocations;
	}
}
