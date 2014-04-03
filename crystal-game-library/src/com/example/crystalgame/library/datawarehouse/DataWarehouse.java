package com.example.crystalgame.library.datawarehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;

/**
 * The Data Warehouse
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataWarehouse {

	private Synchronizer synchronizer;
	protected ObjectContainer db;
	private ExecutorService pool;
	
	protected LockManager lockManager;
	
	protected DataWarehouse(ObjectContainer db, Synchronizer synchroniser) {
		this.db = db;
		this.synchronizer = synchroniser;
		this.pool = Executors.newScheduledThreadPool(20);
		lockManager = new LockManager();
	}
	
	/**
	 * Store a {@link HasID} object in the Data Warehouse
	 * @param type The class of the object
	 * @param value The {@link HasID} to store
	 * @return True if stored successfully
	 * @throws DataWarehouseException Thrown in case of an error or input mismatch
	 */
	public HasID put(@SuppressWarnings("rawtypes") Class type, HasID value) throws DataWarehouseException {
		List<HasID> values = new ArrayList<HasID>();
		values.add(value);
		values = putList(type, values);
		
		if (values.size() > 0) {
			return values.get(0);
		}
		
		return null;
	}
	
	/**
	 * Update a list of HasID elements
	 * @param type The type of the elements in the list
	 * @param value The elements to update
	 * @return The new values
	 * @throws DataWarehouseException Thrown in case of an error
	 */
	public List<HasID> putList(@SuppressWarnings("rawtypes") Class type, List<HasID> value) throws DataWarehouseException {
		if (type == null || value == null || value.size() == 0) {
			return Collections.emptyList();
		}
		
		DB4OInterface store = new DB4OInterface(lockManager, db);

		System.out.println("DataWarehouse|putList: Initiating PutList operation...");
		
		FutureTask<Boolean> future = new FutureTask<Boolean>(
				new DataWarehouseUpdateTask(
						synchronizer,
						DataSynchronisationInstruction.createUpdateRequestInstruction(type, value)));
		pool.submit(future);
		
		List<HasID> result = new ArrayList<HasID>();
		try {
			future.get();
//			System.out.println("DataWarehouse|putList: PutList operation successful, returning results");
			for (HasID v : value) {
				result.add(store.get(type, v.getID()));
			} 
		} catch (Exception e) {
//			System.out.println("DataWarehouse|putList: PutList operation failed, rolling back");
			e.printStackTrace();
			throw DataWarehouseException.FAILED_TO_UPDATE;
		}
		
		return result;
	}

	/**
	 * Get a {@link HasID} stored in the Data Warehouse
	 * @param type The class of the object
	 * @param id The ID of the object
	 * @return The object or null if not found
	 * @throws DataWarehouseException Thrown in case of an error or input mismatch
	 */
	public HasID get(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
		// Just read local copy
		return new DB4OInterface(lockManager, db).get(type, id);
	}

	/**
	 * Delete an object stored in the data warehouse
	 * @param type The class of the object
	 * @param id The ID of the object
	 * @return True if deleted
	 * @throws DataWarehouseException Thrown in case of an error
	 */
	public boolean delete(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
		List<String> values = new ArrayList<String>();
		values.add(id);
		return deleteList(type, values);
	}
	
	/**
	 * Delete a list of of objects stored in the data warehouse
	 * @param type The type of the objects
	 * @param idsThe list of object IDs
	 * @return true of successful
	 * @throws DataWarehouseException
	 */
	public boolean deleteList(@SuppressWarnings("rawtypes") Class type, List<String> ids) throws DataWarehouseException {
		System.out.println("DataWarehouse|deleteList: Initiating deleteList operation...");
		
		FutureTask<Boolean> future = new FutureTask<Boolean>(
				new DataWarehouseUpdateTask(
						synchronizer,
						DataSynchronisationInstruction.createDeleteRequestInstruction(type, ids)));
		pool.submit(future);
		
		try {
			future.get();
//			System.out.println("DataWarehouse|deleteList: Successful deleteList operation");
			ids = null;
			return true;
		} catch (Exception e) {
//			System.out.println("DataWarehouse|deleteList: Failed to execute deleteList operation");
			return false;
		}
	}

	/**
	 * Get all occurrences of a class in the Data Warehouse
	 * @param type The class
	 * @return The list of result
	 * @throws DataWarehouseException thrown in case of an error
	 */
	public List<HasID> getList(@SuppressWarnings("rawtypes") Class type) throws DataWarehouseException {
		// Just read local copy
		System.out.println("DataWarehouse|getList: Retrieving desired list for " + type + " items.");
		return new DB4OInterface(lockManager, db).getAll(type);
	}
	
	/**
	 * Forward an instruction event to the DW
	 * @param event The instruction
	 */
	public void passInstruction(DataSynchronisationInstruction instruction) {
//		System.out.println("DataWarehouse|passInstruction: Forwarding instruction to synchroniser. TransactionID=" + instruction.getTransactionID() + " Type=" + instruction.getDataSynchronisationInstructiontype());
		synchronizer.passInstruction(instruction);
	}
	
	public void addInstructionEventListener(InstructionEventListener listener) {
		synchronizer.addInstructionEventListener(listener);
	}
	
	public void removeInstructionEventListener(InstructionEventListener listener) {
		synchronizer.removeInstructionEventListener(listener);
	}
	
}
