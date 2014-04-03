package com.example.crystalgame.library.communication.messages;

/**
 * A generic unicast message
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public abstract class UnicastMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9120590166831859911L;
	
	/**
	 * Create a Message destined to a single node
	 * @param receiverId The receiver's ID
	 */
	public UnicastMessage(String receiverId, MessageType messageType) {
		super(messageType);
		this.receiverId = receiverId;
	}
	
	@Override
	public boolean isMulticastMessage() {
		return false;
	}

}
