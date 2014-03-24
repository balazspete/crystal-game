/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.StateChangeEvent;
import com.example.crystalgame.library.events.StateChangeEventListener;
import com.example.crystalgame.ui.ZoneChangeEvent.LocationState;
import com.example.crystalgame.ui.ZoneChangeEvent.ZoneType;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class LocationManager {

	private ZoneChangeEvent zoneChangeEvent = null;
	
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
		
		artifact = ArtifactTracker.getInstance().getArtifactsInProximity(currentLocation);
		if(null != artifact) {
			GameStateManager.getInstance().itemProximityAlert(artifact);
		}
		
		zoneChangeEvent = ZoneTracker.getInstance().searchGameBoundary(currentLocation);
		if(null != zoneChangeEvent) {
			System.out.println("Zone Changed : "+zoneChangeEvent);
			GameStateManager.getInstance().zoneProximityAlert(zoneChangeEvent);
		}
	}
	
	public void saveGameBoundary(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setBoundaryPoints(gameBoundary);
	}
	
	public ArrayList<Location> getGameBoundaryPoints()
	{
		return ZoneTracker.getInstance().getBoundaryPoints();
		
	}
	
	public ArrayList<Location> getGameLocationPoints()
	{
		return ZoneTracker.getInstance().getGameLocationPoints();
	}
	public void saveGameLocation(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setGameLocationPoints(gameBoundary);
	}
	
	public boolean isGameBoundary() {
		if(
				zoneChangeEvent != null
			&&	zoneChangeEvent.getLocationState() == LocationState.IN
			&&	zoneChangeEvent.getZoneType() == ZoneType.GAME_BOUNDARY) {
			return true;
		}
		return false;
	}
	
	public boolean isGameLocation() {
		if(
				zoneChangeEvent != null	
			&&  zoneChangeEvent.getLocationState() == LocationState.IN
			&&	zoneChangeEvent.getZoneType() == ZoneType.GAME_LOCATION) {
			return true;
		}
		return false;
	}
	
}
