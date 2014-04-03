package com.example.crystalgame.server.groups;

/**
 * An object describing a client
 * @author Balazs Pete
 *
 */
public class Client {

	private final String id;
	private String name, groupID;
	private long timestamp = -1;
	
	/**
	 * Create a new client
	 * @param id The client's ID
	 * @param name The client's name
	 */
	public Client(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the groupID
	 */
	public String getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	/**
	 * Increment and retrieve the cleint's new timestamp
	 * @return The timestamp
	 */
	public long incrementTimestamp() {
		return ++timestamp;
	}
}
