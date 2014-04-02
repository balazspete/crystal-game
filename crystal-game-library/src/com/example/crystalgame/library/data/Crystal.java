package com.example.crystalgame.library.data;

/**
 * Defines a crystal
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class Crystal extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7426488079048008002L;
	
	/**
	 * Create a crystal
	 * @param latitude The latitude
	 * @param longitude The longitude
	 */
	public Crystal(double latitude, double longitude) {
		super(ArtifactType.CRYSTAL, latitude, longitude);
	}

	/**
	 * Compare two Crystal objects using ID
	 * 
	 * @param obj Crystal object
	 * @return true or false
	 */
	public boolean equals(Crystal obj) {
		return (
				obj != null
			&&	obj.getID().equals(id)
			&&	obj.getClass().equals(getClass())
			);
	}

	@Override
	public double getVisibilityRange() {
		return 10;
	}

	@Override
	public double getInteractionRange() {
		return 5;
	}
}
