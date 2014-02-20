package com.example.crystalgame.library.datawarehouse;

import java.io.Serializable;
import java.util.List;

public class DataWarehouse {

	private DB4OInterface store;
	
	protected DataWarehouse(DB4OInterface store) {
		this.store = store;
	}
	
	public boolean put(String key, Serializable value) {
		// TODO: remove and handle by synchroniser/concurrency control module
		return store.put(key, value);
	}

	public Serializable get(String key) {
		// TODO: ensure concurrency
		return store.get(key);
	}

	public boolean delete(String key) {
		// TODO: remove and handle by synchroniser/concurrency control
		return store.delete(key);
	}

	public List<Serializable> getAll(Class<?> c) {
		// TODO: ensure concurrency control
		return store.getAll(c);
	}
	
}
