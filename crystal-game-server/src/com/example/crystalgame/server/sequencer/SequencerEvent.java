package com.example.crystalgame.server.sequencer;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.Event;

public class SequencerEvent extends Event {
	
	private String receiverId;
	private Message message;
	
	public SequencerEvent(String receiverId, Message message) {
		this.receiverId = receiverId;
		this.message = message;
	}

	/**
	 * @return the receiverId
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}
	
}
