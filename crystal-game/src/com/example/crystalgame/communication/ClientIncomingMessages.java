package com.example.crystalgame.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.Message;

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
//		if (message.getGroupId() != null && !groupIDs.contains(message.getGroupId().intern())) {
//			return;
//		}
		
		messageListenerManager.send(createMessageEventFromMessage(message));
	}

	@Override
	protected void handleGroupStatusMessage(GroupStatusMessage message) {
		messageListenerManager.send(createMessageEventFromMessage(message));
	}

	@Override
	protected void handleControlMessage(ControlMessage message) {
		messageListenerManager.send(createMessageEventFromMessage(message));
	}
	
	private MessageEvent createMessageEventFromMessage(Message message) {
		MessageEvent event = new MessageEvent(message);
		event.setSenderId(message.getSenderId());
		return event;
		
	}

	@Override
	protected void handleInstructionRelayMessage(InstructionRelayMessage message) {
		InstructionEvent event = new InstructionEvent((Instruction) message.getData());
		instructionListenerManager.send(event);
	}

	@Override
	protected void handleIdMessages(IdMessage message) {
		messageListenerManager.send(createMessageEventFromMessage(message));
	}
}
