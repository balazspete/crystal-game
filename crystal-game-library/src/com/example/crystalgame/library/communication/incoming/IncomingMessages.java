package com.example.crystalgame.library.communication.incoming;

import java.util.HashSet;

import com.example.crystalgame.library.communication.abstraction.AbstractionModule;
import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;

/**
 * The interface responsible for managing incoming messages
 * @author Balazs Pete, Shen Chen
 *
 */
public abstract class IncomingMessages {

	protected AbstractionModule abstraction;
	protected ListenerManager<MessageEventListener, MessageEvent> messageListenerManager;
	protected HashSet<String> groupIDs;
	
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
		
		// Add an event listener manager for message events
		messageListenerManager = new ListenerManager<MessageEventListener, MessageEvent>() {
			@Override
			protected void eventHandlerHelper(MessageEventListener listener, MessageEvent event) {
				MessageEventListener.eventHandlerHelper(listener, event);
			}
		};
		
		// TODO: add instruction listener manager
		
		// Subscribe to events from the abstraction layer
		abstraction.addEventListener(new MessageEventListener(){

			@Override
			public void messageEvent(MessageEvent event) {
				handleMessage(event.getMessage());
			}

			@Override
			public void groupStatusMessageEvent(MessageEvent event) {
				handleGroupStatusMessage((GroupStatusMessage) event.getMessage());
			}

			@Override
			public void controlMessage(MessageEvent event) {
				handleControlMessage((ControlMessage) event.getMessage());
			}
		});
	}
	
	/**
	 * Handler for message events by the {@link AbstractionModule}
	 * @param message The message
	 */
	protected abstract void handleMessage(Message message);
	
	/**
	 * Handler for group status message events by the {@link AbstractionModule}
	 * @param message The message
	 */
	protected abstract void handleGroupStatusMessage(GroupStatusMessage message);
	
	/**
	 * Handler for control message events by the {@link AbstractionModule}
	 * @param message The message
	 */
	protected abstract void handleControlMessage(ControlMessage message);
}
