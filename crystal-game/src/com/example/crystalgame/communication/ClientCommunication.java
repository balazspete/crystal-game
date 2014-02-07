package com.example.crystalgame.communication;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.CommunicationManager;

public class ClientCommunication extends Communication {
	
	public final ClientOutgoingMessages out;
	
	public ClientCommunication(CommunicationManager manager, ClientOutgoingMessages outgoing) {
		super(manager, outgoing);
		out = (ClientOutgoingMessages) super.out;
	}
	
	public void serverAddressChange(String address) {
		System.out.println("changing address");
		manager.setServerAddress(address);
	}
	
	public void portChange(int port) {
		System.out.println("changing port");
		manager.setPort(port);
	}

}
