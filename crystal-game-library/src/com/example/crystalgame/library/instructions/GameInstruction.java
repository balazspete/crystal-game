package com.example.crystalgame.library.instructions;

import java.io.Serializable;

import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Character;

public class GameInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6267013751421768277L;

	public enum GameInstructionType implements Serializable {
		START_GAME_REQUEST, CREATE_GAME_REQUEST, CREATE_GAME,
		GAME_BOUNDARY_OUTSIDE_RESPONSE, ENERGY_DISQUALIFY_RESPONSE,
		CAPTURE_CRYSTAL_REQUEST, CAPTURE_MAGICAL_ITEM_REQUEST, EXCHANGE_MAGICAL_ITEM,
		GAME_STARTED, GAME_ENDED
		, OUT_OF_ENERGY
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
	 * @param gameCharacter The game character that sends the create game request
	 * @return The instruction
	 * @throws InstructionFormatException Thrown if an argument was null
	 */
	public static GameInstruction createCreateGameGameInstruction(String gameName, Location upperLeft, Location upperRight, Location lowerRight, Location lowerLeft, Location throneRoom, int gameLength) throws InstructionFormatException {
		if (upperLeft == null || upperRight == null || lowerLeft == null || lowerRight == null) {
			throw InstructionFormatException.NULL_ARGUMENT;
		}
		
		return new GameInstruction(GameInstructionType.CREATE_GAME, gameName, upperLeft, upperRight, lowerRight, lowerLeft, throneRoom, gameLength);
	}
	
	/**
	 * 
	 * @return
	 */
	public static GameInstruction createGameBoundaryDisqualifyInstruction() {
		return new GameInstruction(GameInstructionType.GAME_BOUNDARY_OUTSIDE_RESPONSE);
	}
	
	public static GameInstruction createEnergyDisqualifyInstruction() {
		return new GameInstruction(GameInstructionType.ENERGY_DISQUALIFY_RESPONSE);
	}
	
	/**
	 * Used to request capture of a crystal
	 * @param playerID Player ID
	 * @param crystalID Crystal ID
	 * @return The instruction
	 * @throws InstructionFormatException
	 */
	public static GameInstruction createCrystalCaptureRequestInstruction(String playerID, String characterID, String crystalID) throws InstructionFormatException {
		if(null == playerID || null == crystalID) {
			throw InstructionFormatException.NULL_ARGUMENT;
		}
		return new GameInstruction(GameInstructionType.CAPTURE_CRYSTAL_REQUEST, playerID, characterID, crystalID); 
	}
	
	/**
	 * Used to request capture of a magical item
	 * @param playerID Player ID
	 * @param crystalID Magical Item ID
	 * @return The instruction
	 * @throws InstructionFormatException
	 */
	public static GameInstruction createMagicalItemCaptureRequestInstruction(String playerID, String characterID, String magicalItemID) throws InstructionFormatException {
		if(null == playerID || null == magicalItemID) {
			throw InstructionFormatException.NULL_ARGUMENT;
		}
		return new GameInstruction(GameInstructionType.CAPTURE_MAGICAL_ITEM_REQUEST, playerID, characterID, magicalItemID); 
	}
	
	/**
	 * Used to signal a game start
	 * @return The instruction
	 */
	public static GameInstruction createGameStartedSignalInstruction() {
		return new GameInstruction(GameInstructionType.GAME_STARTED);
	}
	
	/**
	 * Used to signal a game end
	 * @return The instruction
	 */
	public static GameInstruction createGameEndedSignalInstruction() {
		return new GameInstruction(GameInstructionType.GAME_ENDED);
	}
	
	public static GameInstruction createPlayerOutOfEnergyRequestInstruction(String playerID, String clientID) {
		return new GameInstruction(GameInstructionType.OUT_OF_ENERGY, playerID, clientID);
	}
	
}
