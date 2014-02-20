package com.example.crystalgame.library.datawarehouse;

import java.util.List;

import com.example.crystalgame.library.data.Location;

public class CollectionStore<DATA extends Location> {

	private DataWarehouse warehouse;
	private Class<DATA> type;	
	protected CollectionStore(Class<DATA> type, DataWarehouse warehouse) {
		this.warehouse = warehouse;
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	public List<DATA> getAll() {
		return (List<DATA>) warehouse.getAll(type);
	}
	
	public void putList(List<DATA> data) {
		for(DATA d : data) {
			put(d);
		}
	}
	
	public boolean put(DATA data) {
		return warehouse.put(createKey(data), data);
	}
	
	protected String createKey(DATA data) {
		return type.toString() + "" + data.getID();
	}
	
}
