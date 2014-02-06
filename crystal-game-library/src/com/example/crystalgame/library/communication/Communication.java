package com.example.crystalgame.library.communication;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;

/**
 * Interface of the Communication module
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class Communication {

	private CommunicationManager manager;
	private AbstractionModule abstraction;
	public final IncomingMessages in;
	public final OutgoingMessages out;
	
	/**
	 * Initialise a new instance of the Communication module
	 * @param manager The CommunicationManager to be used
	 */
	public Communication(CommunicationManager manager, OutgoingMessages outgoing) {
		this.manager = manager;
		
		this.abstraction = new AbstractionModule();
		this.abstraction.initialise(manager);
		
		this.in = new IncomingMessages(abstraction);
		this.out = outgoing;
		this.out.setAbstraction(abstraction);
		
		this.manager.addAbstraction(abstraction);
		
		manager.start();
	}
	
}
