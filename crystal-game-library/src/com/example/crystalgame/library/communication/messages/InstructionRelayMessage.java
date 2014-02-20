package com.example.crystalgame.library.communication.messages;

public class InstructionRelayMessage extends UnicastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 465254985920824948L;

	public InstructionRelayMessage(String receiverId) {
		super(receiverId, MessageType.INSTRUCTION_MESSAGE);
	}

}
