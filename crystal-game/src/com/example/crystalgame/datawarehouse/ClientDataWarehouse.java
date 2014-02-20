package com.example.crystalgame.datawarehouse;

import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;

/**
 * The client version of the DB$OInterface
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ClientDataWarehouse extends DataWarehouse {

	protected ClientDataWarehouse(DB4OInterface store) {
		super(store);
	}

	private static ClientDataWarehouse instance;
	
	/**
	 * Get the instance of the Data warehouse
	 * @return The data warehouse
	 */
	public static ClientDataWarehouse getInstance() {
		if(instance == null) {
			instance = new ClientDataWarehouse(DataStore.getInstance());
		}
		
		return instance;
	}
	
}
