package com.example.crystalgame.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.TestMessage;
import com.example.crystalgame.library.communication.messages.TestMulticastMessage;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;
import com.example.crystalgame.library.instructions.Instruction;

/**
 * The outgoing messages component extension for the clients
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese, Rajan Verma
 *
 */
public class ClientOutgoingMessages extends OutgoingMessages {

	private final String SERVER_ID = "SERVER";
	
	public ClientOutgoingMessages() {
		super();
	}

	@Deprecated
	public boolean sendTestDataToServer(Serializable data) {
		TestMessage message = new TestMessage();
		message.setData(data);
		message.setReceiverId(SERVER_ID);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	@Deprecated
	public boolean sendTestUnicastData(String clientId, Serializable data) {
		TestMessage message = new TestMessage(clientId);
		message.setData(data);
		message.setReceiverId(clientId);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	@Deprecated
	public boolean sendTestMulticastData(Serializable data) {
		TestMulticastMessage message = new TestMulticastMessage();
		message.setData(data);
		message.setReceiverId(SERVER_ID);
		
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
		message.setReceiverId(SERVER_ID);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Send a group status instruction to the server
	 * @param instruction The instruction
	 * @return True if message has been sent
	 */
	public boolean sendGroupStatusInstruction(GroupStatusInstruction instruction) {
		GroupStatusMessage message = new GroupStatusMessage(SERVER_ID);
		message.setData(instruction);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Send a Game instruction to the server
	 * @param instruction The game instruction
	 * @return True if the message was sent
	 */
	public boolean relayInstructionToServer(Instruction instruction) {
		return relayInstruction(instruction, SERVER_ID);
	}
	
	/**
	 * Send a Game instruction to a node
	 * @param instruction The game instruction
	 * @param receiverId The node ID
	 * @return True if the message was sent
	 */
	public boolean relayInstruction(Instruction instruction, String receiverId)  {
		InstructionRelayMessage message = new InstructionRelayMessage(receiverId);
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
