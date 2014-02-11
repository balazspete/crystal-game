package com.example.crystalgame.communication;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.CommunicationManager;

/**
 * The client side implementation of communication
 * @author Balazs Pete, Allen, Thomas Varghese, Rajan Verma
 *
 */
public class ClientCommunication extends Communication {
	
	public final ClientOutgoingMessages out;
	public final ClientIncomingMessages in;
	
	public ClientCommunication(CommunicationManager manager, ClientOutgoingMessages out) {
		super(manager);
		this.in = new ClientIncomingMessages(abstraction);
		this.out = out;
		this.out.setAbstraction(abstraction);
	}
	
	/**
	 * Change the address of the server to connect to
	 * @param address The new address
	 */
	public void serverAddressChange(String address) {
		manager.setServerAddress(address);
	}
	
	/**
	 * Change the port to be used for communication
	 * @param port The new port number
	 */
	public void portChange(int port) {
		manager.setPort(port);
	}

}
