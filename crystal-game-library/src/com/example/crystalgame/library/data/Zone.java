package com.example.crystalgame.library.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.crystalgame.library.util.RandomID;

/**
 * Describes a rectangular area
 * 
 * @author Chen Shen, Allen Thomas Varghese
 * 
 */
public class Zone implements Serializable, HasID
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8421037828271760380L;
	
	public final String id;
	private ArrayList<Location> locationPoints = new ArrayList<Location>();
	
	// Quarter of PI
	private static final double QUARTERPI = Math.PI / 4.0;

	// Radius of earth
	private static final double EARTH_RADIUS = 6378.1 * 1000;
	
	// Entry radius around an item expressed in meters
	private static final double RADIUS_OF_ENTRY = 5;
	
	
	/**
	 * Create a zone
	 */

	public Zone() 
	{
		id = RandomID.getRandomId();
	}
	
	/**
	 * Convert degree to radian
	 * @param degree value
	 * @return radian value
	 */
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
	
	public boolean isWithin(Location location) {
		return checkIfWithin(this.locationPoints, location);
	}
	
	/**
	 * Checks if the given location is within the quadrilateral zone
	 * @param location
	 * @return boolean
	 */
	public static boolean checkIfWithin(ArrayList<Location> locationPoints, Location location){
	  int i;
	  int j;
	  boolean result = false;
	  double xVali, yVali, xValj, yValj, lng, lat, testX, testY;
	  ArrayList<Location> tempDataList = new ArrayList<Location>();
	  
	  // If the list of locations is empty or null, do not process
	  if(null == locationPoints || (null!=locationPoints && locationPoints.isEmpty())) {
		  return false;
	  }
	  
	  // Temporary ArrayList for easy iteration of points
	  for(Location key : locationPoints)
	  {
		  tempDataList.add(key);
	  }
	  
	  lng = location.getLongitude() * Math.PI/180;
	  lat = location.getLatitude() * Math.PI/180;
	  
	  testX = lng;
	  testY = Math.log(Math.tan(QUARTERPI + 0.5 * lat));
	  
	  // Ray-casting and Mercator projection for conversion of points
	  for(i = 0, j = tempDataList.size() - 1; i < tempDataList.size(); j = i++)
	  {
		  lng = tempDataList.get(i).getLongitude() * Math.PI/180;
		  lat = tempDataList.get(i).getLatitude() * Math.PI/180;
		  xVali = lng;
		  yVali = Math.log(Math.tan(QUARTERPI + 0.5 * lat));
		  
		  lng = tempDataList.get(j).getLongitude() * Math.PI/180;
		  lat = tempDataList.get(j).getLatitude() * Math.PI/180;
		  xValj = lng;
		  yValj = Math.log(Math.tan(QUARTERPI + 0.5 * lat));
		  
		  if(
			  (yVali > testY) != (yValj > testY) &&
			  (testX < (xValj - xVali) * (testY - yVali) / (yValj - yVali)+xVali)
		    )
		  {
			  result = !result;
		  }
	  }
	  return result;
	}
	
	/**
	 * Returns point from the boundary
	 * @return The location
	 */
	public Location getLocation(int index) 
	{
		return locationPoints.get(index);
	}
	
	public Location getLocation(String markerId) {
		for(Location location : locationPoints) {
			if(location.getMarkerID().equals(markerId)) {
				return location;
			}
		}
		
		return null;
	}
	
	public ArrayList<Location> getLocationList() {
		return locationPoints;
	}
	
	public void setLocationList(ArrayList<Location> locationPoints)
	{
		this.locationPoints = locationPoints;
	}
	
	/**
	 * Add a zone corner
	 * @param location The location point
	 * @return True if the Location was added
	 */
	public boolean addLocation(Location location) 
	{
		if(locationPoints.size() >= 4) {
			return false;
		}
		
		locationPoints.add(location);
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
	public boolean isEmpty() {
		return this.locationPoints != null 
				&& this.locationPoints.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMinLattitudeValue() {
		double minLatitude = Double.POSITIVE_INFINITY;
		for (Location l : locationPoints) {
			minLatitude = Math.min(minLatitude, l.getLatitude());
		}
		return minLatitude;
	}
	/**
	 * 
	 * @return
	 */
	public double getMinLongitudeValue() {
		double minLongitude = Double.POSITIVE_INFINITY;
		for (Location l : locationPoints) {
			minLongitude = Math.min(minLongitude, l.getLongitude());
		}
		return minLongitude;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMaxLattitudeValue() {
		double maxLatitude = Double.NEGATIVE_INFINITY;
		System.err.println("location points "+locationPoints.size());
		for (Location l : locationPoints) {
			maxLatitude = Math.max(maxLatitude, l.getLatitude());
		}
		return maxLatitude;
	}
	/**
	 * 
	 * @return
	 */
	public double getMaxLongitudeValue()
	{
		double maxLongitude = Double.NEGATIVE_INFINITY;
		for (Location l : locationPoints) {
			maxLongitude = Math.max(maxLongitude, l.getLongitude());
		}
		return maxLongitude;
	}
	
	@Override
	public String getID() 
	{
		return id;
	}
	
	/**
	 * Clear the zone definition
	 */
	public void clear() {
		locationPoints.clear();
	}
}
