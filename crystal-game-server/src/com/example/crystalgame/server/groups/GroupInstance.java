package com.example.crystalgame.server.groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.game.GameManager;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.server.sequencer.Sequencer;

/**
 * An object handling a group instance
 * @author Balazs Pete, Shen Chen
 *
 */
public class GroupInstance implements Runnable {

	public final Group group;
	
	private boolean running = true;
	private Sequencer sequencer;
	private boolean inGame = false;
	private DateTime lastGameStartRequestTime = DateTime.now().minusMinutes(1);
	
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
					handleInstruction((Instruction) event.getMessage().getData());					
				}
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
	
	private void handleInstruction(Instruction instruction) {
		switch(instruction.type) {
			case GAME_INSTRUCTION:
			handleGameInstruction((GameInstruction) instruction);
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
		}
	}
	
}
