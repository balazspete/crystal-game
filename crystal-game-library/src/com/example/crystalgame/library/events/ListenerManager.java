package com.example.crystalgame.library.events;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * A module to manage event listeners
 * @author Balazs Pete, Allen Thomas Varghese
 *
 * @param <LISTENER> The type of listener to manage
 * @param <DATA> The type of data the listeners handle
 */
public abstract class ListenerManager<LISTENER extends EventListener, DATA> {

	private ArrayList<LISTENER> listeners;
	
	/**
	 * Create a listener manager
	 */
	public ListenerManager() {
		listeners = new ArrayList<LISTENER>();
	}
	
	/**
	 * Add a listener to the manager
	 * @param listener The listener to add
	 */
	public synchronized void addEventListener(LISTENER listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener from the manager
	 * @param listener The listener to remove
	 */
	public synchronized void removeEventListener(LISTENER listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Send a DATA to all listeners
	 * @param data The DATA to send
	 */
	public synchronized void send(DATA data) {
		// Call the handler function for each listener
		for (LISTENER listener : listeners) {
			eventHandlerHelper(listener, data);
		}
	}
	
	/**
	 * Method used to forward DATA objects to a listener.
	 * <i>Decide here how to forward the DATA to the listener (if at all).</i>
	 * @param listener The listener to forward the DATA object to
	 * @param data The DATA object to forward
	 */
	protected abstract void eventHandlerHelper(LISTENER listener, DATA data);
	
}
