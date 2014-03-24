package com.example.crystalgame.library.communication.abstraction;

import java.util.HashMap;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MessageType;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.util.RandomID;

/**
 * This module is responsible for conversion between ray data and {@link Message} objects.
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public abstract class AbstractionModule {

	private MessageQueue queue;
	
	private CommunicationManager manager;
	private ListenerManager<MessageEventListener, MessageEvent> listenerManager;
	
	public static HashMap<String, String> clientToCommunicationMap;
	
	/**
	 * Initialise the module.
	 * @param manager The communication manager used together with the module.
	 */
	public void initialise(CommunicationManager manager) {
		this.manager = manager;
		clientToCommunicationMap = new HashMap<String, String>();
		
		// Create an listener manager to be able to emit events
		this.listenerManager = new ListenerManager<MessageEventListener, MessageEvent>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, MessageEvent event) {
				// Call the forwarding implementation specified in the MessageEventListener class
				MessageEventListener.eventHandlerHelper(listener, event);
			}
		};
		
		this.queue = new MessageQueue(manager);
		this.queue.start();
	}
	
	/**
	 * Encode and send the input message to the CommunicationManager 
	 * @param message The message to be processed
	 * @throws CommunicationFailureException Exception thrown if the message failed to serialise
	 */
	public void sendMessage(Message message) throws CommunicationFailureException {
		String id = message.getSenderId();
		if (id == null || id.isEmpty()) {
			// If id is not set => Message originates from this node
			message.setSenderId(myID());
		}
		
		System.out.println("Outgoing message { client:" + message.getReceiverId() + ", type:" + message.getMessageType() + "}");
		
		if (message.getReceiverId() == null) {
			// The message does not have a destination :(
			throw CommunicationFailureException.NO_RECEIVER_ID;
		}
		
		// Queue up message to send
		queue.put(message.getReceiverId(), message);
	}
	
	/**
	 * Process the input data into a message and forward to higher layers
	 * @param id The ID of the sender
	 * @param data The data received
	 */
	public void forwardData(String id, Object data) {
		Message message = (Message) data;
		String senderId = message.getSenderId();
		
		System.out.println("Incoming message { client:" + message.getSenderId() + ", type:" + message.getMessageType() + "}");
		
		if (senderId == null || senderId.isEmpty()) {
			// If connected client did not have a connection information previously
			// Create an ID
			senderId = getRandomId();
			// Set the incoming Message's sender ID
			message.setSenderId(senderId);
			// Let the client know about its new ID
			manager.sendId(id, senderId);
			
			// Don't accept null IDs
			return;
		}
		
		// Update connection information for client (in case it changed)
		clientToCommunicationMap.put(senderId, id);
		
		// Forward message to higher layers through an event
		MessageEvent event = new MessageEvent(message);
		event.setSenderId(message.getSenderId());
		event.setReceiverId(message.getReceiverId());
		listenerManager.send(event);
	}

	/**
	 * Add an event listener for new messages
	 * @param listener the listener
	 */
	public void addEventListener(MessageEventListener listener) {
		listenerManager.addEventListener(listener);
	}

	/**
	 * Remove an event listener
	 * @param listener The listener to remove
	 */
	public void removeEventListener(MessageEventListener listener) {
		listenerManager.removeEventListener(listener);
	}
	
	/**
	 * Get a random ID
	 * @return A random String ID
	 */
	private String getRandomId() {
		String id = null;
		do {
			id = RandomID.getRandomId();
		} while (clientToCommunicationMap.containsKey(id));
		
		return id;
	}
	
	public abstract String myID();
	
}
