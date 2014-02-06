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
	
	public void setAbstraction(AbstractionModule abstraction) {
		this.abstraction = abstraction;
	}
			
	protected void send(Message message) throws CommunicationFailureException {
		if (abstraction == null) {
			throw CommunicationFailureException.FAILED_TO_INITIALISE;
		}
		
		abstraction.sendMessage(message);
	}
	
}
