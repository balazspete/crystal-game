package com.example.crystalgame.library.communication.messages;

public class IdMessage extends UnicastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1484726058591292615L;

	public IdMessage(String senderId, String receiverId) {
		super(senderId, receiverId, MessageType.ID_MESSAGE);
	}

}
