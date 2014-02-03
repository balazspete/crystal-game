package com.example.crystalgame.server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.ConnectionHandler;
import com.example.crystalgame.library.util.RandomID;

/**
 * Module responsible for the management of communications on the server side
 * @author Balazs Pete, Shen Chen
 *
 */
public class ServerCommunicationManager extends CommunicationManager {

	public static final int MAX_THREADS = 100;
	
	private ConcurrentHashMap<String, ConnectionHandler> connections;
	
	private ServerSocket serverSocket;
	private ExecutorService pool;
	
	public ServerCommunicationManager() {
		connections = new ConcurrentHashMap<String, ConnectionHandler>();
	}
	
	/**
	 * Initialise the module
	 * @param port The port to bind to
	 * @throws CommunicationFailureException Thrown in case of an error
	 */
	public void initialise(int port) throws CommunicationFailureException {
		pool = Executors.newFixedThreadPool(MAX_THREADS);
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw CommunicationFailureException.FAILED_TO_INITIALISE;
		}
	}
	
	@Override
	public void run() {
		handleConnections();
	}

	@Override
	public void sendData(String id, String data) throws CommunicationFailureException {
		ConnectionHandler handler = connections.get(id);
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
		} while (connections.contains(id));
		
		return id;
	}

}
