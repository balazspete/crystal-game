package com.example.crystalgame.library.communication;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
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
			out.writeObject(data);
			out.flush();
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
	private void receive() throws CommunicationFailureException, EOFException {
		try {
			in = new ObjectInputStream(socket.getInputStream());

			while(true) {
				Object inputObject = in.readObject();		
				abstraction.forwardData(id, inputObject);
			}
			
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_RECEIVE;
		} catch (ClassNotFoundException e) {
			throw CommunicationFailureException.FAILED_TO_DESERIALISE;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				System.err.println("Failed to correctly close socket");
			}
		}
	}
	
	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			receive();
		} catch (EOFException e) {
			System.err.println(e.getMessage());
		} catch (CommunicationFailureException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.err.println("Connection closed");
			}
		}
	}

}
