package com.example.crystalgame.server.test.datawarehouse;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.util.RandomID;

class StringWithID implements HasID {
	
	private String id, value;
	
	public StringWithID(String value) {
		this.id = RandomID.getRandomId();
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String getID() {
		return id;
	}
	
}
