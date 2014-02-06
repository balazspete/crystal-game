package com.example.crystalgame.server;

import com.example.crystalgame.library.communication.Communication;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.communication.ServerCommunicationManager;
import com.example.crystalgame.server.communication.ServerOutgoingMessages;

public class Server {

	public static void main(String[] args) {
		ServerCommunicationManager manager = new ServerCommunicationManager(3000); 
		
		Communication c = new Communication(manager, new ServerOutgoingMessages());
		c.in.addMessageEventListener(new MessageEventListener(){

			@Override
			public void messageEvent(Message message) {
				System.out.println(message.getData());
			}
			
		});
		
	}
	
}
