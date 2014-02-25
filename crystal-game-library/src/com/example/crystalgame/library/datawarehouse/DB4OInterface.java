package com.example.crystalgame.library.datawarehouse;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.example.crystalgame.library.data.HasID;

/**
 * DB Connection class for Android
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public abstract class DB4OInterface implements KeyValueStore {
	
    private ObjectContainer db;    
	
	protected DB4OInterface(String dbFileName) {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbFileName);
	}
 
    void close() {
        db.close();
    }
    
	@Override
	public boolean put(@SuppressWarnings("rawtypes") Class type, HasID value) {
		DataWrapper<HasID> wrapper = getWrapper(type, value.getID());
		if (wrapper == null) {
			wrapper = new DataWrapper<HasID>(type, value);
		}
		
		db.store(wrapper);
		db.commit();
		
		return get(type, value.getID()) != null;
	}

	@Override
	public HasID get(@SuppressWarnings("rawtypes") Class type, String key) {
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
		DataWrapper<HasID> found = getWrapper(type, key);
		if(found == null) {
			return false;
		}
		
		db.delete(found);
		return true;
	}

	private DataWrapper<HasID> getWrapper(@SuppressWarnings("rawtypes") Class type, String key) {
		Query query = db.query();
		query.descend("type").constrain(type.toString());
		query.descend("key").constrain(key);
		
		ObjectSet<DataWrapper<HasID>> result = query.execute();
		if(result.hasNext()) {
			return result.next();
		}
		
		return null;
	}
}