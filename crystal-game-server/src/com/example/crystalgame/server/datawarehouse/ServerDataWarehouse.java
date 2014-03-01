package com.example.crystalgame.server.datawarehouse;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import com.example.crystalgame.library.datawarehouse.DataWarehouse;
import com.example.crystalgame.server.groups.Group;

/**
 * An extension of the data warehouse for the servers
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ServerDataWarehouse extends DataWarehouse {

	public static final String DB_PATH = "/Users/balazspete/Documents/";
	
	protected ServerDataWarehouse(ObjectContainer store, ServerSynchroniser synchroniser) {
		super(store, synchroniser);
	}
	
	public static ServerDataWarehouse getWarehouseForGroup(Group group) {
		ObjectContainer container = createGroupObjectContainer(group.groupId);
		return new ServerDataWarehouse(container, new ServerSynchroniser(container, group));
	}
	
	private static ObjectContainer createGroupObjectContainer(String groupId) {
		return Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH + "group-db-" + groupId);
	}
	
}
