package com.example.crystalgame.library.data;

import java.io.Serializable;

/**
 * Describes a generic artifact
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public abstract class Artifact extends Location {
	
	public static final double SHOW_RADUIS = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5645186569766828225L;
	
	private double radius;
	
	public enum ArtifactType implements Serializable {
		CRYSTAL, MAGICAL_ITEM, CHARACTER
	}
	
	private ArtifactType artifactType;
	
	/**
	 * Create an artifact
	 * @param latitude the latitude
	 * @param longitude The longitude
	 * @param radius The radius
	 */
	public Artifact(ArtifactType artifactType, double latitude, double longitude, double radius) {
		super(latitude, longitude);
		this.radius = radius;
		this.artifactType = artifactType;
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

	/**
	 * @return the artifactType
	 */
	public ArtifactType getArtifactType() {
		return artifactType;
	}
	
	public boolean isInInteractionRange(Artifact other) {
		return isInInteractionRange(other, getInteractionRange());
	}

	private boolean isInInteractionRange(Artifact other, double distance) {
		return distance < getInteractionRange();
	}
	
	public boolean isInVisibleRange(Artifact other) {
		return isInVisibleRange(other, getDistance(other));
	}
	
	private boolean isInVisibleRange(Artifact other, double distance) {
		return distance < getVisibilityRange();
	}
	
	public boolean[] rangeChecks(Artifact other) {
		double distance = getDistance(other);
		return new boolean[]{ isInVisibleRange(other, distance), isInInteractionRange(other, distance) };
	}
	
	public abstract double getVisibilityRange();
	public abstract double getInteractionRange();
	
}
