package com.example.crystalgame.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.messages.TestMessage;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

public class ClientOutgoingMessages extends OutgoingMessages {

	public ClientOutgoingMessages() {
		super();
	}

	// temporary/tesing function
	public boolean sendTestDataToServer(Serializable data) {
		TestMessage message = new TestMessage();
		message.setData(data);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	// TODO: for each messaging scenario, add a method call here
}
