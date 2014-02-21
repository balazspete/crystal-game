package com.example.crystalgame.library.datawarehouse;

import java.util.List;

import com.example.crystalgame.library.data.HasID;

/**
 * The Data Warehouse
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataWarehouse {

	private DB4OInterface store;
	
	protected DataWarehouse(DB4OInterface store) {
		this.store = store;
	}
	
	/**
	 * Store a {@link HasID} object in the Data Warehouse
	 * @param type The class of the object
	 * @param value The {@link HasID} to store
	 * @return True if stored successfully
	 * @throws DataWarehouseException Thrown in case of an error or input mismatch
	 */
	public boolean put(@SuppressWarnings("rawtypes") Class type, HasID value) throws DataWarehouseException {
		// TODO: remove and handle by synchroniser/concurrency control module
		return store.put(type, value);
	}

	/**
	 * Get a {@link HasID} stored in the Data Warehouse
	 * @param type The class of the object
	 * @param id The ID of the object
	 * @return The object or null if not found
	 * @throws DataWarehouseException Thrown in case of an error or input mismatch
	 */
	public HasID get(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
		// TODO: ensure concurrency
		return store.get(type, id);
	}

	/**
	 * Delete an object stored in the data warehouse
	 * @param type The class of the object
	 * @param id The ID of the object
	 * @return True if deleted
	 * @throws DataWarehouseException Thrown in case of an error
	 */
	public boolean delete(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
		// TODO: remove and handle by synchroniser/concurrency control
		return store.delete(type, id);
	}

	/**
	 * Get all occurrences of a class in the Data Warehouse
	 * @param type The class
	 * @return The list of result
	 * @throws DataWarehouseException thrown in case of an error
	 */
	public List<HasID> getAll(@SuppressWarnings("rawtypes") Class type) throws DataWarehouseException {
		// TODO: ensure concurrency control
		return store.getAll(type);
	}
	
}
