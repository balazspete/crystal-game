package com.example.crystalgame.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.TestMessage;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;
import com.example.crystalgame.library.instructions.GroupInstruction;

/**
 * The outgoing messages component extension for the clients
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese, Rajan Verma
 *
 */
public class ClientOutgoingMessages extends OutgoingMessages {

	public ClientOutgoingMessages() {
		super();
	}

	@Deprecated
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
	
	/**
	 * Send a {@Link GroupInstruction} to the server
	 * @param instruction The instruction to send
	 * @return True if the instruction was sent
	 */
	public boolean sendGroupInstructionToServer(GroupInstruction instruction) {
		ControlMessage message = new ControlMessage();
		message.setData(instruction);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	// TODO: for each messaging scenario, add a method call here
}
