package com.example.crystalgame.library.instructions;

import java.io.Serializable;

public class CommunicationStatusInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6799049552830180517L;
	
	public enum CommunicationStatusInstructionType implements Serializable {
		CLIENT_DISCONNECTED
	}
	
	public final CommunicationStatusInstructionType type;

	private CommunicationStatusInstruction(CommunicationStatusInstructionType type, Serializable... arguments) {
		super(InstructionType.COMMUNICATION_STATUS, arguments);
		this.type = type;
	}
	
	/**
	 * Create an instruction used to notify if a client has disconnected
	 * @param clientID The client ID
	 * @return The instruction
	 */
	public static CommunicationStatusInstruction createClientDisconnectedInstruction(String clientID) {
		return new CommunicationStatusInstruction(CommunicationStatusInstructionType.CLIENT_DISCONNECTED, clientID);
	}

}
