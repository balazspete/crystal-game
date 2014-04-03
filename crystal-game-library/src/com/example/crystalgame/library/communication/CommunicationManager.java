package com.example.crystalgame.library.communication;

import java.io.Serializable;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.instructions.CommunicationStatusInstruction;

/**
 * General description of the CommunicationManager
 * @author Balazs Pete, Shen Chen
 *
 */
public abstract class CommunicationManager extends Thread {

	protected AbstractionModule abstraction; 
	
	protected static String address;
	protected static int port;
	
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
	
	/**
	 * Set/Change the server address to connect to
	 * @param address The new server address
	 */
	public void setServerAddress(String address) {
		System.out.print("Changing server address: " + CommunicationManager.address + "->");
		CommunicationManager.address = address;
		System.out.println(CommunicationManager.address);
	}
	
	/**
	 * Set/Change the port number used for communication
	 * @param port The port number
	 */
	public void setPort(int port) {
		System.out.print("Changing port: " + CommunicationManager.port + "->");
		CommunicationManager.port = port;
		System.out.println(CommunicationManager.port);
	}
	
	public void notifyOfDisconnectedClient(String clientID) {
		InstructionRelayMessage message = new InstructionRelayMessage(abstraction.myID());
		message.setData(CommunicationStatusInstruction.createClientDisconnectedInstruction(clientID));
		abstraction.forwardData(null, message);
	}
	
	public abstract boolean isClient();
	
}
