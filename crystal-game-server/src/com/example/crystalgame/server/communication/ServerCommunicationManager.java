package com.example.crystalgame.server.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.ConnectionHandler;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.util.RandomID;

/**
 * Module responsible for the management of communications on the server side
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class ServerCommunicationManager extends CommunicationManager {

	public static final int MAX_THREADS = 100;
	
	private HashMap<String, ConnectionHandler> connections;
	
	private int port;
	
	private ServerSocket serverSocket;
	private ExecutorService pool;
	
	/**
	 * Create a server specific communication manager
	 * @param port The port to be used
	 */
	public ServerCommunicationManager(int port) {
		connections = new HashMap<String, ConnectionHandler>();
		this.port = port;
	}
	
	/**
	 * Initialise the module
	 * @param port The port to bind to
	 * @throws CommunicationFailureException Thrown in case of an error
	 */
	public void initialise() throws CommunicationFailureException {
		pool = Executors.newFixedThreadPool(MAX_THREADS);
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_INITIALISE;
		}
	}
	
	@Override
	public void run() {
		try {
			initialise();
			handleConnections();
		} catch (CommunicationFailureException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void sendData(String id, Serializable data) throws CommunicationFailureException {
		ConnectionHandler handler = connections.get(id);
		if (handler == null || handler.isClosed()) {
			connections.remove(id);
			throw CommunicationFailureException.FAILED_TO_TRANSMIT;
		}
		
		handler.send(data);
	}
	
	/**
	 * Start handling connections from clients
	 */
	private void handleConnections() {
		while(true) {
			try {
				String id = getRandomId();
				ConnectionHandler connection = new ConnectionHandler(abstraction, id, serverSocket.accept());
				connections.put(id, connection);
				pool.execute(connection);
			} catch (IOException e) {
				// TODO: log this
				System.err.println("Failed to receive");
			}
		}
	}
	
	/**
	 * Get a random ID
	 * @return A random String ID
	 */
	private String getRandomId() {
		String id = null;
		
		do {
			id = RandomID.getRandomId();
		} while (connections.containsKey(id));
		
		return id;
	}

	@Override
	public void sendId(String id, String nodeId) {
		IdMessage message = new IdMessage(nodeId);
		message.setSenderId("SERVER");
		message.setData(nodeId);
		try {
			sendData(id, message);
		} catch (CommunicationFailureException e) {
			System.err.println("Failed to transmit node ID to " + nodeId);
		}
	}
}
