package com.example.crystalgame.library.datawarehouse;

import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.example.crystalgame.library.data.HasID;

/**
 * DB Connection class for Android
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DB4OInterface implements KeyValueStore {
	
	public static final int TIMEOUT = 5000;
	
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
		return put(type.toString(), value);
	}
	
	public HasID put(String type, HasID value) {
		try {
			boolean locked = db.ext().setSemaphore(getLockName(value), TIMEOUT);
			if (locked) {
				DataWrapper<HasID> wrapper = getWrapper(type, value.getID());
				if (wrapper == null) {
					wrapper = new DataWrapper<HasID>(type, value);
				} else {
					try {
						//db.ext().activate(wrapper, Integer.MAX_VALUE);
						wrapper.setValue(value);
					} catch (DataWarehouseException e) {
						return null;
					}
				}
				
				pending.add(wrapper);
				db.ext().store(wrapper, Integer.MAX_VALUE);
				if(db.ext().isStored(wrapper)) {
					return value;
				}
			}
		} finally {
			db.ext().releaseSemaphore(getLockName(value));
		}
		
		return null;
	}
	
	@Override
	public HasID get(@SuppressWarnings("rawtypes") Class type, String key) {
		return get(type.toString(), key);
	}
	
	public HasID get(String type, String key) {
		try {
			boolean locked = db.ext().setSemaphore(getLockName(key), TIMEOUT);
			if (locked) {
				DataWrapper<HasID> result = getWrapper(type, key);
				if(result != null) {
					return result.getValue();
				}
			}
			
			return null;
		} finally {
			db.ext().releaseSemaphore(getLockName(key));
		}
	}
	
	@Override
	public List<HasID> getAll(@SuppressWarnings("rawtypes") final Class type) {
		return getAll(type.toString());
	}
	
	public List<HasID> getAll(final String type) {
		List<DataWrapper<HasID>> wrappers = db.query(new Predicate<DataWrapper<HasID>>() {
			private static final long serialVersionUID = 7890956022571856026L;

			@Override
			public boolean match(DataWrapper<HasID> arg) {
//				System.out.println("DaWr type: " + arg.getType() + " looking for " + type);
				
				boolean match = arg.getType().equals(type);
//				System.out.println("DaWr match? " + match);
				return match;
			}
		});
		
		List<HasID> results = new ArrayList<HasID>();
		for(DataWrapper<HasID> wrapper : wrappers) {
			try {
				boolean locked = db.ext().setSemaphore(getLockName(wrapper.getKey()), TIMEOUT);
				if (locked) {
					results.add(wrapper.getValue());
				}
			} finally {
				db.ext().releaseSemaphore(getLockName(wrapper.getKey()));
			}
		}
		
		return results;
	}

	@Override
	public boolean delete(@SuppressWarnings("rawtypes") Class type, String key) {
		return delete(type.toString(), key);
	}
	
	public boolean delete(String type, String key) {
		try {
			boolean locked = db.ext().setSemaphore(getLockName(key), TIMEOUT);
			if (locked) {
				DataWrapper<HasID> found = getWrapper(type, key);
				if(found != null) {
					db.delete(found);
					pending.add(found);
					return true;
				}
			}
			
			return false;
		} finally {
			db.ext().releaseSemaphore(getLockName(key));
		}
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
	public DataWrapper<HasID> getWrapper(final String type, final String key) {
		List<DataWrapper<HasID>> wrappers = db.query(new Predicate<DataWrapper<HasID>>() {
			private static final long serialVersionUID = 7890956022571856026L;

			@Override
			public boolean match(DataWrapper<HasID> arg) {
//				System.out.println("DaWr type: " + arg.getType() + " looking for " + type);
//				System.out.println("DaWr key: " + arg.getKey() + " looking for " + key);
//				
				boolean match = arg.getType().equals(type) && arg.getKey().equals(key);
//				System.out.println("DaWr match? " + match);
				return match;
			}
		});
		
		if (wrappers != null && wrappers.size() > 0) {
			return wrappers.get(0);
		}
		
		return null;
		
//		Query query = db.query();
//		query.descend("type").constrain(type);
//		query.descend("key").constrain(key);
//		
//		ObjectSet<DataWrapper<HasID>> result = query.execute();
//		if(result.hasNext()) {
//			return result.next();
//		}
//		
//		return null;
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
	
	private String getLockName(HasID value) {
		return getLockName(value.getID());
	}
	
	private String getLockName(String ID) {
		return "LOCK_" + ID;
	}
	
}