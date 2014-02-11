package com.example.crystalgame;

import android.app.Application;

import com.example.crystalgame.communication.ClientCommunication;

/**
 * The Android application class for the CrystalGame project
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class CrystalGame extends Application {

	private ClientCommunication communication;
	
	public void addCommunication(ClientCommunication communication) {
		this.communication = communication;
	}
	
	/**
	 * Get the communication module
	 * @return the communication module
	 */
	public ClientCommunication getCommunication() {
		return communication;
	}
	
	/**
	 * Change the address to connect to
	 * @param address The new address
	 */
	public void serverAddressChange(String address) {
		communication.serverAddressChange(address);
	}
	
	/**
	 * Change the port used for the connection
	 * @param port The new port number
	 */
	public void portChange(int port) {
		communication.portChange(port);
	}
}
