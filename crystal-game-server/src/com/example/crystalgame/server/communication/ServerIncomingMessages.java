package com.example.crystalgame.server.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.server.groups.Client;
import com.example.crystalgame.server.groups.Group;
import com.example.crystalgame.server.groups.GroupInstanceManager;

public class ServerIncomingMessages extends IncomingMessages {

	private GroupInstanceManager groupInstanceManager;
	
	public ServerIncomingMessages(AbstractionModule abstraction, GroupInstanceManager groupInstanceManager) {
		super(abstraction);
		this.groupInstanceManager = groupInstanceManager;
		
		// Allow components to subscribe to message events
		this.messageListenerManager = new ListenerManager<MessageEventListener, MessageEvent>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, MessageEvent event) {
				// If the listener listens to messages from only one group, and the group IDs differ -> return
				if(listener.groupId != null && listener.groupId.equals(event.getMessage().getGroupId())) {
					return;
				}
				
				// Forward the event based on the defined logic in the listener
				MessageEventListener.eventHandlerHelper(listener, event);
			}
		};
	}

	private Group getGroup(Message message) {
		Client client = groupInstanceManager.getClient(message.getSenderId());
		if (client == null) {
			return null;
		}
		return groupInstanceManager.getGroup(client.getGroupID());
	}
	
	/**
	 * Add a message event listener
	 * @param listener The listener to be added
	 */
	public void addMessageEventListener(MessageEventListener listener) {
		messageListenerManager.addEventListener(listener);
	}

	/**
	 * Remove a message event listener
	 * @param listener The listener to be removed
	 */
	public void removeMessageEventListener(MessageEventListener listener) {
		messageListenerManager.removeEventListener(listener);
	}

	@Override
	protected void handleMessage(Message message) {
		Group group = getGroup(message);
		
		// Only allow messaging, if client belongs to a group
		if (group != null) {
			message.setGroup(group.groupId);
			
			// If message is legitimate, do some forwarding here...
			MessageEvent event = new MessageEvent(message);
			event.setSenderId(message.getSenderId());
			event.setReceiverId(message.getReceiverId());
			messageListenerManager.send(event);
		}
	}
	
	@Override
	protected void handleGroupStatusMessage(GroupStatusMessage message) {
		Group group = getGroup(message);
		if (group != null) {
			message.setGroup(group.groupId);
			MessageEvent event = new MessageEvent(message);
			event.setSenderId(message.getSenderId());
			messageListenerManager.send(event);
		}
	}

	@Override
	protected void handleControlMessage(ControlMessage message) {
		Group group = getGroup(message);
		if (group != null) {
			message.setGroup(group.groupId);
		}
		
		MessageEvent event = new MessageEvent(message);
		event.setSenderId(message.getSenderId());
		messageListenerManager.send(event);
	}

	@Override
	protected void handleInstructionRelayMessage(InstructionRelayMessage message) {
		Group group = getGroup(message);
		if (group != null) {
			message.setGroup(group.groupId);
		}
		
		// Emit instruction 
		InstructionEvent event = new InstructionEvent((Instruction) message.getData());
		instructionListenerManager.send(event);
		
		// Forward instruction message to the group instance manager
		groupInstanceManager.forwardMessage(message);
	}

	@Override
	protected void handleIdMessages(IdMessage message) {
		// Server does not care about this...
	}
	
}
