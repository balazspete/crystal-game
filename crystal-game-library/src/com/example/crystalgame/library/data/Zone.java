package com.example.crystalgame.library.data;

import java.io.Serializable;
import java.util.HashMap;

import com.example.crystalgame.library.util.RandomID;

/**
 * Describes a rectangular area
 * 
 * @author Chen Shen, Allen Thomas Varghese
 * 
 */
public class Zone implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8421037828271760380L;
	
	public final String id;
	private HashMap<String, Location> locationPoints = new HashMap<String, Location>();
	
	/**
	 * Create a zone
	 */
	public  Zone() {
		id = RandomID.getRandomId();
	}
	
	/**
	 * Checks if the given location is within the zone
	 * @param location 
	 * @return True if in zone
	 */
	public boolean inZone(Location location){
		// TODO: complete
		return false;
	}
	
	/**
	 * Returns the set of points that form the boundary
	 * @return All locations
	 */
	public HashMap<String, Location> getLocations() 
	{
		return locationPoints;
	}
	
	/**
	 * Returns point from the boundary
	 * @return The location
	 */
	public Location getLocation(String index) 
	{
		return locationPoints.get(index);
	}
	
	/**
	 * Add a zone corner
	 * @param location The location point
	 * @return True if the Location was added
	 */
	public boolean addLocation(Location location) 
	{
		if(locationPoints.size() > 4) {
			return false;
		}
		
		locationPoints.put(location.id, location);
		return true;
	}
	
	/**
	 * Get the number of locations
	 * @return The number of locations
	 */
	public int getLength() 
	{
		return locationPoints.size();
	}
	
	/**
	 * To check if there are any locations marked as part of a zone
	 * @return true if zone if not defined
	 */
	public boolean isEmpty() 
	{
		return this.locationPoints != null 
				&& this.locationPoints.isEmpty();
	}
}
