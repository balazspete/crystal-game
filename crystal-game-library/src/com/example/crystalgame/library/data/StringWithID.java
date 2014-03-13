package com.example.crystalgame.library.data;

import java.io.Serializable;

import com.example.crystalgame.library.util.RandomID;

public class StringWithID implements HasID, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1010812153100710048L;
	
	private String id, value;
	
	public StringWithID(String value) {
		this.id = RandomID.getRandomId();
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getID() {
		return id;
	}
	
}
