package com.example.crystalgame.library.data;

/**
 * Describes an item
 * @author 
 *
 */
public abstract class Item extends Artifact {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807516195771430780L;
	
	public enum ItemType {
		CRYSTAL, MAGICAL_ITEM
	}
	
	public static final double RADIUS = 100;
	public final ItemType type;
	
	/**
	 * Create an item
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	protected Item(double latitude, double longitude, ItemType type) {
		super(latitude, longitude, RADIUS);
		this.type = type;
	}

}
