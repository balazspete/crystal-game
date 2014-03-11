/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;

import android.util.Log;

import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;

/**
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class ZoneTracker {

	
	private ArrayList<Location> boundaryPoints = null;
	private ArrayList<Location> gameLocationPoints = null;
	
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
		boundaryPoints = new ArrayList<Location>();
		boundaryPoints.add(new Location(53.34373266956245, -6.247414081275463));
		boundaryPoints.add(new Location(53.34372426266502 ,-6.247033790433407));
		boundaryPoints.add(new Location(53.34363979327044, -6.247001939201355));
		boundaryPoints.add(new Location(53.34363338800144, -6.2474171864748));
		if(boundaryPoints!=null)
		{
			Log.d("Not null","OK");
		}
		return boundaryPoints;
	}
	
	public void setBoundaryPoints(GameBoundary gameBoundary) {
		this.boundaryPoints = gameBoundary.getLocationList();
	}
	
	public ArrayList<Location> getGameLocationPoints() {
		
		gameLocationPoints = new ArrayList<Location>();
		gameLocationPoints.add(new Location(53.34373266956245, -6.247314081275463));
		gameLocationPoints.add(new Location(53.34372426266502 ,-6.247133790433407));
		gameLocationPoints.add(new Location(53.34363979327044, -6.247101939201355));
		gameLocationPoints.add(new Location(53.34363338800144, -6.2473171864748));
		
		return gameLocationPoints;
	}
	
	public void setGameLocationPoints(GameBoundary gameBoundary) {
		
		this.gameLocationPoints = gameBoundary.getLocationList();
	}
	
	public ZoneChangeEvent searchGameBoundary(Location location)
	{
		ZoneChangeEvent zoneChangeEvent = null;
		
		//boolean isInGameBoundary = Zone.inQuadZone(boundaryPoints, location);
		boolean isInGameBoundary = Zone.inQuadZone(getBoundaryPoints(), location);
		//boolean isInGameLocation = Zone.inQuadZone(gameLocationPoints, location);
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
