package com.example.crystalgame.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MessageType;

/**
 * The client side implementation of incoming messages
 * @author Balazs Pete, Rajan verma
 *
 */
public class ClientIncomingMessages extends IncomingMessages {

	public ClientIncomingMessages(AbstractionModule abstraction) {
		super(abstraction);
	}

	@Override
	protected void handleMessage(Message message) {
		if (message.getGroupId() != null && !groupIDs.contains(message.getGroupId().intern())) {
			return;
		}
		
		
		if(message.getMessageType() == MessageType.CONTROL_MESSAGE) {
			handleControlMessage((ControlMessage) message);
			return;
		}
		
		messageListenerManager.send(message);
		
		// TODO: instruction extraction/encoding and event send
	}

	private void handleControlMessage(ControlMessage message) {
		// TODO: handle control message
	}
}
