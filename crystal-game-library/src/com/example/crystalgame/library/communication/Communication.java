package com.example.crystalgame.library.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

/**
 * Interface of the Communication module
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public abstract class Communication {

	protected CommunicationManager manager;
	protected AbstractionModule abstraction;
	
	/**
	 * Initialise a new instance of the Communication module
	 * @param manager The CommunicationManager to be used
	 */
	public Communication(CommunicationManager manager) {
		this.manager = manager;
		
		this.abstraction = abstraction();
		this.abstraction.initialise(manager);
		
		this.manager.addAbstraction(abstraction);
		
		manager.start();
	}
	
	protected abstract AbstractionModule abstraction();
	
}
