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
	private ListenerManager<MessageEventListener, Message> messageListenerManager;
	private HashSet<String> groupIDs;
	
	/**
	 * Create an incoming messages module
	 * @param abstraction The abstraction layer to be used
	 */
	public IncomingMessages(AbstractionModule abstraction) {
		this.abstraction = abstraction;
		
		initialise();
	}
	
	/**
	 * Add a message event listener
	 * @param listener The listener to be added
	 */
	public void addMessageEventListener(MessageEventListener listener) {
		messageListenerManager.addEventListener(listener);
	}

	/**
	 * Remove a message event listener
	 * @param listener The listener to be removed
	 */
	public void removeMessageEventListener(MessageEventListener listener) {
		messageListenerManager.removeEventListener(listener);
	}
	
	// TODO: add instruction event listeners
	
	/**
	 * Add a group ID to the filter
	 * @param groupId The group ID to allow
	 */
	public void addGroupId(String groupId) {
		groupIDs.add(groupId.intern());
	}
	
	/**
	 * Remove a group ID from the filter
	 * @param groupId The group ID to not allow any more
	 */
	public void removeGroupId(String groupId) {
		groupIDs.remove(groupId.intern());
	}
	
	private void initialise() {
		groupIDs = new HashSet<String>();
		
		messageListenerManager = new ListenerManager<MessageEventListener, Message>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, Message message) {
				listener.messageEvent(message);
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
		
		messageListenerManager.send(message);
		// TODO: instruction extraction/encoding and event send
	}
}
