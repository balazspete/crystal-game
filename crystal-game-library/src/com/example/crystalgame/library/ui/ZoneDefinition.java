/**
 * This activity is for creating a game
 * 
 * @author Chen Shen, Allen Thomas Varghese
 */

package com.example.crystalgame.library.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ZoneDefinition implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, LatLngLocation> locPoints = new HashMap<String, LatLngLocation>();
	
	public  ZoneDefinition() {
		
	}
	
	/*
	public ZoneDefinition(
			LatLngLocation corner1
		,	LatLngLocation corner2
		,	LatLngLocation corner3
		,	LatLngLocation corner4
			) {
		locPoints.put(corner1);
		locPoints.add(corner2);
		locPoints.put(corner3);
		locPoints.put(corner4);
	}
	*/
	
	/**
	 * Checks if the given location is within the zone
	 * @param location
	 * @return boolean
	 */
	public boolean inZone(LatLngLocation location){
		
		return false;
	}
	
	/**
	 * Returns the set of points that form the boundary
	 * @return ArrayList<LatLngLocation>
	 */
	public HashMap<String, LatLngLocation> getLocations() 
	{
		return this.locPoints;
	}
	
	/**
	 * Returns point from the boundary
	 * @return ArrayList<LatLngLocation>
	 */
	public LatLngLocation getLocation(String index) 
	{
		return this.locPoints.get(index);
	}
	/**
	 *
	 *
	 */
	public void setLocation(String markerID, LatLngLocation loc) 
	{
		this.locPoints.put(markerID, loc);
	}
	
	public int getLength() 
	{
		return this.locPoints.size();
	}
	
	/**
	 * To check if there are any locations marked as part of a zone
	 * @return true/false
	 */
	public boolean isEmpty() 
	{
		boolean result = false;
		if(this.locPoints != null && this.locPoints.isEmpty())
		{
			result = true;
		}
		else
		{
			result = false;
		}
		return result;
	}
}
