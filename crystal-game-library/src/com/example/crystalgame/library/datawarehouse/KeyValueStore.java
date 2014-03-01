package com.example.crystalgame.library.datawarehouse;

import java.util.List;

import com.example.crystalgame.library.data.HasID;

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
	public HasID put(@SuppressWarnings("rawtypes") Class type, HasID value);
	
	/**
	 * Get a value using the associated key
	 * @param key The key
	 * @return The value or null
	 */
	public HasID get(@SuppressWarnings("rawtypes") Class type, String key);
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public List<HasID> getAll(@SuppressWarnings("rawtypes") Class type);
	
	/**
	 * Delete a value in the store
	 * @param key The key of the value
	 * @return True if removed
	 */
	public boolean delete(@SuppressWarnings("rawtypes") Class type, String key);
	
}
