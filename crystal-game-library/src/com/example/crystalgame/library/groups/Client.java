package com.example.crystalgame.library.groups;

public class Client {

	private final String id;
	private String name;
	
	public Client(String id, String name) {
		this.id = id.intern();
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
	
	
}
