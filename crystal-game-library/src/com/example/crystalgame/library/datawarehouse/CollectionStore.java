package com.example.crystalgame.library.datawarehouse;

import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.library.data.HasID;

/**
 * An object used to help the querying of collections
 * @author Balazs Pete, Allen Thomas Varghese
 *
 * @param <DATA> The querried class
 */
public class CollectionStore<DATA extends HasID> {

	private DataWarehouse warehouse;
	private Class<DATA> type;	
	
	protected CollectionStore(DataWarehouse warehouse, Class<DATA> type) {
		this.warehouse = warehouse;
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Get all occurrences of the queried class
	 * @return The result
	 */
	public List<DATA> getAll() {
		try {
			return (List<DATA>) warehouse.getAll(type);
		} catch (DataWarehouseException e) {
			return new ArrayList<DATA>();
		}
	}
	
	/**
	 * Save all objects in the list
	 * @param data The list to save
	 */
	public void putList(List<DATA> data) {
		for(DATA d : data) {
			put(d);
		}
	}
	
	/**
	 * Save one object
	 * @param data The object to save
	 * @return True if saved
	 */
	public boolean put(DATA data) {
		try {
			return warehouse.put(type, data);
		} catch (DataWarehouseException e) {
			return false;
		}
	}
	
}
