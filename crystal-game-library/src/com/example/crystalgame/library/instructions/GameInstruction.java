package com.example.crystalgame.library.instructions;

public class GameInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6267013751421768277L;

	public enum GameInstructionType {
		
	}
	
	public final GameInstructionType gameInstruction;
	
	private GameInstruction(GameInstructionType type, String[] arguments) {
		super(InstructionType.GAME, arguments);
		this.gameInstruction = type;
	}

}
