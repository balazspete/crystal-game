package com.example.crystalgame.server.game;
/**
 * Magical Item Controller
 * @author Chen Shen, Rajan verma
 *
 */
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;

public class MagicalItemController 
{
	private DB4OInterface store;
	private MagicalItem magicalItem;
	
	public MagicalItemController() 
	{
		
	}

	public MagicalItemController(MagicalItem magicalItem) 
	{
		this.magicalItem = magicalItem;
	}
	/**
	 * 
	 * @return
	 */
	public boolean storeCrystal()
	{
		return store.put(MagicalItem.class, magicalItem) != null;
	}
	
	/**
	 * 
	 * @return
	 */
	public MagicalItem getMagicalItem()
	{
		return (MagicalItem) store.get(MagicalItem.class, magicalItem.getID());
	}

}
