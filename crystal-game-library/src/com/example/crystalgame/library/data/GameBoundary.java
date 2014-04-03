package com.example.crystalgame.library.data;

import java.util.ArrayList;


/**
 * Describes the game boundary
 * @author Balazs Pete, Shen Chen, Rajan Verma, Allen Thomas Varghese
 *
 */
public class GameBoundary extends Zone {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6136247701997501094L;

	public GameBoundary(ArrayList<Location> locationPoints) {
		super();
		setLocationList(locationPoints);
	}
}
