package com.example.crystalgame.library.events;

import java.util.EventListener;

import com.example.crystalgame.library.communication.messages.Message;

/**
 * The listener used to receive new message events
 * @author Balazs Pete
 *
 */
public abstract class MessageEventListener implements EventListener {

	/**
	 * Listen to all message events
	 * @param message The new message
	 */
	public abstract void messageEvent(Message message);
	
}
