package com.example.crystalgame.library.instructions;

import java.io.Serializable;

import com.example.crystalgame.library.data.Location;

public class GameInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6267013751421768277L;

	public enum GameInstructionType implements Serializable {
		START_GAME_REQUEST, CREATE_GAME_REQUEST, CREATE_GAME
		, CAPTURE_CRYSTAL_REQUEST
	}
	
	public final GameInstructionType gameInstruction;
	
	private GameInstruction(GameInstructionType type, Serializable... arguments) {
		super(InstructionType.GAME_INSTRUCTION, arguments);
		this.gameInstruction = type;
	}

	/**
	 * Create a start game request
	 * Used to request the start of a game by a client
	 * @return The instruction
	 */
	public static GameInstruction createStartGameRequestGameInstruction() {
		return new GameInstruction(GameInstructionType.START_GAME_REQUEST);
	}
	
	/**
	 * Create a create game request
	 * Used by the server to request a game creation by a client
	 * @return the instruction
	 */
	public static GameInstruction createCreateGameRequestGameInstruction() {
		return new GameInstruction(GameInstructionType.CREATE_GAME_REQUEST);
	}
	
	/**
	 * Used by the clients to create a game create instruction
	 * @param gameName The name of the game
	 * @param upperLeft First corner of the game location
	 * @param upperRight Second corner of the game location
	 * @param lowerRight Third corner of the game location
	 * @param lowerLeft Fourth corner of the game location
	 * @return The instruction
	 * @throws InstructionFormatException Thrown if an argument was null
	 */
	public static GameInstruction createCreateGameGameInstruction(String gameName, Location upperLeft, Location upperRight, Location lowerRight, Location lowerLeft) throws InstructionFormatException {
		if (upperLeft == null || upperRight == null || lowerLeft == null || lowerRight == null) {
			throw InstructionFormatException.NULL_ARGUMENT;
		}
		
		return new GameInstruction(GameInstructionType.CREATE_GAME, gameName, upperLeft, upperRight, lowerRight, lowerLeft);
	}
	
	/**
	 * Used to request capture of a crystal
	 * @param playerID Player ID
	 * @param crystalID Crystal ID
	 * @return The instruction
	 * @throws InstructionFormatException
	 */
	public static GameInstruction createCrystalCaptureRequestInstruction(String playerID, String crystalID) throws InstructionFormatException {
		if(null == playerID || null == crystalID) {
			throw InstructionFormatException.NULL_ARGUMENT;
		}
		return new GameInstruction(GameInstructionType.CAPTURE_CRYSTAL_REQUEST, playerID, crystalID); 
	}
}
