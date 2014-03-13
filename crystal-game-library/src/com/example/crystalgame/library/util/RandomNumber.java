package com.example.crystalgame.library.util;

import java.util.Random;

/**
 * A utility to generate random number
 * @author Shen Chen
 *
 */
public class RandomNumber 
{


	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomIntNumber(int start, int end) 
	{
	    return new Random().nextInt((start - end) + 1) + end;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static double getRandomDoubleNumber(double start, double end) 
	{
		return  start + (new Random().nextDouble() * (end - start));
	}


}
