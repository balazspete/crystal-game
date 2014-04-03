package com.example.crystalgame.library.events;

import java.util.EventListener;

public abstract class StateChangeEventListener implements EventListener {

	public abstract void onLocationStateChange(StateChangeEvent event);
	
	public static void listenerManagerHelper(StateChangeEventListener listener, StateChangeEvent event) {
		switch(event.getCurrent().type) {
			case LOCATION_STATE:
				listener.onLocationStateChange(event);
				break;
			default:
				break;
		}
	}
	
	
}
