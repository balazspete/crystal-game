package com.example.crystalgame.library.communication;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.communication.messages.Message;

/**
 * A runnable to handle individual client connections
 * @author Balazs Pete, Shen Chen
 *
 */
public class ConnectionHandler implements Runnable {

	private String id, myID;
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
	public ConnectionHandler(AbstractionModule absraction, String myID, String id, Socket socket) {
		this.id = id;
		this.myID = myID;
		this.socket = socket;
		this.abstraction = absraction;
	}
	
	/**
	 * Send data to the client
	 * @param data The data to be sent
	 * @throws CommunicationFailureException Thrown in case of failure
	 */
	public void send(Serializable data) throws CommunicationFailureException {
		if (!socket.isConnected() || out == null) {
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
				// Get an object from the input stream (block until received)
				Object inputObject = in.readObject();		
				abstraction.forwardData(id, inputObject);
			}
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_RECEIVE;
		} catch (ClassNotFoundException e) {
			throw CommunicationFailureException.FAILED_TO_DESERIALISE;
		} finally {
			try {
				// Try to close the input stream
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
			if (myID != null) {
				sendMyID();
			}
			receive();
		} catch (EOFException e) {
			System.err.println(e.getMessage());
		} catch (CommunicationFailureException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				// Try to close the output stream
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.err.println("Connection closed");
			}
		}
	}
	
	private int tries = 0;
	private void sendMyID() {
		Message message = new IdMessage(id);
		message.setSenderId(myID);
		
		try {
			send(message);
		} catch (CommunicationFailureException e) {
			if (++tries < 100) {
				sendMyID();
			}
		}
	}
}
