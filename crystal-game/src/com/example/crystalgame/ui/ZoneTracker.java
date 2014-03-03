/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class ZoneTracker {

	private ArrayList<Location> boundaryPoints;
	private ArrayList<Location> gameLocationPoints;
	
private static ZoneTracker zoneTracker = null;
	
	private ZoneTracker() 
	{
		
	}
	
	public static ZoneTracker getInstance() {
		if(null == zoneTracker) {
			zoneTracker = new ZoneTracker();
		}
		
		return zoneTracker;
	}
	
	public ArrayList<Location> getBoundaryPoints() {
		return boundaryPoints;
	}
	
	
	public void setBoundaryPoints(GameBoundary gameBoundary) {
		this.boundaryPoints = gameBoundary.getLocationList();
	}
	
	public ArrayList<Location> getGameLocationPoints() {
		return gameLocationPoints;
	}
	
	public void setGameLocationPoints(GameBoundary gameBoundary) {
		this.gameLocationPoints = gameBoundary.getLocationList();
	}
	
	public ZoneChangeEvent searchGameBoundary(Location location)
	{
		ZoneChangeEvent zoneChangeEvent = null;
		
		boolean isInGameBoundary = Zone.inQuadZone(boundaryPoints, location);
		boolean isInGameLocation = Zone.inQuadZone(gameLocationPoints, location);
		
		if(isInGameBoundary)
		{
			zoneChangeEvent = new ZoneChangeEvent();
			zoneChangeEvent.setZoneType(ZoneChangeEvent.ZoneType.GAME_BOUNDARY);
			zoneChangeEvent.setLocationState(ZoneChangeEvent.LocationState.IN);
			
			if(isInGameLocation)
			{
				zoneChangeEvent.setZoneType(ZoneChangeEvent.ZoneType.GAME_LOCATION);
				zoneChangeEvent.setLocationState(ZoneChangeEvent.LocationState.IN);
			}

		}
		
		return zoneChangeEvent;
	}
}
