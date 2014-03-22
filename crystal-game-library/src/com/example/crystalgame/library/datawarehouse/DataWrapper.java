package com.example.crystalgame.library.datawarehouse;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.example.crystalgame.library.data.HasID;

/**
 * Data wrapper for storing objects in database
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DataWrapper<DATA extends HasID> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8732618089860011400L;
	
	private String key, type;
	private DATA value;
	private int version;
	
	public DataWrapper() {}
	
	/**
	 * Create a DataWapper user for query purposes
	 * @param id The ID of the desired
	 */
	protected DataWrapper(String type, String id) {
		this.type = type;
		this.key = id;
		
		version = 1;
	}
	
	/**
	 * Create a wrapper for a data entry
	 * @param type The class of the entry
	 * @param value The entry
	 */
	public DataWrapper(Class<DATA> type, DATA value) {
		this(type.toString(), value);
	}
	
	/**
	 * Create a wrapper for a data entry
	 * @param type The stringified class of the entry
	 * @param value The entry
	 */
	public DataWrapper(String type, DATA value) {
		this(type, value.getID());
		this.value = value;
	}
	
	public DataWrapper(DataWrapper<DATA> wrapper) {
		this(wrapper.getType(), wrapper.getValue());
		this.version = wrapper.getVersion();
	}
	
	/**
	 * Get the wrapped value
	 * @return The value
	 */
	public DATA getValue() {
		return value;
	}
	
	/**
	 * Update the wrapped value
	 * @param value The new value
	 * @throws DataWarehouseException Thrown in case the ID or the types do not match
	 */
	public void setValue(DATA value) throws DataWarehouseException {
		// Make sure the input and the wrapped object are the same
		if (!value.getID().equals(this.value.getID())) {
			throw DataWarehouseException.MISMATCHING_ID_EXCEPTION;
		} else if (value.getClass().equals(this.value.getClass())) {
			throw DataWarehouseException.MISMATCHING_TYPE_EXCEPTION;
		}
		
		this.value = value;
		version++;
	}
	
	/**
	 * Get the version of the data
	 * @return The version number
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Get the stringified class value
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
