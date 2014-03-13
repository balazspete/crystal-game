package com.example.crystalgame.library.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.crystalgame.library.data.Location;

public class ListController 
{
    static List<Double> xPos;
    /**
     * 
     * @param list
     * @return
     */
    public static double findMinDoubleNumber (ArrayList<Location> gameBoundaryPoints) 
    {
    	
    	xPos = new ArrayList<Double>();
    	for(Location location:gameBoundaryPoints)
    	{
    		xPos.add(location.getLatitude());
    	}
          return Collections.min(xPos); 
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static double findMaxDoubleNumner (ArrayList<Double> list) 
    {
          return Collections.max(list); 
    }
}