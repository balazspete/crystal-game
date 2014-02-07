package com.example.crystalgame.library.communication.outgoing;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.Message;

/**
 * The interface responsible for managing outgoing connections
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public abstract class OutgoingMessages {

	private AbstractionModule abstraction;
	protected String nodeId;
	
	/**
	 * Set the abstraction module to be used
	 * @param abstraction The abstraction layer to be used
	 */
	public void setAbstraction(AbstractionModule abstraction) {
		this.abstraction = abstraction;
	}

	/**
	 * Send a message
	 * @param message The message to be sent
	 * @throws CommunicationFailureException Thrown in case of a failure
	 */
	protected void send(Message message) throws CommunicationFailureException {
		if (abstraction == null) {
			throw CommunicationFailureException.FAILED_TO_INITIALISE;
		}
		
		abstraction.sendMessage(message);
	}
	
}
