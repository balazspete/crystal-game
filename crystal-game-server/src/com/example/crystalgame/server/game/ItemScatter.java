package com.example.crystalgame.server.game;
/**
 * Crystal Scatter
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.Item;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.util.RandomNumber;

public class ItemScatter {
	
	public static Item position(Item item, Zone zone) {
		Double latitude = RandomNumber.getRandomDoubleNumber(
				zone.getMinLattitudeValue(), 
				zone.getMaxLattitudeValue());
		Double longitude = RandomNumber.getRandomDoubleNumber(
				zone.getMinLongitudeValue(), 
				zone.getMaxLongitudeValue());
		
		item.setLatitude(latitude);
		item.setLongitude(longitude);
		
		return item;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Item generate(Class<? extends Item> theClass, Zone zone) throws ScatterException {
		Item item = null;
		try {
			item = (Item) theClass.newInstance();
			item = ItemScatter.position(item, zone);
		} catch (InstantiationException e) {
			throw new ScatterException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ScatterException(e.getMessage());
		}
		
		item.setZoneID(zone.getID());
		return item;
	}

	/**
	 * @param theClass The type of item to generate
	 * @param amount The number of generated crystals
	 * @return Generated list of crystals
	 */
	public static List<Item> generate(Class<? extends Item> theClass, Zone zone, int amount) {
		List<Item> items = new ArrayList<Item>();
		for(int i = 0; i < amount; i++) {
			try {
				items.add(ItemScatter.generate(theClass, zone));
			} catch (ScatterException e) {
				System.err.println("Failed to generate crystal.");
			}
		}
		
		return items;
	}
	
	public static class ScatterException extends Exception {
		private static final long serialVersionUID = 7233935205790857341L;

		public ScatterException(String message) {
			super(message);
		}
	}

}
