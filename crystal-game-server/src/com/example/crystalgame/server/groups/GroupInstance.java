package com.example.crystalgame.server.groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Character.UnknownPlayerCharacter;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Information;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.instructions.DataTransferInstruction;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.server.datawarehouse.ServerDataWarehouse;
import com.example.crystalgame.server.game.GameManager;
import com.example.crystalgame.server.sequencer.Sequencer;

/**
 * An object handling a group instance
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class GroupInstance implements Runnable {

	public final Group group;
	
	private boolean running = true;
	private Sequencer sequencer;
	private boolean inGame = false;
	private DateTime lastGameStartRequestTime = DateTime.now().minusMinutes(1);
	private ServerDataWarehouse dataWarehouse;
	
	private ArrayBlockingQueue<GameManager> managerLock;
	
	private ListenerManager<InstructionEventListener, InstructionEvent> instructionEventListenerManager;
	
	/**
	 * Create a new group instance
	 * @param group The corresponding group
	 */
	public GroupInstance(Group group) {
		this.group = group;
		sequencer = new Sequencer(group);
		this.managerLock = new ArrayBlockingQueue<GameManager>(1);
		dataWarehouse = ServerDataWarehouse.getWarehouseForGroup(group);
		dataWarehouse.addInstructionEventListener(new InstructionEventListener() {
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {
				InstructionRelayMessage message = new InstructionRelayMessage(null);
				message.setData(event.getInstruction());
				sequencer.sendMessageToAll(message);
			}
			
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			@Override
			public void onGroupInstruction(InstructionEvent event) {}
			@Override
			public void onGameInstruction(InstructionEvent event) {}
			@Override
			public void onDataTransferInstruction(InstructionEvent event) {}
		});
		
		// Add a sequencer event listener for local events
		sequencer.addSequencerEventListener(new MessageEventListener(){
			private boolean isMessageForServer(MessageEvent event) {
				return event.getReceiverId().equals("SERVER");
			}
			
			@Override
			public void onMessageEvent(MessageEvent event) {
				if (isMessageForServer(event)) {
					// TODO
				}
			}

			@Override
			public void onGroupStatusMessageEvent(MessageEvent event) {
				// Group Status Messages are handled by the group instance manager
			}

			@Override
			public void onControlMessage(MessageEvent event) {
				// Control messages are handled by the group instance manager
			}

			@Override
			public void onInstructionRelayMessage(MessageEvent event) {
				if (isMessageForServer(event)) {
					// TODO
					handleInstruction((Instruction) event.getMessage().getData(), event.getMessage().getSenderId());					
				}
			}

			@Override
			public void onIdMessageEvent(MessageEvent event) {
				// group instance does not care about this...
			}

		});
		
		// Allow components to subscribe to instruction events
		instructionEventListenerManager = new ListenerManager<InstructionEventListener, InstructionEvent>() {
			@Override
			protected void eventHandlerHelper(InstructionEventListener listener, InstructionEvent event) {
				// Use the implementation by the listener
				InstructionEventListener.eventHandlerHelper(listener, event);
			}
		};
		
		try {
			// Save the group related information
			dataWarehouse.putDirect(Information.class, new Information(Information.GROUP_NAME, group.getName()));
			dataWarehouse.putDirect(Information.class, new Information(Information.GROUP_MAX_PLAYERS, group.getMaxPlayers()));
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		System.out.println(group.groupId);
		// TODO: do group stuff here
		while(running) {
			try {
				GameManager manager = managerLock.poll(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				if (manager == null) {
					continue;
				}
				
				manager.run();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Stop the instance, terminating execution
	 */
	public void stopInstance() {
		running = false;
		// TODO: Stop child threads here
	}
	
	/**
	 * Send an instruction to the group
	 * @param instruction The instruction
	 */
	public void sendInstruction(Instruction instruction) {
		instructionEventListenerManager.send(new InstructionEvent(instruction));
	}
	
	/**
	 * Add a sequencer event listener 
	 * @param listener the event listener
	 */
	public void addMessageEventListener(MessageEventListener listener) {
		sequencer.addSequencerEventListener(listener);
	}
	
	/**
	 * Remove a sequencer event listener
	 * @param listener The listener
	 */
	public void removeMessageEventListener(MessageEventListener listener) {
		sequencer.removeSequencerEventListener(listener);
	}
	
	/**
	 * Send a message to all members of the group
	 * @param message The message
	 */
	public void sendMessageToAll(MulticastMessage message) {
		sequencer.sendMessageToAll(message);
	}
	
	/**
	 * Send a message to one member of the group
	 * @param message The message
	 */
	public void sendMessageToOne(UnicastMessage message) {
		sequencer.sendMessageToOne(message);
	}
	
	/**
	 * Add an instruction event listener
	 * @param listener The listener
	 */
	protected void addInstructionEventListener(InstructionEventListener listener) {
		instructionEventListenerManager.addEventListener(listener);
	}
	
	/**
	 * Remove an instruction event listener
	 * @param listener The listener
	 */
	protected void removeInstructionEventListener(InstructionEventListener listener) {
		instructionEventListenerManager.removeEventListener(listener);
	}
	
	private void handleInstruction(Instruction instruction, String sender) {
		switch(instruction.type) {
			case GAME_INSTRUCTION:
				handleGameInstruction((GameInstruction) instruction);
				break;
			case DATA_SYNCRONISATION:
				handleDataSyncInstruction((DataSynchronisationInstruction) instruction);
				break;
			case DATA_TRANSFER:
				handleDataTransferInstruction((DataTransferInstruction) instruction, sender);
				break;
			default:
				// Ignoring other cases, as they will not occur
				break;
		}
	}
	
	private void handleGameInstruction(GameInstruction instruction) {
		switch(instruction.gameInstruction) {
			case START_GAME_REQUEST:
				handleGameStartRequest();
				break;
			case CREATE_GAME:
				handleCreateGame(instruction.arguments);
				break;
			case CAPTURE_CRYSTAL_REQUEST:
				handleCaptureCrystalRequest(instruction.arguments);
				break;
			case CAPTURE_MAGICAL_ITEM_REQUEST :
				handleCaptureMagicalItemRequest(instruction.arguments);
				break;
			case EXCHANGE_MAGICAL_ITEM :
				handleExchangeMagicalItemsRequest(instruction.arguments);
				break;
			default:
				// TODO: handle other cases
				break;
		}
	}
	
	private synchronized void handleGameStartRequest() {
		if(lastGameStartRequestTime.plusMinutes(1).isBefore(DateTime.now())) {
			lastGameStartRequestTime = DateTime.now();
			int max = group.getClients().size();
			Client client = group.getClients().get((int)(Math.random() * max));
			InstructionRelayMessage message = new InstructionRelayMessage(client.getId());
			message.setData(GameInstruction.createCreateGameRequestGameInstruction());
			message.setReceiverId(client.getId());
			sequencer.sendMessageToOne(message);
		}
	}
	
	private synchronized void handleCreateGame(Serializable[] data) {
		if (!inGame) {
			String gameName = (String) data[0];
			
			List<String> clientIDs = new ArrayList<String>();
			for (Client client : group.getClients()) {
				clientIDs.add(client.getId());
			}
			
			List<Location> locations = new ArrayList<Location>();
			locations.add((Location) data[1]);
			locations.add((Location) data[2]);
			locations.add((Location) data[3]);
			locations.add((Location) data[4]);
			
			GameManager manager = new GameManager(gameName, clientIDs, locations);
		
			new Thread(manager).start();
		
			inGame = true;
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					createCharacters();
					sendGameStartSignal();
				}		
			}).start();
		}	
	}
	
	/**
	 * The crystal is checked to see if it is available in the common pool. If yes,
	 * then it is associated with the player and removed from the pool.
	 * 
	 * @param data Player ID, Crystal ID
	 */
	private synchronized void handleCaptureCrystalRequest(Serializable[] data) {
		String playerID = (String) data[0];
		String crystalID = (String) data[1];
		
		try {
			HasID resultObj = dataWarehouse.get(Crystal.class, crystalID);
			
			// Check if the crystal is available in the crystal pool
			if(null != resultObj) {
				HasID playerObj = dataWarehouse.get(Character.class, playerID);
				
				// If the player exists
				if(null != playerObj) {
					Character player = (Character) playerObj;
					
					// Add the crystal to the player
					player.addCrystal((Crystal)resultObj);
					
					// Delete the crystal from the common pool
					dataWarehouse.delete(Crystal.class, crystalID);
					
					// Add the updated player back to the data warehouse
					dataWarehouse.put(Character.class, player);
				}
			}
		} catch (DataWarehouseException e) {
			// Ignored. No need to reply to client (Another client might have removed the crystal)
		}
	}
	
	/**
	 * The magical item is checked to see if it is available in the common pool. If yes,
	 * then it is associated with the player and removed from the pool.
	 * 
	 * @param data Player ID, Magical Item ID
	 */
	private synchronized void handleCaptureMagicalItemRequest(Serializable[] data) {
		String playerID = (String) data[0];
		String magicalItemID = (String) data[1];
		
		try {
			HasID resultObj = dataWarehouse.get(MagicalItem.class, magicalItemID);
			
			// Check if the magical item is available in the crystal pool
			if(null != resultObj) {
				HasID playerObj = dataWarehouse.get(Character.class, playerID);
				
				// If the player exists
				if(null != playerObj) {
					Character player = (Character) playerObj;
					
					// Add the magical item to the player
					player.addMagicalItem((MagicalItem)resultObj);
					
					// Delete the magical item from the common pool
					dataWarehouse.delete(MagicalItem.class, magicalItemID);
					
					// Add the updated player back to the data warehouse
					dataWarehouse.put(Character.class, player);
				}
			}
		} catch (DataWarehouseException e) {
			// Ignored. No need to reply to client (Another client might have removed the crystal)
		}
	}
	
	/**
	 * Transfer a list of magical items from second player to the first player   
	 * @param data Player1 ID, Player2 ID, List of Magical Item IDs
	 */
	@SuppressWarnings("unchecked")
	private synchronized void handleExchangeMagicalItemsRequest(Serializable[] data) {
		String player1ID = (String) data[0];
		String player2ID = (String) data[1];
		List<MagicalItem> magicalItemList = (ArrayList<MagicalItem>) data[2];
		
		try {
			
			// Fetch player information from the data warehouse
			Character player1 = (Character)dataWarehouse.get(Character.class, player1ID);
			Character player2 = (Character)dataWarehouse.get(Character.class, player2ID);
			
			// If any of the players are not available, return.
			if(null == player1 || null == player2) {
				return;
			}
			
			// Add the magical items to Player 1
			for(MagicalItem magicalItem : magicalItemList) {
				player1.addMagicalItem(magicalItem);
			}
			
			// Remove the magical items from Player 2
			for(MagicalItem magicalItem : magicalItemList) {
				player2.getMagicalItems().remove(magicalItem);
			}
			
			// Add the updated players back to the data warehouse
			dataWarehouse.put(Character.class, player1);
			dataWarehouse.put(Character.class, player2);
		} catch (DataWarehouseException e) {
			// Ignored. No need to reply to client (Another client might have removed the crystal)
		}
	}
	
	private void handleDataSyncInstruction(DataSynchronisationInstruction instruction) {
		dataWarehouse.passInstruction(instruction);
	}
	
	private void handleDataTransferInstruction(DataTransferInstruction instruction, String sender) {
		DataTransferInstruction reply = dataWarehouse.getDownloadReply(instruction);
		InstructionRelayMessage message = new InstructionRelayMessage(sender);
		message.setData(reply);
		sequencer.sendMessageToOne(message);
	}
	
	private void sendGameStartSignal() {
		InstructionRelayMessage message = new InstructionRelayMessage(null);
		message.setData(GameInstruction.createGameStartedSignalInstruction());
		sequencer.sendMessageToAll(message);
	}
 	
	public void createCharacters() {
		List<HasID> characters = new ArrayList<HasID>();
		for (Client client : group.getClients()) {
			characters.add(new UnknownPlayerCharacter(client.getId()));
		}
		
		try {
			dataWarehouse.putList(Character.class, characters);
		} catch (DataWarehouseException e) {
			System.err.println("Failed to create characters, retrying...");
			createCharacters();
		}
	}
	
}
