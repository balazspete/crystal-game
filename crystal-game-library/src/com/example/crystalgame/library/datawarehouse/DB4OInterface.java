package com.example.crystalgame.library.datawarehouse;

import java.io.Serializable;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

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
	public boolean put(String key, Serializable value) {
		db.store(new DataWrapper<Serializable>(key,value));
		db.commit();
		
		return get(key) != null;
	}

	@Override
	public Serializable get(String key) {
		Query query = db.query();
		query.descend("key").constrain(key);
		ObjectSet<DataWrapper<Serializable>> result = query.execute();
		
		if(result.hasNext()) {
			return result.next();
		}
		
		return null;
	}

	@Override
	public boolean delete(String key) {
		DataWrapper<Serializable> found = null;
        ObjectSet<DataWrapper<Serializable>> result = db.queryByExample(new DataWrapper<Serializable>(key));
 
        if (result.hasNext()) {
            found = result.next();
            db.delete(found);
            return true;
        }
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Serializable> getAll(Class<?> c) {
		return (List<Serializable>) db.query(c);
	}
 
}