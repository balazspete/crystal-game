package com.example.crystalgame.server.sequencer;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.server.groups.Client;
import com.example.crystalgame.server.groups.Group;

/**
 * The module responsible for message ordering
 * @author Balazs Pete, Shen Chen
 *
 */
public class Sequencer {

	private ListenerManager<SequencerEventListener, SequencerEvent> manager;
	
	private long multicastTimestamp = -1;
	private Group group;
	
	/**
	 * Create a sequencer for the specified group
	 * @param group The group
	 */
	public Sequencer(Group group) {
		this.group = group;
		
		// Allow components to subscribe to message events from the Sequencer
		manager = new ListenerManager<SequencerEventListener, SequencerEvent>() {
			@Override
			protected void eventHandlerHelper(SequencerEventListener listener, SequencerEvent data) {
				listener.sequencerEvent(data);
			}
		};
	}
	
	/**
	 * Send a message to all members of a group
	 * @param message The message
	 */
	public void sendMessageToAll(MulticastMessage message) {
		for(Client receiver : group.getClients()) {
			message.setTimeStamp(++multicastTimestamp);
			send(receiver, message);
		}
	}
	
	/**
	 * Send a message to a member of a group
	 * @param message The message
	 */
	public void sendMessageToOne(UnicastMessage message) {
		System.out.println("message");
		Client receiver = group.getClient(message.getReceiverId());
		if (receiver == null) {
			System.err.println("Receiver ID illegal: " + message.getReceiverId());
			return;
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
	public void addSequencerEventListener(SequencerEventListener listener) {
		manager.addEventListener(listener);
	}
	
	/**
	 * Remove an event listener
	 * @param listener The listener
	 */
	public void removeSequencerEventListener(SequencerEventListener listener) {
		manager.removeEventListener(listener);
	}
}
