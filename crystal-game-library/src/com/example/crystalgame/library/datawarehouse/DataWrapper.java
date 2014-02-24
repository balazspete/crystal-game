package com.example.crystalgame.library.datawarehouse;

import com.example.crystalgame.library.data.HasID;

/**
 * Data wrapper for storing objects in database
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DataWrapper<DATA extends HasID> {
	
	@SuppressWarnings("unused")
	private String key, type;
	
	private DATA value;
	
	/**
	 * Create a DataWapper user for query purposes
	 * @param id The ID of the desired
	 */
	protected DataWrapper(Class<DATA> type, String id) {
		this.type = type.toString();
		this.key = id;
	}
	
	public DataWrapper(Class<DATA> type, DATA value) {
		this(type, value.getID());
		this.value = value;
	}
	
	public HasID getValue() {
		return value;
	}
	
}
