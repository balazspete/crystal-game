package com.example.crystalgame.library.util;

import java.util.ArrayList;
import java.util.Collections;


public class ListController 
{

    /**
     * 
     * @param list
     * @return
     */
    public static double findMinDoubleNumber (ArrayList<Double> list) 
    {
    	
    	return Collections.min(list); 
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