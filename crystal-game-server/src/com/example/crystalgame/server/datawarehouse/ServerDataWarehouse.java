package com.example.crystalgame.server.datawarehouse;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;
import com.example.crystalgame.library.datawarehouse.LockManager;
import com.example.crystalgame.library.instructions.DataTransferInstruction;
import com.example.crystalgame.server.groups.Group;

/**
 * An extension of the data warehouse for the servers
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ServerDataWarehouse extends DataWarehouse {

	// Need to set up value in Server
	public static String DB_PATH = "";
	
	protected ServerDataWarehouse(ObjectContainer store, ServerSynchroniser synchroniser) {
		super(store, synchroniser);
	}
	
	public static ServerDataWarehouse getWarehouseForGroup(Group group) {
		ObjectContainer container = createGroupObjectContainer(group.groupId);
		LockManager manager = new LockManager();
		return new ServerDataWarehouse(container, new ServerSynchroniser(container, group, manager));
	}
	
	private static ObjectContainer createGroupObjectContainer(String groupId) {
		return Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH + "group-db-" + groupId);
	}
	
	public DataTransferInstruction getDownloadReply(DataTransferInstruction instruction)
	{
		DB4OInterface i = new DB4OInterface(this.lockManager, this.db); 
		
		DataTransferInstruction reply = 
				DataTransferInstruction.createDataWarehouseDownloadReplyInstruction(i.getAllWrappers().toArray(new Serializable[0]));
		return reply;
	}
	
} 
