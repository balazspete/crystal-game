/**
 * POJO for storing the location
 * 
 * @author Chen Shen, Allen Thomas Varghese
 */
package com.example.crystalgame.library.data;

import java.io.Serializable;

import com.example.crystalgame.library.util.RandomID;

/**
 * Describes a GPS coordinate
 * 
 * @author Allen Thomas Varghese, Shen Chen
 *
 */
public class Location implements Serializable, HasID 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4048063505677209412L;

	public final String id;
	
	private double lattitude = 0.0f;
	private double longitude = 0.0f;
	
	private transient String markerID;
	
	/**
	 * Create a Location
	 * @param lattitudePosition 
	 * @param longitude
	 */
	public Location(double lattitude, double longitude) {
		id = RandomID.getRandomId();
		this.lattitude = lattitude;
		this.longitude = longitude;
	}
	
	/**
	 * Get the latitude
	 * @return The latitude value
	 */
	public double getLatitude() {
		return lattitude;
	}
	
	/**
	 * Set the latitude
	 */
	public void setLatitude(double lattitudePosition) {
		this.lattitude = lattitudePosition;
	}
	
	/**
	 * Get the longitude
	 * @return The longitude value
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Set the longitude
	 */
	public void setLongitude(double longitudePosition) {
		this.longitude = longitudePosition;
	}
	
	@Override
	public String toString()
	{
		return (getLatitude()+  ":" + getLongitude());
	}

	/**
	 * @return the markerID
	 */
	public String getMarkerID() {
		return markerID;
	}

	/**
	 * @param markerID the markerID to set
	 */
	public void setMarkerID(String markerID) {
		this.markerID = markerID;
	}
	
	@Override
	public String getID() {
		return id;
	}
	
	public Location getLocation() {
		return this;
	}
	
	public double getDistance(Location other) {
	 	final int R = 6371; // Radius of the earth

	    Double latDistance = deg2rad(other.getLatitude() - getLatitude());
	    Double lonDistance = deg2rad(other.getLongitude() - getLongitude());
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(deg2rad(other.getLatitude())) * Math.cos(deg2rad(other.getLatitude()))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000;									// convert to meters
	   	
	    return distance;
	}
	
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
	
}
