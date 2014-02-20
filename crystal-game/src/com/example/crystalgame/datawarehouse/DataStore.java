package com.example.crystalgame.datawarehouse;

import com.example.crystalgame.library.datawarehouse.DB4OInterface;

/**
 * The client version of the DB$OInterface
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataStore extends DB4OInterface {

	protected DataStore() {
		// TODO: read value from the configuration
		super("CrystalGame");
	}

	private static DB4OInterface instance;
	
	public static DB4OInterface getInstance() {
		if(instance == null) {
			instance = new DataStore();
		}
		
		return instance;
	}
	
}
