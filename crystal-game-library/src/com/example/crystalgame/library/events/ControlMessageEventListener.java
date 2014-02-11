package com.example.crystalgame.library.events;

import com.example.crystalgame.library.communication.messages.Message;

/**
 * An event listener for control messages
 * @author Balazs Pete, Rajan Verma
 *
 */
public abstract class ControlMessageEventListener extends MessageEventListener {

	@Override
	public abstract void messageEvent(Message message);

}
