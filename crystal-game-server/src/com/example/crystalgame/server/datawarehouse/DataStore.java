package com.example.crystalgame.server.datawarehouse;

import com.example.crystalgame.library.datawarehouse.DB4OInterface;

public class DataStore extends DB4OInterface {

	protected DataStore(String dbName) {
		super(dbName);
	}
	
	public static DataStore getGameStore(String groupId) {
		// TODO: Read path value from the configuration
		String path = "";
		return new DataStore(path + "-groupID-" + groupId);
	}
	
}
