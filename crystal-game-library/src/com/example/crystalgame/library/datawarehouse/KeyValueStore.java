package com.example.crystalgame.library.datawarehouse;

import java.io.Serializable;
import java.util.List;

/**
 * An interface describing the expected behaviour of a database
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public interface KeyValueStore {

	/**
	 * Put a value into the database with an associated key
	 * @param key The key of the value
	 * @param value The value to store
	 * @return True if successful
	 */
	public boolean put(String key, Serializable value);
	
	/**
	 * Get a value using the associated key
	 * @param key The key
	 * @return The value or null
	 */
	public Serializable get(String key);
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public List<Serializable> getAll(Class c);
	
	/**
	 * Delete a value in the store
	 * @param key The key of the value
	 * @return True if removed
	 */
	public boolean delete(String key);
	
}
