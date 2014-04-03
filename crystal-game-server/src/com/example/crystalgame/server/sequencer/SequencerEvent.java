package com.example.crystalgame.server.sequencer;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.MessageEvent;

/**
 * A sequencer message event
 * @author Balazs Pete
 *
 */
public class SequencerEvent extends MessageEvent {
	
	/**
	 * Create a new Sequencer message event
	 * @param receiverId The receiver's ID
	 * @param message The message
	 */
	public SequencerEvent(String receiverId, Message message) {
		super(message);
		setReceiverId(receiverId);
	}

}
