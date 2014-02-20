package com.example.crystalgame.library.instructions;

import java.io.Serializable;

/**
 * An abstract instruction
 * @author Balazs Pete, Rajan Verma
 *
 */
public abstract class Instruction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2487964493690159271L;
	
	public final InstructionType type;
	public final Serializable[] arguments;
	
	/**
	 * Create an instruction
	 * @param type The type of the instruction
	 * @param arguments The instruction's arguments
	 */
	public Instruction(InstructionType type, Serializable... arguments) {
		this.type = type;
		this.arguments = arguments;
	}
	
}
