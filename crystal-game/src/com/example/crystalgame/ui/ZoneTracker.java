package com.example.crystalgame.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;

/**
 *  Notify & keep track of zone changes
 *  @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class ZoneTracker {

	
	private ArrayList<Location> boundaryPoints = null;
	private ArrayList<Location> gameLocationPoints = null;
	
	private static ZoneTracker zoneTracker = null;
	
	private ZoneTracker() 
	{
		// private constructor
	}
	
	/**
	 * Singleton instance
	 * @return ZoneTracker
	 */
	public static ZoneTracker getInstance() {
		if(null == zoneTracker) {
			zoneTracker = new ZoneTracker();
		}
		
		return zoneTracker;
	}
	
	/**
	 * Game Boundary points
	 * @return List of locations
	 */
	public ArrayList<Location> getBoundaryPoints() {
		try {
			List<HasID> boundaries = ClientDataWarehouse.getInstance().getList(GameBoundary.class);
			if (boundaries == null || boundaries.size() == 0) {
				return boundaryPoints;
			}
			
			GameBoundary gameBoundary = (GameBoundary) boundaries.get(0);
			boundaryPoints = gameBoundary.getLocationList();
		} catch (DataWarehouseException e) {
			// Ignore
		}
		
		return boundaryPoints;
	}
	
	public void setBoundaryPoints(GameBoundary gameBoundary) {
		this.boundaryPoints = gameBoundary.getLocationList();
	}
	
	/**
	 * Game Location points
	 * @return List of locations
	 */
	public ArrayList<Location> getGameLocationPoints() {
		try {
			List<HasID> locations = ClientDataWarehouse.getInstance().getList(GameLocation.class);
			if (locations == null || locations.size() == 0) {
				return gameLocationPoints;
			}
			
			gameLocationPoints = ((GameLocation) locations.get(0)).getLocationList();
		} catch (DataWarehouseException e) {
			e.printStackTrace();
		}
		
		return gameLocationPoints;
	}
	
	public void setGameLocationPoints(GameBoundary gameBoundary) {
		
		this.gameLocationPoints = gameBoundary.getLocationList();
	}
	
	/**
	 * Search for availability of the given location within the game boundary
	 * @param location
	 * @return ZoneChangeEvent
	 */
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
