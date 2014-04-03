/**
 * 
 */
package com.example.crystalgame.location;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.util.Log;

import com.example.crystalgame.game.GameStateManager;
import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.StateChangeEvent;
import com.example.crystalgame.library.events.StateChangeEventListener;
import com.example.crystalgame.location.ZoneChangeEvent.LocationState;
import com.example.crystalgame.location.ZoneChangeEvent.ZoneType;

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
	
	public synchronized void startComponents() {
		// Starts the GPS tracking service
		//startTracking();
	}
	
	public synchronized void addStateChangeEventListener(StateChangeEventListener listener) {
		manager.addEventListener(listener);
	}
	
	public synchronized void removeStateChangeEventListener(StateChangeEventListener listener) {
		manager.removeEventListener(listener);
	}
	
	public synchronized void locationTrackerCallback(Location previousLocation, Location currentLocation) {
//		Artifact artifact = null;
//		
//		artifact = ArtifactTracker.getInstance().getArtifactsInProximity(currentLocation);
//		if(null != artifact) {
//			Log.i("LocationManager","Artifact detected!");
//			GameStateManager.getInstance().itemProximityAlert(artifact);
//		}
		
		zoneChangeEvent = ZoneTracker.getInstance().searchGameBoundary(currentLocation);
		if(null != zoneChangeEvent) {
			System.out.println("Zone Changed : "+zoneChangeEvent);
			GameStateManager.getInstance().zoneProximityAlert(zoneChangeEvent);
		}
		
		// Update the game character location in data warehouse 
//		InventoryManager.getInstance().setCharacterLocation(currentLocation);
	}
	
	public synchronized void saveGameBoundary(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setBoundaryPoints(gameBoundary);
	}
	
	public synchronized ArrayList<Location> getGameBoundaryPoints()
	{
		return ZoneTracker.getInstance().getBoundaryPoints();
	}
	
	public synchronized ArrayList<Location> getGameLocationPoints()
	{
		return ZoneTracker.getInstance().getGameLocationPoints();
	}
	
	public synchronized void saveGameLocation(GameBoundary gameBoundary) {
		ZoneTracker.getInstance().setGameLocationPoints(gameBoundary);
	}
	
	public synchronized boolean isGameBoundary() {
		if(
				zoneChangeEvent != null
			&&	zoneChangeEvent.getLocationState() == LocationState.IN
			&&	zoneChangeEvent.getZoneType() == ZoneType.GAME_BOUNDARY) {
			return true;
		}
		return false;
	}
	
	public synchronized boolean isGameLocation() {
		if(
				zoneChangeEvent != null	
			&&  zoneChangeEvent.getLocationState() == LocationState.IN
			&&	zoneChangeEvent.getZoneType() == ZoneType.GAME_LOCATION) {
			return true;
		}
		
		return false;
	}
	
	private Location characterLocation;
	private DateTime lastCharacterLocationSaveTime = DateTime.now();
	public void setCharacterLocation(Location location) {
		Location previous = characterLocation;
		characterLocation = location;
		
		locationTrackerCallback(previous, location);
		
//		if (lastCharacterLocationSaveTime.plusSeconds(20).isBefore(DateTime.now())) {
//			InventoryManager.getInstance().setCharacterLocation(location);
//		}
	}
	
	public Location getCharacterLocation() {
		return characterLocation;
	}
	
}
