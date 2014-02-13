/**
 * POJO for storing the location
 * 
 * @author Chen Shen, Allen Thomas Varghese
 */
package com.example.crystalgame.library.ui;

import java.io.Serializable;

public class LatLngLocation implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private double lattitudePosition = 0.0f;
	private double longitudePosition = 0.0f;
	
	/**
	 * Constructor
	 */
	public LatLngLocation(double lattitudePosition, double longitudePosition) {
		this.lattitudePosition = lattitudePosition;
		this.longitudePosition = longitudePosition;
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
	
	/**
	 * Overriding toString() method to print the location values
	 */
	@Override
	public String toString()
	{
		return (getLattitudePosition()+  ":" + getLongitudePosition());
	}
}
