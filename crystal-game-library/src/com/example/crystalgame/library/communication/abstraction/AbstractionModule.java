package com.example.crystalgame.library.communication.abstraction;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;

/**
 * This module is responsible for conversion between ray data and {@link Message} objects.
 * @author Balazs Pete, Shen Chen
 *
 */
public class AbstractionModule {

	private CommunicationManager manager;
	private ListenerManager<MessageEventListener, Message> listenerManager;
	
	/**
	 * Initialise the module.
	 * @param manager The communication manager used together with the module.
	 */
	public void initialise(CommunicationManager manager) {
		this.manager = manager;
		this.listenerManager = new ListenerManager<MessageEventListener, Message>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, Message message) {
				listener.messageEvent(message);
			}
		};
	}
	
	/**
	 * Encode and send the input message to the CommunicationManager 
	 * @param message The message to be processed
	 * @throws CommunicationFailureException Exception thrown if the message failed to serialise
	 */
	public void sendMessage(Message message) throws CommunicationFailureException {
		try {
			manager.sendData(""/*message.getReceivedId()*/, message);
		} catch (CommunicationFailureException e) {
			throw CommunicationFailureException.FAILED_TO_SERIALISE;
		}
	}
	
	/**
	 * Process the input data into a message and forward to higher layers
	 * @param id The ID of the sender
	 * @param data The data received
	 */
	public void forwardData(String id, Object data) {
		listenerManager.send((Message) data);
	}

	public void addEventListener(MessageEventListener listener) {
		listenerManager.addEventListener(listener);
	}

	public void removeEventListener(MessageEventListener listener) {
		listenerManager.removeEventListener(listener);
	}
	
}
