package com.example.crystalgame.library.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

/**
 * Interface of the Communication module
 * @author Balazs Pete, Shen Chen
 *
 */
public class Communication {

	private CommunicationManager manager;
	private AbstractionModule abstraction;
	private IncomingMessages incoming;
	private OutgoingMessages outgoing;
	
	/**
	 * Initialise a new instance of the Communication module
	 * @param manager The CommunicationManager to be used
	 */
	public Communication(CommunicationManager manager) {
		this.manager = manager;
		
		this.abstraction = new AbstractionModule();
		this.abstraction.initialise(manager);
		
		// TODO: initialise remaining components
		
		this.manager.addAbstraction(abstraction);
	}
	
}
