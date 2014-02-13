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
		FAILED_TO_RECEIVE = new CommunicationFailureException("Failed to receive"),
		FAILED_TO_CONNECT = new CommunicationFailureException("Failed to connect to remote"),
		FAILED_TO_SERIALISE = new CommunicationFailureException("Failed to serialise the message"),
		FAILED_TO_DESERIALISE = new CommunicationFailureException("Failed to deserialise the message"),
		NO_RECEIVER_ID = new CommunicationFailureException("No receiver ID set!");
		
	/**
	 * Create an exception describing a communication failure
	 * @param message The error message
	 */
	public CommunicationFailureException(String message) {
		super(message);
	}
}
