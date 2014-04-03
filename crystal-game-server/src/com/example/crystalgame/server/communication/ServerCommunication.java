package com.example.crystalgame.server.communication;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
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
		substribeToSequencerEvents(groupInstanceManager);
	}

	private void substribeToGroupInstanceManagerEvents(GroupInstanceManager groupInstanceManager) {
		groupInstanceManager.addMessageEventListener(new MessageEventListener() {
			@Override
			public void onMessageEvent(MessageEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}

			@Override
			public void onGroupStatusMessageEvent(MessageEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}

			@Override
			public void onControlMessage(MessageEvent event) {
				out.sendControlMessageToClient(event.getReceiverId(), (ControlMessage) event.getMessage());
			}

			@Override
			public void onInstructionRelayMessage(MessageEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}

			@Override
			public void onIdMessageEvent(MessageEvent event) {
				// Server does not care about this...
			}
		});
	}
	
	private void substribeToSequencerEvents(GroupInstanceManager groupInstanceManager) {
		// Forward all messages from the groups to the ougoing messages module
		groupInstanceManager.setGroupMessageEventListener(new MessageEventListener(){
			@Override
			public void onMessageEvent(MessageEvent event) {
				String receiverId = event.getReceiverId();
				if(receiverId.equals("SERVER")) {
					return;
				}
				out.sendSequencedMessage(receiverId, event.getMessage());
			}

			@Override
			public void onGroupStatusMessageEvent(MessageEvent event) {
				onMessageEvent(event);
			}

			@Override
			public void onControlMessage(MessageEvent event) {
				onMessageEvent(event);
			}

			@Override
			public void onInstructionRelayMessage(MessageEvent event) {
				onMessageEvent(event);
			}

			@Override
			public void onIdMessageEvent(MessageEvent event) {
				// Server does not care about this...
			}
		});
	}

	@Override
	protected AbstractionModule abstraction() {
		return new AbstractionModule() {
			@Override
			public String myID() {
				return "SERVER";
			}
		};
	}
}
