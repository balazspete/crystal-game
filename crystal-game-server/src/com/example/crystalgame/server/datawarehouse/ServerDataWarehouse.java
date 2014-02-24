package com.example.crystalgame.server.datawarehouse;

import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;

public class ServerDataWarehouse extends DataWarehouse {

	protected ServerDataWarehouse(DB4OInterface store) {
		super(store);
	}

	public static ServerDataWarehouse getWarehouseForGroup(String groupId) {
		return new ServerDataWarehouse(DataStore.getGameStore(groupId));
	}
	
}
