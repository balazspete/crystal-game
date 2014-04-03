package com.example.crystalgame.library.communication.messages;

import java.io.Serializable;

/**
 * A generic Message object
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public abstract class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8692134014882762994L;

	private String senderId, groupId;
	private long timeStamp = -1;
	private MessageType messageType; 
	protected String receiverId;
	
	protected Serializable data;
	
	/**
	 * Create a Message
	 * @param messageType The type of the message
	 */
	public Message(MessageType messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * Get the sender's ID
	 * @return The id of the sender
	 */
	public String getSenderId() {
		return senderId;
	}
	
	/**
	 * Set the sender's ID
	 * @param senderId The sender's ID
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	/**
	 * Set the group the message belongs to
	 * @param groupId The group ID
	 */
	public void setGroup(String groupId) {
		this.groupId = groupId; 
	}
	
	/**
	 * Get the ID of group the message is destined for 
	 * @return The group ID
	 */
	public String getGroupId() {
		return groupId;
	}
	
	/**
	 * Set the timestamp of the message
	 * @param timeStamp The timestamp to be assigned
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Get the timestamp of the message
	 * @return The timestamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Get the type of the message
	 * @return the type of the message
	 */
	public MessageType getMessageType() {
		return messageType;
	}
	
	/**
	 * Get the receiver's ID
	 * @return The receiver node's ID
	 */
	public String getReceiverId() {
		return receiverId;
	}
	
	/**
	 * Set the receiver's ID
	 * @param receiverId The receiver node's ID
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	/**
	 * Set the data to be carried by the message
	 * @param data
	 */
	public void setData(Serializable data) {
		this.data = data;
	}
	
	/**
	 * Get the data carried by the message
	 * @return
	 */
	public Serializable getData() {
		return data;
	}
	
	/**
	 * Determine if the message is a multicast message
	 * @return true if the message is a multicast message
	 */
	public abstract boolean isMulticastMessage();
	
}
