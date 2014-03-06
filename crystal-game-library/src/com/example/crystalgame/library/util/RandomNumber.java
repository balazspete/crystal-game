package com.example.crystalgame.library.util;

import java.util.Random;

/**
 * A utility to generate random number
 * @author Shen Chen
 *
 */
public class RandomNumber 
{

	public static int getRandomNumber(int min, int max) 
	{
	    return new Random().nextInt((max - min) + 1) + min;
	}

}
