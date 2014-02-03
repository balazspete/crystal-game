package com.example.crystalgame.library.communication;

/**
 * An error expressing a communication fault
 * @author Balazs Pete, Shen Chen
 *
 */
public class CommunicationFailureException extends Exception {

	/**
	 * An ID used by serialisation
	 */
	private static final long serialVersionUID = -729398427262941830L;
	
	public static final CommunicationFailureException 
		FAILED_TO_INITIALISE = new CommunicationFailureException("Failed to initialise connection"),
		FAILED_TO_TRANSMIT = new CommunicationFailureException("Failed to transmit"),
		FAILED_TO_CONNECT = new CommunicationFailureException("Failed to connect to remote");
			
	public CommunicationFailureException(String message) {
		super(message);
	}
}
