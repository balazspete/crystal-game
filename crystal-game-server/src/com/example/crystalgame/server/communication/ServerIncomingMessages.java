package com.example.crystalgame.server.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MessageType;
import com.example.crystalgame.library.events.ControlMessageEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.groups.GroupInstanceManager;

public class ServerIncomingMessages extends IncomingMessages {

	private GroupInstanceManager groupInstanceManager;
	private ListenerManager<ControlMessageEventListener, ControlMessage> controlMessageListenerManager;
	
	public ServerIncomingMessages(AbstractionModule abstraction, GroupInstanceManager groupInstanceManager) {
		super(abstraction);
		this.groupInstanceManager = groupInstanceManager;
		
		// Allow components to subscribe to message events
		this.messageListenerManager = new ListenerManager<MessageEventListener, Message>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, Message message) {
				// If the listener listens to messages from only one group, and the group IDs differ -> return
				if(listener.groupId != null && listener.groupId != message.getGroupId().intern()) {
					return;
				}
				
				// Forward the message to the upper layers
				listener.messageEvent(message);
			}
		};
		
		// Allow components to subscribe to control message events
		controlMessageListenerManager = new ListenerManager<ControlMessageEventListener, ControlMessage>() {
			@Override
			protected void eventHandlerHelper(
					ControlMessageEventListener listener, ControlMessage message) {
				// Forward the message to the upper layers
				listener.messageEvent(message);
			}
		};
	}

	protected void handleMessage(Message message) {
		// Determine if message is legitimate (is group ID is set, does the client belong to the group?)
		boolean isAllowed = 
				message.getGroupId() != null && 
				groupInstanceManager.getGroup(message.getGroupId()).isClientInGroup(message.getSenderId());
		
		if (message.getMessageType() == MessageType.CONTROL_MESSAGE) {
			// Forward the component message to others who have subscribed to them
			controlMessageListenerManager.send((ControlMessage) message);
			return;
		}
		
		if (isAllowed) {
			// If message is legitimate, do some forwarding here...
			
			// TODO: filter based on type and pass to appropriate listener
			
			messageListenerManager.send(message);
		}
	}
	
	/**
	 * Add a message event listener
	 * @param listener The listener to be added
	 */
	public void addControlMessageEventListener(ControlMessageEventListener listener) {
		controlMessageListenerManager.addEventListener(listener);
	}

	/**
	 * Remove a message event listener
	 * @param listener The listener to be removed
	 */
	public void removeControlMessageEventListener(ControlMessageEventListener listener) {
		controlMessageListenerManager.removeEventListener(listener);
	}
	
}
