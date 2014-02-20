package com.example.crystalgame.library.instructions;

public class InstructionFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -769947058200812360L;
	
	public static final InstructionFormatException
		NULL_ARGUMENT = new InstructionFormatException("One or more required arguments are null");
	
	public InstructionFormatException(String message) {
		super(message);
	}

}
