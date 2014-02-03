package com.example.crystalgame.library.communication.abstraction;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.Message;

/**
 * This module is responsible for conversion between ray data and {@link Message} objects.
 * @author Balazs Pete, Shen Chen
 *
 */
public class AbstractionModule {

	private CommunicationManager manager;
	
	/**
	 * Initialise the module.
	 * @param manager The communication manager used together with the module.
	 */
	public void initialise(CommunicationManager manager) {
		this.manager = manager;
	}
	
	/**
	 * Encode and send the input message to the CommunicationManager 
	 * @param message The message to be processed
	 */
	public void sendMessage(Message message) {
		// TODO: to complete
		String s = "test"; 
		try {
			manager.sendData("id", s);
		} catch (CommunicationFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Process the input data into a message and forward to higher layers
	 * @param id The ID of the sender
	 * @param data The data received
	 */
	public void forwardData(String id, String data) {
		System.out.println(id + " " + data);
	}
	
}
