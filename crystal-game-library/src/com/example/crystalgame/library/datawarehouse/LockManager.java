package com.example.crystalgame.library.datawarehouse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {
	
	public final int TIMEOUT = 2;

	private static transient ConcurrentHashMap<String, ReentrantLock> locks;
	
	public LockManager() {
		locks = new ConcurrentHashMap<String, ReentrantLock>();
	}
	
	public boolean lock(String key) {
		ReentrantLock lock = locks.get(key);
		if (lock == null) {
			lock = new ReentrantLock(true);
			locks.put(key, lock);
		}
		
		try {
			return lock.tryLock(TIMEOUT, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public void unlock(String key) {
		ReentrantLock lock = locks.get(key);
		if (lock != null) {
			lock.unlock();
		}
	}
}
