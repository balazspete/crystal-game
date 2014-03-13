package com.example.crystalgame.server;

import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.server.communication.ServerCommunication;
import com.example.crystalgame.server.communication.ServerCommunicationManager;
import com.example.crystalgame.server.datawarehouse.ServerDataWarehouse;
import com.example.crystalgame.server.groups.GroupInstanceManager;

/**
 * The server
 * @author Balazs Pete, Shen Chen, Allen Thomas Varghese
 *
 */
public class Server {

	public static final int COMMUNICATION_PORT = 3000;
	public static final String DB_PATH = "./";
	
	private void run() {
		ServerDataWarehouse.DB_PATH = DB_PATH;
		
		final GroupInstanceManager groupInstanceManager = new GroupInstanceManager();
		ServerCommunicationManager manager = new ServerCommunicationManager(COMMUNICATION_PORT); 
		
		final ServerCommunication c = new ServerCommunication(manager, groupInstanceManager);
		c.in.addMessageEventListener(new MessageEventListener(){
			@Override
			public void onMessageEvent(MessageEvent event) {
				groupInstanceManager.forwardMessage(event.getMessage());
			}

			@Override
			public void onGroupStatusMessageEvent(MessageEvent event) {
				groupInstanceManager.handleGroupStatusMessage((GroupStatusMessage) event.getMessage());
			}

			@Override
			public void onControlMessage(MessageEvent event) {
				ControlMessage message = (ControlMessage) event.getMessage();
				groupInstanceManager.handleGroupInstruction(message, (GroupInstruction) message.getData());
			}

			@Override
			public void onInstructionRelayMessage(MessageEvent event) {
				// Case handled in onMessageEvent
			}

			@Override
			public void onIdMessageEvent(MessageEvent event) {
				// Server does not care about this...
			}
		});
	}
	
	public static void main(String[] args) {
		new Server().run();
	}
	
}
