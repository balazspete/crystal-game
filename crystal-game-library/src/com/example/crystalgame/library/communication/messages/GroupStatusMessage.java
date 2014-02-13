package com.example.crystalgame.library.communication.messages;

/**
 * A message used to communicate group status related information
 * @author Balazs Pete, Shen Chen
 *
 */
public class GroupStatusMessage extends UnicastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327517924516892684L;

	/**
	 * Create a new message
	 * @param receiverId The receiver's ID
	 */
	public GroupStatusMessage(String receiverId) {
		super(receiverId, MessageType.GROUP_STATUS_MESSAGE);
	}
	
}
