package com.example.crystalgame.library.datawarehouse;

import java.io.Serializable;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

/**
 * DB Connection class for Android
 * 
 * @author Allen Thomas Varghese, Pete Balazs
 *
 */
public class DBConnection implements KeyValueStore {

	private String dbName = "CrystalGame";
    private ObjectContainer db;
    
	private static DBConnection dbConnectionInstance;
	
	private DBConnection() {
		if (this.dbName != null) {
	        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), this.dbName);
	    }
	}

	public static DBConnection getInstance() {
		if(null == dbConnectionInstance) {
			dbConnectionInstance = new DBConnection();
		}
		
		return dbConnectionInstance;
	}
 
 
    void CloseDb() {
 
        db.close();
 
    }
    
	@Override
	public boolean put(String key, Serializable value) {
		db.store(new DataWrapper<Serializable>(key,value));
		db.commit();
		
		return (get(key)!=null);
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