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
		this.db = container.ext().openSession();
		this.pending = new ArrayList<DataWrapper<HasID>>();
	}
    
	@Override
	public boolean put(@SuppressWarnings("rawtypes") Class type, HasID value) {
		return put(type.toString(), value);
	}
	
	public boolean put(String type, HasID value) {
		DataWrapper<HasID> wrapper = getWrapper(type, value.getID());
		if (wrapper == null) {
			wrapper = new DataWrapper<HasID>(type, value);
		} else {
			try {
				wrapper.setValue(value);
			} catch (DataWarehouseException e) {
				System.err.println("Failed to store " + type + ": " + e.getMessage());
				return false;
			}
		}
		
		db.store(wrapper);
		
		pending.add(wrapper);
		System.out.println("GET: " + get(type, value.getID()));
		return db.ext().isStored(wrapper);
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
	public List<HasID> getAll(@SuppressWarnings("rawtypes") final Class type) {
		List<DataWrapper<HasID>> result = db.query(new Predicate<DataWrapper<HasID>>() {
			private static final long serialVersionUID = 7890956022571856026L;
			@Override
			public boolean match(DataWrapper<HasID> arg) {
				
				return arg.getType().equals(type);
			}
		});
		
		List<HasID> results = new ArrayList<HasID>();
		for(DataWrapper<HasID> entry : result) {
			results.add(entry.getValue());
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
	
	public void close() {
		db.close();
	}

	/**
	 * Get the wrapper associated with the input
	 * @param type The type of the object
	 * @param key the id
	 * @return The wrapper
	 */
	public DataWrapper<HasID> getWrapper(final String type, final String key) {
		List<DataWrapper<HasID>> result = db.query(new Predicate<DataWrapper<HasID>>() {
			private static final long serialVersionUID = 7890956022571856026L;
			@Override
			public boolean match(DataWrapper<HasID> arg) {
				
				return arg.getKey().equals(key)
						&& arg.getType().equals(type);
			}
		});
		
		if(result.size() > 0) {
			return result.get(0);
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