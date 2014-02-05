/**
 * 
 */
package com.example.crystalgame.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.ConnectionHandler;

/**
 * Client side implementation of {@link CommunicationManager}
 * 
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ClientCommunicationManager extends CommunicationManager {

	private Socket socket;
	private ConnectionHandler handler;
	
	private String address;
	private int port;
	
	public ClientCommunicationManager(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void sendData(String id, String data)
			throws CommunicationFailureException {
		// TODO Auto-generated method stub
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
				
				socket = new Socket(address, port);
				handler = new ConnectionHandler(this.abstraction, "", socket);
				Log.i("ComunicationManager", "Connection to " + address + "successful!");
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
		}
	}

}
