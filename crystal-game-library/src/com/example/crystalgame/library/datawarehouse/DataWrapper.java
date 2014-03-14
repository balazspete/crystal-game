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
	private long version;
	
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
	
	/**
	 * Get the wrapped value
	 * @return The value
	 */
	public HasID getValue() {
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
	public long getVersion() {
		return version;
	}
	
	/**
	 * Get the stringified class value
	 * @return The type
	 */
	public String getType() {
		return type;
	}
	
	/*********
	 * SERIALISATION OVERRIDE
	 *********/
	
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.writeObject(type);
		stream.writeObject(key);
		stream.writeObject(value);
		stream.writeLong(version);
    }

    @SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
    	type = (String) stream.readObject();
    	key = (String) stream.readObject();
    	value = (DATA) stream.readObject();
    	version = (long) stream.readLong();
    }
    
}
