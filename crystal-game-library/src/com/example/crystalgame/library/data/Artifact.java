package com.example.crystalgame.library.data;

/**
 * Describes a generic artifact
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public abstract class Artifact extends Location {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5645186569766828225L;
	
	private double radius;
	
	/**
	 * Create an artifact
	 * @param latitude the latitude
	 * @param longitude The longitude
	 * @param radius The radius
	 */
	public Artifact(double latitude, double longitude, double radius) {
		super(latitude, longitude);
		this.radius = radius;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	
}
