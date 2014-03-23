/**
 * 
 */
package com.example.crystalgame.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.ConnectionHandler;
import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.communication.messages.Message;

/**
 * Client side implementation of {@link CommunicationManager}
 * 
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ClientCommunicationManager extends CommunicationManager {

	public static final int DELAY = 2000;
	
	private Socket socket;
	private ConnectionHandler handler;
	
	/**
	 * Create a CommunicationManager for the client
	 * @param address The address of the remote to connect to
	 * @param port The port number to use for the connection
	 */
	public ClientCommunicationManager(String address, int port) {
		CommunicationManager.address = address;
		CommunicationManager.port = port;
	}
	
	@Override
	public void sendData(String id, Serializable data)
			throws CommunicationFailureException {
		if (handler == null) {
			throw CommunicationFailureException.FAILED_TO_TRANSMIT;
		}
		
		handler.send(data);
		Log.d("ClientCommunicationManager", "Sent message to " + address);
	}
	
	@Override
	public void run() {
		while (true) {
			socket = null;
			handler = null;

			try {
				Log.i("ComunicationManager", "Attempting to connect to " + address);
				
				// Establish a connection...
				socket = new Socket(address, port);
				handler = new ConnectionHandler(this.abstraction, CrystalGame.getClientID(), "SERVER", socket);
				
				Log.i("ComunicationManager", "Connection to " + address + " successful!");
				
				// Run the handler in this thread
				handler.run();
			} catch (UnknownHostException e) {
				Log.e("ClientCommunicationManager", "Connection failed, unknown remote " + address);
			} catch (IOException e) {
				Log.e("ClientCommunicationManager", "Failed to open connection to " + address);
			} finally {
				if (socket != null) {
					try {
						socket.close();
						
						Log.i("ClientCommunicationManager", "Connection to " + address + " closed");
					} catch (IOException e) {
						Log.e("ClientCommunicationManager", "Connection already closed");
					}
				} else {
					Log.i("ClientCommunicationManager", "No socket to close");
				}
			}
			try {
				Log.i("ClientCommunicationManager", "Sleeping for " + DELAY + " seconds before reconnecting...");
				sleep (DELAY);
			} catch (InterruptedException e) {
				Log.e("ClientCommunicationMnaager", "Failed to sleep");
			}
		}
	}

	@Override
	public void sendId(String id, String nodeId) {
		// Ignored
	}

	@Override
	public boolean isClient() {
		return true;
	}
	
}
