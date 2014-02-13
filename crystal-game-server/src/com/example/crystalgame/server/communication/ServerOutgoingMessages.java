package com.example.crystalgame.server.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.TestMessage;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

/**
 * The server specific implementation of the outgoing messages module
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ServerOutgoingMessages extends OutgoingMessages {
	
	public final static String serverID = "SERVER"; 

	@Deprecated
	public boolean sendTestDataToClient(String clientId, Serializable data) {
		TestMessage message = new TestMessage(clientId);
		message.setData(data);
		message.setSenderId(serverID);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Send a control message to a client
	 * @param clientId The ID of the client to send the message to
	 * @param message The message to send
	 * @return True of the message was sent
	 */
	public boolean sendControlMessageToClient(String clientId, ControlMessage message) {
		message.setSenderId(serverID);
		message.setReceiverId(clientId);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Send a timestamped message to the specified client
	 * @param receiverId The receiving client's ID
	 * @param message The message
	 * @return True if the message was successfully sent
	 */
	public boolean sendSequencedMessage(String receiverId, Message message) {
		message.setReceiverId(receiverId);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	// TODO: implement required data transfer funtions
}
