package com.example.crystalgame.server.communication;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.ControlMessageEventListener;
import com.example.crystalgame.server.groups.GroupInstanceManager;

/**
 * An extension of the Communication module for the server
 * @author Balazs Pete, Rajan Verma
 *
 */
public class ServerCommunication extends Communication {

	public final ServerOutgoingMessages out;
	public final ServerIncomingMessages in;
	
	/**
	 * Create a server communication module
	 * @param manager The COmmunication manager
	 * @param groupInstanceManager The group instance manager
	 */
	public ServerCommunication(CommunicationManager manager, GroupInstanceManager groupInstanceManager) {
		super(manager);
		
		// Set up outgoing messages
		out = new ServerOutgoingMessages();
		out.setAbstraction(abstraction);
		
		// Set up incoming messages
		in = new ServerIncomingMessages(this.abstraction, groupInstanceManager);
		substribeToGroupInstanceManagerEvents(groupInstanceManager);
	}

	private void substribeToGroupInstanceManagerEvents(GroupInstanceManager groupInstanceManager) {
		groupInstanceManager.addControlMessageEventListener(new ControlMessageEventListener() {
			@Override
			public void messageEvent(Message message) {
				// Forward the group instance manager's message events to the outgoing messages component
				out.sendControlMessageToClient(message.getReceiverId(), (ControlMessage) message);
			}
		});
	}
	
}
