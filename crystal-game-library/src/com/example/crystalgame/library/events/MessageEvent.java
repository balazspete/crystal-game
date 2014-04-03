package com.example.crystalgame.library.events;

import com.example.crystalgame.library.communication.messages.Message;

/**
 * An event to describe the presence of a new event
 * @author Balazs Pete, Shen Chen
 *
 */
public class MessageEvent extends Event {

	private Message message;
	private String senderId, receiverId;
	
	/**
	 * Create a new message event
	 * @param message the message
	 */
	public MessageEvent(Message message) {
		this.message = message;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the receiverId
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}
	
}
