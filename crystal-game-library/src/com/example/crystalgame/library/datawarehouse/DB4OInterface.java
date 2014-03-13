package com.example.crystalgame.library.datawarehouse;

import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.example.crystalgame.library.data.HasID;
import com.sun.org.apache.bcel.internal.util.Class2HTML;

/**
 * DB Connection class for Android
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DB4OInterface implements KeyValueStore {
	
    private ObjectContainer db;    
    private List<DataWrapper<HasID>> pending;
	
    /**
     * Create a DB4OInterface with the input container
     * @param container the container
     */
	public DB4OInterface(ObjectContainer container) {
		this.db = container;
		this.pending = new ArrayList<DataWrapper<HasID>>();
	}
    
	@Override
	public HasID put(@SuppressWarnings("rawtypes") Class type, HasID value) {
		DataWrapper<HasID> wrapper = getWrapper(type, value.getID());
		if (wrapper == null) {
			wrapper = new DataWrapper<HasID>(type, value);
		} else {
			try {
				wrapper.setValue(value);
			} catch (DataWarehouseException e) {
				return null;
			}
		}
		
		db.store(wrapper);
		pending.add(wrapper);
		return get(type, value.getID());
	}
	
	public HasID put(String type, HasID value) {
		DataWrapper<HasID> wrapper = getWrapper(type, value.getID());
		if (wrapper == null) {
			wrapper = new DataWrapper<HasID>(type, value);
		} else {
			try {
				wrapper.setValue(value);
			} catch (DataWarehouseException e) {
				return null;
			}
		}
		
		db.store(wrapper);
		pending.add(wrapper);
		return get(type, value.getID());
	}

	@Override
	public HasID get(@SuppressWarnings("rawtypes") Class type, String key) {
		return get(type.toString(), key);
	}
	
	public HasID get(String type, String key) {
		DataWrapper<HasID> result = getWrapper(type, key);
		if(result != null) {
			return result.getValue();
		}
		
		return null;
	}
	
	@Override
	public List<HasID> getAll(@SuppressWarnings("rawtypes") Class type) {
		Query query = db.query();
		query.descend("type").constrain(type.toString());
		
		ObjectSet<DataWrapper<HasID>> result = query.execute();
		List<HasID> results = new ArrayList<HasID>();
		while(result.hasNext()) {
			DataWrapper<HasID> entry = result.next();
			if (!entry.isWriteLocked()) {
				results.add(entry.getValue());
			}
		}
		
		return results;
	}

	@Override
	public boolean delete(@SuppressWarnings("rawtypes") Class type, String key) {
		return delete(type.toString(), key);
	}
	
	public boolean delete(String type, String key) {
		DataWrapper<HasID> found = getWrapper(type, key);
		if(found == null) {
			return false;
		}
		
		db.delete(found);
		pending.add(found);
		return true;
	}
	
	/**
	 * Commit the pending changes
	 */
	public void commit() {
		db.commit();
		pending.clear();
	}
	
	/**
	 * Abort the pending changes
	 */
	public void rollback() {
		db.rollback();
		pending.clear();
	}

	/**
	 * Get the wrapper associated with the input
	 * @param type The type of the object
	 * @param key the id
	 * @return The wrapper
	 */
	public DataWrapper<HasID> getWrapper(String type, String key) {
		Query query = db.query();
		query.descend("type").constrain(type);
		query.descend("key").constrain(key);
		
		ObjectSet<DataWrapper<HasID>> result = query.execute();
		
		if(result.hasNext()) {
			return result.next();
		}
		
		return null;
	}
	
	/**
	 * Get the wrapper associated with the input
	 * @param type The type of the object
	 * @param key the id
	 * @return The wrapper
	 */
	public DataWrapper<HasID> getWrapper(@SuppressWarnings("rawtypes") Class type, String key) {
		return getWrapper(type.toString(), key);
	}
	
	/**
	 * Get the pending changes
	 * @return The list of wrappers
	 */
	public List<DataWrapper<HasID>> getPending() {
		return pending;
	}
	
	public List<DataWrapper<HasID>> getAllWrappers() {
		DataWrapper<HasID> ex = new DataWrapper<HasID>();
		
		ObjectSet<DataWrapper<HasID>> result = db.queryByExample(ex);
		List<DataWrapper<HasID>> list = new ArrayList<DataWrapper<HasID>>();
		while (result.hasNext()) {
			list.add(result.next());
		}
		
		return list;
	}
}