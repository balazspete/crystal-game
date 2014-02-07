package com.example.crystalgame.library.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;

/**
 * General description of the CommunicationManager
 * @author Balazs Pete, Shen Chen
 *
 */
public abstract class CommunicationManager extends Thread {

	protected AbstractionModule abstraction; 
	
	/**
	 * Add the abstraction layer to the module
	 * @param abstraction the abstraction layer associated with the module
	 */
	public void addAbstraction(AbstractionModule abstraction) {
		this.abstraction = abstraction;
	}
	
	/**
	 * Send the input data to the specified destination
	 * @param id The id of the destination module
	 * @param data The data to be sent
	 * @throws CommunicationFailureException Thrown in case of an error
	 */
	public abstract void sendData(String id, Serializable data) throws CommunicationFailureException;
	
	/**
	 * Send an ID to the other side
	 * @param id The connection ID
	 * @param nodeId The ID to send
	 */
	public abstract void sendId(String id, String nodeId);
}
