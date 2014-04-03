package com.example.crystalgame.library.communication.messages;

/**
 * A message used for testing and temporary/in-progress features
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class TestMessage extends UnicastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -883459902274361048L;

	/**
	 * Create a test message
	 */
	public TestMessage() {
		super(null, MessageType.TEST_MESSAGE);
	}
	
	/**
	 * Create a test message, specifying the sender and receiver
	 * @param sourceId The sender's ID
	 * @param receiverId The receiver's ID
	 */
	public TestMessage(String receiverId) {
		super(receiverId, MessageType.TEST_MESSAGE);
	}

}
