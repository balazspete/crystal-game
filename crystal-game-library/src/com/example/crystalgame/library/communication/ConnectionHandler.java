package com.example.crystalgame.library.communication;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
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
	public void send(Serializable data) throws CommunicationFailureException {
		if (!socket.isConnected()) {
			throw CommunicationFailureException.FAILED_TO_CONNECT;
		}
		
		try {
			System.out.println("Sending message: " + data instanceof Serializable);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(data);
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_TRANSMIT;
		}
	}
	
	/**
	 * Determine if the connection has been closed
	 * @return True if the connection has been closed
	 */
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	/**
	 * Start receiving data from the client
	 * @throws IOException 
	 */
	private void receive() {
		ObjectInputStream in = null;
		
		while (!socket.isClosed()) {
			in = null;
			
			try {
				in = new ObjectInputStream(socket.getInputStream());

				Object inputObject;		
				while ((inputObject = in.readObject()) != null) {
					abstraction.forwardData(id, inputObject);
				}
			} catch (IOException e) {
				// TODO: handle error
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO: handle error
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	@Override
	public void run() {
		receive();
	}

}
