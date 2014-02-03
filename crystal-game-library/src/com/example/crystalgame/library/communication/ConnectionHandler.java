package com.example.crystalgame.library.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;

/**
 * A runnable to handle individual client connections
 * @author Balazs Pete, Shen Chen
 *
 */
public class ConnectionHandler implements Runnable {

	private String id;
	private Socket socket;
	private AbstractionModule abstraction;
	
	/**
	 * Create a new handler
	 * @param absraction the abstraction layer used in the communication module
	 * @param id The ID of the client
	 * @param socket The socket associated with the client
	 */
	public ConnectionHandler(AbstractionModule absraction, String id, Socket socket) {
		this.id = id;
		this.socket = socket;
		this.abstraction = absraction;
	}
	
	/**
	 * Send data to the client
	 * @param data The data to be sent
	 * @throws CommunicationFailureException Thrown in case of failure
	 */
	public void send(String data) throws CommunicationFailureException {
		if (!socket.isConnected()) {
			throw CommunicationFailureException.FAILED_TO_CONNECT;
		}
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(data);
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_TRANSMIT;
		}
	}
	
	/**
	 * Start receiving data from the client
	 */
	private void receive() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String inputLine;
				
			while ((inputLine = in.readLine()) != null) {
				abstraction.forwardData(id, inputLine);
			}
		} catch (IOException e) {
			// TODO: handle error
			System.err.println("Connection error");
		}
	}
	
	@Override
	public void run() {
		receive();
	}

}
