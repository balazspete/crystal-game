package com.example.crystalgame.library.events;

import java.util.EventListener;

import com.example.crystalgame.library.communication.messages.Message;

/**
 * The listener used to receive new message events
 * @author Balazs Pete, Allen Thomas Varghese, Rajan Verma
 *
 */
public abstract class MessageEventListener implements EventListener {

	public final String groupId;
	
	/**
	 * Create a Message event listener
	 */
	public MessageEventListener() {
		this(null);
	}
	
	/**
	 * Create a message event listener listening to message events from only a specific group
	 * @param groupId The group ID
	 */
	public MessageEventListener(String groupId) {
		this.groupId = groupId == null ? null : groupId.intern();
	}
	
	/**
	 * Listen to all message events
	 * @param message The new message
	 */
	public abstract void messageEvent(Message message);

}
