package com.example.crystalgame.library.instructions;

import java.io.Serializable;

/**
 * A data type describing the types of instructions
 * @author Balazs Pete, Rajan Verma
 *
 */
public enum InstructionType implements Serializable {

	GROUP_INSTRUCTION,
	GROUP_STATUS_INSTRUCTION,
	GAME_INSTRUCTION,
	DATA_SYNCRONISATION,
	DATA_TRANSFER,
	COMMUNICATION_STATUS,
	CHARACTER_INTERACTION
	
}
