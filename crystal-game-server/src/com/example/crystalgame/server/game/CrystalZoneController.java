package com.example.crystalgame.server.game;
/**
 * Crystal Zone Controller
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.List;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;

public class CrystalZoneController 
{
	
	private DB4OInterface store;
	private CrystalZone crystalZone;
	
	
	public CrystalZoneController(CrystalZone crystalZone) 
	{
		this.crystalZone = crystalZone;
	}
	
	/**
	 * 
	 * @param crystalLocation
	 */
	public boolean storeCystalZone()
	{
		return store.put(CrystalZone.class, crystalZone)  != null;	
	}
	
	/**
	 * 
	 * @param crystalLocation
	 */
	public void storeCystalZones(List<CrystalZone> crystalZones)
	{
		for(CrystalZone crystalZone : crystalZones)
		{
			this.store.put(CrystalZone.class, crystalZone);
		}
	}
}
