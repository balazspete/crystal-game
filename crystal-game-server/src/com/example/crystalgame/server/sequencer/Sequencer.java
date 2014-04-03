package com.example.crystalgame.server.sequencer;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.groups.Client;
import com.example.crystalgame.server.groups.Group;

/**
 * The module responsible for message ordering
 * @author Balazs Pete, Shen Chen
 *
 */
public class Sequencer {

	public static String SERVER_ID = "SERVER";
	
	private ListenerManager<MessageEventListener, SequencerEvent> manager;
	
	private long multicastTimestamp = -1;
	private Group group;
	
	private Client server;
	
	/**
	 * Create a sequencer for the specified group
	 * @param group The group
	 */
	public Sequencer(Group group) {
		this.group = group;
		
		// Create a mock client for the server 
		server = new Client(SERVER_ID, SERVER_ID);
		
		// Allow components to subscribe to message events from the Sequencer
		manager = new ListenerManager<MessageEventListener, SequencerEvent>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, SequencerEvent event) {
				MessageEventListener.eventHandlerHelper(listener, event);
			}
		};
	}
	
	/**
	 * Send a message to all members of a group
	 * @param message The message
	 */
	public void sendMessageToAll(Message message) {
		message.setTimeStamp(++multicastTimestamp);
		
		for(Client receiver : group.getClients()) {
			send(receiver, message);
		}
		
		send(server, message);
	}
	
	/**
	 * Send a message to a member of a group
	 * @param message The message
	 */
	public void sendMessageToOne(UnicastMessage message) {
		Client receiver;
		String receiverId = message.getReceiverId();
		if (receiverId.equalsIgnoreCase(SERVER_ID)) {
			receiver = server;
		} else {
			receiver = group.getClient(message.getReceiverId());
			if (receiver == null) {
				System.err.println("Receiver ID illegal: " + message.getReceiverId());
				return;
			}
		}

		message.setTimeStamp(receiver.incrementTimestamp());
		send(receiver, message);
	}
	
	/**
	 * Send a message to a specified member
	 * @param receiver The receiver's ID
	 * @param message The message
	 */
	public void send(Client receiver, Message message) {
		manager.send(new SequencerEvent(receiver.getId(), message));
	}
	
	/**
	 * Add an event listener
	 * @param listener The listener
	 */
	public void addSequencerEventListener(MessageEventListener listener) {
		manager.addEventListener(listener);
	}
	
	/**
	 * Remove an event listener
	 * @param listener The listener
	 */
	public void removeSequencerEventListener(MessageEventListener listener) {
		manager.removeEventListener(listener);
	}
}
