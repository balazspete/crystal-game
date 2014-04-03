package com.example.crystalgame.library.events;

import com.example.crystalgame.library.instructions.Instruction;

/**
 * An event containing an instruction
 * @author Balazs Pete, Allen Thomas Varghese, Rajan Verma
 *
 */
public class InstructionEvent extends Event {

	private Instruction instruction;
	
	/**
	 * Create a new instruction event
	 * @param instruction The instruction to contain
	 */
	public InstructionEvent(Instruction instruction) {
		this.instruction = instruction;
	}
	
	/**
	 * Get the wrapped instruction
	 * @return The instruction
	 */
	public Instruction getInstruction() {
		return instruction;
	}
	
}
