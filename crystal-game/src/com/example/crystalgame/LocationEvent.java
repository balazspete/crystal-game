/**
 * This class encapsulates the location information
 */
package com.example.crystalgame;

/**
 * @author Allen Thomas Varghese
 *
 */
public class LocationEvent {

	// Lattitude
	private double lattitudePosition;
	// Longitude
	private double longitudePosition;
	
	/**
	 * Default Constructor
	 * 
	 * latVal - Lattitude value
	 * lngVal - Longitude value
	 */
	public LocationEvent(double latVal, double lngVal) {
		this.lattitudePosition = latVal;
		this.longitudePosition = lngVal;
	}

	public double getLattitudePosition() {
		return lattitudePosition;
	}

	public void setLattitudePosition(double lattitudePosition) {
		this.lattitudePosition = lattitudePosition;
	}

	public double getLongitudePosition() {
		return longitudePosition;
	}

	public void setLongitudePosition(double longitudePosition) {
		this.longitudePosition = longitudePosition;
	}
}
