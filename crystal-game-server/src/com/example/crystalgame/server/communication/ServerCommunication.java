package com.example.crystalgame.server.communication;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.groups.GroupInstanceManager;
import com.example.crystalgame.server.sequencer.SequencerEvent;
import com.example.crystalgame.server.sequencer.SequencerEventListener;

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
			public void messageEvent(MessageEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}

			@Override
			public void groupStatusMessageEvent(MessageEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}

			@Override
			public void controlMessage(MessageEvent event) {
				out.sendControlMessageToClient(event.getReceiverId(), (ControlMessage) event.getMessage());
			}
		});
	}
	
	private void substribeToSequencerEvents(GroupInstanceManager groupInstanceManager) {
		groupInstanceManager.setSequencerEventListener(new SequencerEventListener(){
			@Override
			public void sequencerEvent(SequencerEvent event) {
				out.sendSequencedMessage(event.getReceiverId(), event.getMessage());
			}
		});
	}
}
