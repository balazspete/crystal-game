package com.example.crystalgame.library.events;

import java.util.EventListener;

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
		this.groupId = groupId == null ? null : groupId;
	}
	
	/**
	 * Listen to all message events
	 * @param message The new message
	 */
	public abstract void onMessageEvent(MessageEvent event);
	
	/**
	 * Listen to GroupStatusMessages
	 * @param message The new group status message
	 */
	public abstract void onGroupStatusMessageEvent(MessageEvent event);
	
	/**
	 * Listen to ControlMessages
	 * @param message The new control message
	 */
	public abstract void onControlMessage(MessageEvent event);
	
	/**
	 * Listen to instruction relay messages
	 * @param event The new instruction relay message
	 */
	public abstract void onInstructionRelayMessage(MessageEvent event);

	/**
	 * Listen to ID message events
	 * @param event The event
	 */
	public abstract void onIdMessageEvent(MessageEvent event);
	
	/**
	 * Implementation of the eventHandlerHelper function of the listener manager
	 * @param listener The listener
	 * @param event The event
	 */
	public static void eventHandlerHelper(MessageEventListener listener, MessageEvent event) {
		// Forward the message to the listener based on its type
		switch(event.getMessage().getMessageType()) {
			case CONTROL_MESSAGE:
				listener.onControlMessage(event);
				break;
			case GROUP_STATUS_MESSAGE:
				listener.onGroupStatusMessageEvent(event);
				break;
			case INSTRUCTION_MESSAGE:
				listener.onInstructionRelayMessage(event);
				break;
			case ID_MESSAGE:
				listener.onIdMessageEvent(event);
				break;
			default:
				listener.onMessageEvent(event);
		}
	}
}
