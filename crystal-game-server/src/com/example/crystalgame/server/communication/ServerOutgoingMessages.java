package com.example.crystalgame.server.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.messages.TestMessage;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

/**
 * The server specific implementation of the outgoing messages module
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ServerOutgoingMessages extends OutgoingMessages {

	public boolean sendTestDataToClient(String clientId, Serializable data) {
		TestMessage message = new TestMessage("SERVER", clientId);
		message.setData(data);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
}
