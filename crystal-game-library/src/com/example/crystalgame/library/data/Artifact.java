package com.example.crystalgame.library.data;

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
	
	public enum ArtifactType {
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
	
	public boolean isInRange(Artifact other) {
		System.out.println(getDistance(other));
		return getDistance(other) < SHOW_RADUIS;
	}
	
}
