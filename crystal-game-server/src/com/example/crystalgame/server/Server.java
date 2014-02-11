package com.example.crystalgame.server;

import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.ControlMessageEventListener;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.communication.ServerCommunication;
import com.example.crystalgame.server.communication.ServerCommunicationManager;
import com.example.crystalgame.server.communication.ServerOutgoingMessages;
import com.example.crystalgame.server.groups.GroupInstanceManager;

/**
 * The server
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class Server {

	public static void main(String[] args) {
		// TODO
		final GroupInstanceManager groupInstanceManager = new GroupInstanceManager();
		
		ServerCommunicationManager manager = new ServerCommunicationManager(3000); 
		
		final ServerCommunication c = new ServerCommunication(manager, groupInstanceManager);
		c.in.addControlMessageEventListener(new ControlMessageEventListener() {
			@Override
			public void messageEvent(Message message) {
				// Forward Control messages to the group instance manager
				groupInstanceManager.processControlMessage((ControlMessage) message);
			}
		});
		
		
		// Just for testing...
		c.in.addMessageEventListener(new MessageEventListener(){
			@Override
			public void messageEvent(Message message) {
				System.out.println(message.getSenderId() + "--" + message.getData());
				((ServerOutgoingMessages) c.out).sendTestDataToClient(message.getSenderId(), message.getData() + " - received");
			}
		});
	}
	
}
