package com.example.crystalgame.server.game;
/**
 * Character Controller
 * @author Chen Shen, Rajan verma
 *
 */
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;


public class CrystalController 
{	
	private DB4OInterface store;
	private Crystal crystal;
	
	public CrystalController() 
	{
		
	}

	public CrystalController(Crystal crystal) 
	{
		this.crystal = crystal;
	}
	/**
	 * 
	 * @return
	 */
	public boolean storeCrystal()
	{
		return store.put(Crystal.class, crystal) != null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Crystal getCrystal()
	{
		return (Crystal) store.get(Crystal.class, crystal.getID());
	}

}
