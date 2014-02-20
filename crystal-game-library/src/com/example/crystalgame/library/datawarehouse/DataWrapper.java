package com.example.crystalgame.library.datawarehouse;

import java.io.Serializable;

/**
 * Data wrapper for storing objects in database
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DataWrapper<DATA extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 433510659043193787L;
	
	private String key;
	private DATA value;
	
	/**
	 * Required for querying stored objects in the database
	 * @param key
	 */
	public DataWrapper(String key) {
		this.key = key;
	}
	
	public DataWrapper(String key, DATA value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(DATA value) {
		this.value = value;
	}

}
