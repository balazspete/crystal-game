/**
 * 
 */
package com.example.crystalgame.ui;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.StateChangeEvent;
import com.example.crystalgame.library.events.StateChangeEventListener;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class LocationManager {

	ListenerManager<StateChangeEventListener, StateChangeEvent> manager = 
		new ListenerManager<StateChangeEventListener, StateChangeEvent>() {
			@Override
			protected void eventHandlerHelper(
					StateChangeEventListener listener, StateChangeEvent event) {
				StateChangeEventListener.listenerManagerHelper(listener, event);
			}
		};
	
	private static LocationManager gameLocationManager = null;
	
	private LocationManager() 
	{
		
	}
	
	public static LocationManager getInstance() {
		if(null == gameLocationManager) {
			gameLocationManager = new LocationManager();
		}
		
		return gameLocationManager;
	}
	
	public void startComponents() {
		// Starts the GPS tracking service
		//startTracking();
	}
	
	public void addStateChangeEventListener(StateChangeEventListener listener) {
		manager.addEventListener(listener);
	}
	
	public void removeStateChangeEventListener(StateChangeEventListener listener) {
		manager.removeEventListener(listener);
	}
	
	public void locationTrackerCallback(Location previousLocation, Location currentLocation) {
		Artifact artifact = null;
		ZoneChangeEvent zoneChangeEvent = null;
		artifact = ArtifactTracker.getInstance().getArtifactsInProximity(currentLocation);
		if(null != artifact) {
			GameStateManager.getInstance().itemProximityAlert(artifact);
		}
		
		zoneChangeEvent = ZoneTracker.getInstance().searchGameBoundary(currentLocation);
		if(null != zoneChangeEvent) {
			GameStateManager.getInstance().zoneProximityAlert(zoneChangeEvent);
		}
	}
	
	public void saveGameBoundary(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setBoundaryPoints(gameBoundary);
	}
	
	public void saveGameLocation(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setGameLocationPoints(gameBoundary);
	}
}
