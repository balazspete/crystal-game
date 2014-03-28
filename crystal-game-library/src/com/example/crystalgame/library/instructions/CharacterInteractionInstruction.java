package com.example.crystalgame.library.instructions;

import java.io.Serializable;

import com.example.crystalgame.library.util.RandomID;

public class CharacterInteractionInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8737249579795449221L;

	public enum CharacterInteractionInstructionType implements Serializable {
		INTERACTION_REQUEST, INTERACTION_REQUEST_ACK,
		RPS_SELECTION_REQUEST, RPS_SELECTION_REPLY,
		RESULT
	}
	
	/**
	 *The rock, paper, scissors selection types
	 */
	public enum RPSSelection implements Serializable {
		ROCK, PAPER, SCISSORS
	}
	
	public final CharacterInteractionInstructionType characterInteractionInstructionType;
	
	private CharacterInteractionInstruction(CharacterInteractionInstructionType type, Serializable... arguments) {
		super(InstructionType.CHARACTER_INTERACTION, arguments);
		characterInteractionInstructionType = type;
	}
	
	/**
	 * Create an instruction to request an interaction with another player
	 * @param myID My ID
	 * @param myCharacterID My character's ID
	 * @return the instruction
	 * @throws InstructionFormatException Thrown if one of the inputs is null
	 */
	public static CharacterInteractionInstruction createInteractionRequestInstruction(String myID, String myCharacterID) throws InstructionFormatException {
		if (myID == null || myCharacterID == null) {
			throw new InstructionFormatException("myID or myCharacterID is null!");
		}
		
		return new CharacterInteractionInstruction(CharacterInteractionInstructionType.INTERACTION_REQUEST,
				RandomID.getRandomId(), myID, myCharacterID, System.currentTimeMillis());
	}
	
	/**
	 * Create a reply to an interaction request
	 * @param instruction The request instruction
	 * @param myID My client ID
	 * @param myCharacterID My character's ID
	 * @param accept True if accepting the request
	 * @param initTime The time the request has been initiated
	 * @return The instruction
	 * @throws InstructionFormatException Thrown if argument is incorrect
	 */
	public static CharacterInteractionInstruction createInteractionRequestAcknowledgmentInstruction(CharacterInteractionInstruction instruction, 
			String myID, String myCharacterID, boolean accept, long initTime) throws InstructionFormatException {
		if (myID == null || myCharacterID == null) {
			throw new InstructionFormatException("myID or myCharacterID is null!");
		}
		
		if (instruction.characterInteractionInstructionType != CharacterInteractionInstructionType.INTERACTION_REQUEST) {
			throw new InstructionFormatException("Input instruction is not of type " + 
					CharacterInteractionInstructionType.INTERACTION_REQUEST);
		}
		
		return new CharacterInteractionInstruction(CharacterInteractionInstructionType.INTERACTION_REQUEST_ACK, 
				instruction.arguments[0], myID, myCharacterID, accept, initTime);
	}
	
	/**
	 * Create an instruction to request the other client to make a selection
	 * @return The instruction
	 */
	public static CharacterInteractionInstruction createRPSSelectionRequest() {
		return new CharacterInteractionInstruction(CharacterInteractionInstructionType.RPS_SELECTION_REQUEST);
	}
	
	/**
	 * Create an instruction containing the selection
	 * @param selection The selection
	 * @return The instruction
	 * @throws InstructionFormatException Thrown if input is null
	 */
	public static CharacterInteractionInstruction createRPSSelectionReply(RPSSelection selection) throws InstructionFormatException {
		if (selection == null) {
			throw new InstructionFormatException("Selection is null!");
		}
		
		return new CharacterInteractionInstruction(CharacterInteractionInstructionType.RPS_SELECTION_REPLY, selection);
	}
	
	public static CharacterInteractionInstruction createRPSResultRelay(boolean didWin) {
		return new CharacterInteractionInstruction(CharacterInteractionInstructionType.RESULT, didWin);
	}

}
