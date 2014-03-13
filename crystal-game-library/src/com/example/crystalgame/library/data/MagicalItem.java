package com.example.crystalgame.library.data;

/**
 * Defines a magical item
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class MagicalItem extends Item {

	/* Description for the type of magical item */
	private String magicalItemDescription;
	
	/* Image that represents the magical item */
	private String icon;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3740669597817985828L;

	/**
	 * Create a magical item
	 * @param latitude the latitude 
	 * @param longitude the longitude
	 */
	public MagicalItem(double latitude, double longitude) {
		super(latitude, longitude, ItemType.MAGICAL_ITEM);
	}

	public String getMagicalItemDescription() {
		return magicalItemDescription;
	}

	public void setMagicalItemDescription(String magicalItemDescription) {
		this.magicalItemDescription = magicalItemDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
