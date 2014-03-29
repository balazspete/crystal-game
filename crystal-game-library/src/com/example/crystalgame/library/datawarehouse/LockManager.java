package com.example.crystalgame.library.datawarehouse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {
	
	public final int TIMEOUT = 2;

	private ConcurrentHashMap<String, ReentrantReadWriteLock> locks;
	
	public LockManager() {
		locks = new ConcurrentHashMap<String, ReentrantReadWriteLock>();
	}
	
	public boolean readLock(String key) {
		ReentrantReadWriteLock lock = locks.get(key);
		if (lock == null) {
			lock = new ReentrantReadWriteLock(true);
			locks.put(key, lock);
		}
		
		try {
			return lock.readLock().tryLock(TIMEOUT, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public void readUnlock(String key) {
		ReentrantReadWriteLock lock = locks.get(key);
		if (lock != null) {
			lock.readLock().unlock();
		}
	}
	
	public boolean writeLock(String key) {
		ReentrantReadWriteLock lock = locks.get(key);
		if (lock == null) {
			return false;
		}
		
		try {
			return lock.writeLock().tryLock(TIMEOUT, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public void writeUnlock(String key) {
		ReentrantReadWriteLock lock = locks.get(key);
		if (lock != null) {
			lock.writeLock().unlock();
		}
	}
	
}
