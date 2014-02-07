package com.example.crystalgame;

import android.app.Application;

import com.example.crystalgame.communication.ClientCommunication;

public class CrystalGame extends Application {

	private ClientCommunication communication;
	
	public void addCommunication(ClientCommunication communication) {
		this.communication = communication;
	}
	
	public ClientCommunication getCommunication() {
		return communication;
	}
	
	public void serverAddressChange(String address) {
		communication.serverAddressChange(address);
	}
	
	public void portChange(int port) {

		System.out.println("changing port");
		communication.portChange(port);
	}
}
