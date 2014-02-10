package com.example.crystalgame.library.communication.abstraction;

import java.util.HashMap;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MessageType;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.util.RandomID;

/**
 * This module is responsible for conversion between ray data and {@link Message} objects.
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class AbstractionModule {

	private MessageQueue queue;
	private static String myId;
	
	private CommunicationManager manager;
	private ListenerManager<MessageEventListener, Message> listenerManager;
	
	private HashMap<String, String> clientToCommunicationMap;
	
	/**
	 * Initialise the module.
	 * @param manager The communication manager used together with the module.
	 */
	public void initialise(CommunicationManager manager) {
		this.manager = manager;
		this.clientToCommunicationMap = new HashMap<String, String>();
		this.listenerManager = new ListenerManager<MessageEventListener, Message>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, Message message) {
				listener.messageEvent(message);
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
			message.setSenderId(myId);
		}
		
		queue.put(clientToCommunicationMap.get(message.getReceiverId()), message);
	}
	
	/**
	 * Process the input data into a message and forward to higher layers
	 * @param id The ID of the sender
	 * @param data The data received
	 */
	public void forwardData(String id, Object data) {
		Message message = (Message) data;
		String senderId = message.getSenderId();
		
		if (message.getMessageType() == MessageType.ID_MESSAGE) {
			myId = message.getReceiverId();
			return;
		}
		
		if (senderId == null || senderId.isEmpty()) {
			// If client did not have a connection information previously
			senderId = getRandomId();
			message.setSenderId(senderId);
			manager.sendId(id, senderId);
		}
		
		// Update connection information for client
		clientToCommunicationMap.put(senderId, id);
		listenerManager.send(message);
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
}
