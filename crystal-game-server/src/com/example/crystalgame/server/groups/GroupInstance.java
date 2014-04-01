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
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Information;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.data.Artifact.ArtifactType;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.CommunicationStatusInstruction;
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
	
	private GameManager currentGame;
	
	private ListenerManager<InstructionEventListener, InstructionEvent> instructionEventListenerManager;
	
	/**
	 * Create a new group instance
	 * @param group The corresponding group
	 */
	public GroupInstance(Group group) {
		this.group = group;
		
		sequencer = new Sequencer(group);
		dataWarehouse = ServerDataWarehouse.getWarehouseForGroup(group);
		dataWarehouse.addInstructionEventListener(new InstructionEventListener() {
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {
				InstructionRelayMessage message = new InstructionRelayMessage(null);
				message.setData(event.getInstruction());
				sequencer.sendMessageToAll(message);
			}

			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) {
				handleCommunicationStatusInstruction((CommunicationStatusInstruction) event.getInstruction());
			}
			
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			@Override
			public void onGroupInstruction(InstructionEvent event) {}
			@Override
			public void onGameInstruction(InstructionEvent event) {}
			@Override
			public void onDataTransferInstruction(InstructionEvent event) {}
			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) { }
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
		
	}

	@Override
	public void run() {
		sleep(1000);
		setup();
		
		// TODO: do group stuff here
		while(running) {
			if (!inGame || currentGame == null) {
				sleep(1000);
				continue;
			}
			
			currentGame.run();
		}
	}
	
	/**
	 * Stop the instance, terminating execution
	 */
	public void stopInstance() {
		running = false;
		// TODO: Stop child threads here
	}
	
	private void setup() {
		try {
			// Save the group related information
			List<HasID> information = new ArrayList<HasID>();
			information.add(new Information(Information.GROUP_NAME, group.getName()));
			information.add(new Information(Information.GROUP_MAX_PLAYERS, group.getMaxPlayers()));
			
//			dataWarehouse.putList(Information.class, information);
			dataWarehouse.put(GameBoundary.class, group.getGameBoundary());
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * Remove a client from the game
	 * @param clientID The ID of the client
	 * @return True if removed
	 */
	public synchronized void removeClientFromGame(String clientID) {
		if (currentGame == null) {
			return;
		}
		
		currentGame.removeClientFromGame(clientID);
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
			case OUT_OF_ENERGY :
				handlePlayerOutOfEnergyRequest(instruction.arguments);
				break;
			default:
				System.err.println("Unhandled GameInstruction: " + instruction.gameInstruction);
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
			System.err.println("Starting the game!");
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
			
			ThroneRoom throneRoom = new ThroneRoom((Location) data[5]);
			
			int gameTime = (Integer) data[6];
			DateTime endTime = DateTime.now().plusMinutes(gameTime);

			inGame = true;
			currentGame = new GameManager(dataWarehouse, sequencer, gameName, clientIDs, locations, throneRoom, endTime);
		}	
	}
	
	/**
	 * The crystal is checked to see if it is available in the common pool. If yes,
	 * then it is associated with the player and removed from the pool.
	 * 
	 * @param data Player ID, Crystal ID
	 */
	private synchronized void handleCaptureCrystalRequest(Serializable[] data) {
		if (currentGame == null) {
			return;
		}
		
		currentGame.handleItemCaptureRequest(ArtifactType.CRYSTAL, data);
	}
	
	/**
	 * The magical item is checked to see if it is available in the common pool. If yes,
	 * then it is associated with the player and removed from the pool.
	 * 
	 * @param data Player ID, Magical Item ID
	 */
	private synchronized void handleCaptureMagicalItemRequest(Serializable[] data) {
		if (currentGame == null) {
			return;
		}
		
		currentGame.handleItemCaptureRequest(ArtifactType.MAGICAL_ITEM, data);
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
	
	public void sleep(int millisecs) {
		boolean proceed = false;
		while (!proceed){
			synchronized(this) {
				try {
					wait(millisecs);
					proceed = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void handleCommunicationStatusInstruction(CommunicationStatusInstruction instruction) {
		System.err.println("GroupInstance:465 - Unhandled communication status instruction");
	}

	private void handlePlayerOutOfEnergyRequest(Serializable[] args) {
		String playerID = (String)args[0];
		String clientID = (String)args[1];

		if(currentGame != null) {
			currentGame.removeClientFromGame(clientID);
			InstructionRelayMessage message = new InstructionRelayMessage(clientID);
			message.setData(GameInstruction.createEnergyDisqualifyInstruction());
			sequencer.sendMessageToOne(message);
		}

	}
	
}
