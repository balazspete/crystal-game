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
	
	private volatile transient ReentrantReadWriteLock lock;
	
	/**
	 * Create a DataWapper user for query purposes
	 * @param id The ID of the desired
	 */
	protected DataWrapper(Class<DATA> type, String id) {
		this.type = type.toString();
		this.key = id;
		
		lock = new ReentrantReadWriteLock(true);
	}
	
	public DataWrapper(Class<DATA> type, DATA value) {
		this(type, value.getID());
		this.value = value;
	}
	
	public HasID getValue() {
		lock.readLock().lock();
		try {
			return value;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public void setValue(DATA value) throws DataWarehouseException {
		if (!value.getID().equals(this.value.getID())) {
			throw DataWarehouseException.MISMATCHING_ID_EXCEPTION;
		} else if (value.getClass().equals(this.value.getClass())) {
			throw DataWarehouseException.MISMATCHING_TYPE_EXCEPTION;
		}
		
		lock.readLock().lock();
		lock.writeLock().lock();
		try {
			this.value = value;
		} finally {
			lock.writeLock().unlock();
			lock.readLock().unlock();
		}
	}
	
	public boolean isWriteLocked() {
		return lock.isWriteLocked();
	}
	
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
		stream.writeObject(type);
		stream.writeObject(key);
		stream.writeObject(value);
    }

    @SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
    	type = (String) stream.readObject();
    	key = (String) stream.readObject();
    	value = (DATA) stream.readObject();
    	lock = new ReentrantReadWriteLock(true);
    }
    
}
