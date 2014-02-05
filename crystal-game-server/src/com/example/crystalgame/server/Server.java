package com.example.crystalgame.server;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.server.communication.ServerCommunicationManager;

public class Server {

	public static void main(String[] args) {
		ServerCommunicationManager manager = new ServerCommunicationManager(3000); 
		
		new Communication(manager);
		
	}
	
}
