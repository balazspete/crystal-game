package com.example.crystalgame.library.communication.incoming;

import java.util.HashSet;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEventListener;

/**
 * The interface responsible for managing incoming messages
 * @author Balazs Pete, Shen Chen
 *
 */
public class IncomingMessages {

	private AbstractionModule abstraction;
	private ListenerManager<MessageEventListener, Object> listenerManager;
	private HashSet<String> groupIDs;
	
	public IncomingMessages(AbstractionModule abstraction) {
		this.abstraction = abstraction;
		
		initialise();
	}
	
	private void initialise() {
		groupIDs = new HashSet<String>();
		
		listenerManager = new ListenerManager<MessageEventListener, Object>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, Object data) {
				
			}
		};
		// TODO: add instruction listener manager
		
		abstraction.addEventListener(new MessageEventListener(){
			@Override
			public void messageEvent(Message message) {
				handleMessage(message);
			}
		});
	}
	
	private void handleMessage(Message message) {
		if (message.getGroupId() != null && !groupIDs.contains(message.getGroupId().intern())) {
			return;
		}
		
		listenerManager.send(message.getData());
		// TODO: instruction extraction/encoding and event send
	}

	public void addMessageEventListener(MessageEventListener listener) {
		listenerManager.addEventListener(listener);
	}

	public void removeMessageEventListener(MessageEventListener listener) {
		listenerManager.removeEventListener(listener);
	}
	
	// TODO: add instruction event listener
	
	
	
	
	
	
	public void addGroupId(String groupId) {
		groupIDs.add(groupId.intern());
	}
	
	public void removeGroupId(String groupId) {
		groupIDs.remove(groupId.intern());
	}
}
