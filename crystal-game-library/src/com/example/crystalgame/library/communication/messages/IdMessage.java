package com.example.crystalgame.library.communication.messages;

/**
 * A message used to communicate IS assignments
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class IdMessage extends UnicastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1484726058591292615L;

	/**
	 * Create a new ID message
	 * @param receiverId The ID of the receiver
	 */
	public IdMessage(String receiverId) {
		super(receiverId, MessageType.ID_MESSAGE);
	}

}
