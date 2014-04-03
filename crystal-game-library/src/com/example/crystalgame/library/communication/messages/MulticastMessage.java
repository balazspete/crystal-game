package com.example.crystalgame.library.communication.messages;

/**
 * A generic multicast message
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public abstract class MulticastMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2056131887823741319L;

	public MulticastMessage(MessageType messageType) {
		super(messageType);
	}

	@Override
	public boolean isMulticastMessage() {
		return true;
	}
	
}
