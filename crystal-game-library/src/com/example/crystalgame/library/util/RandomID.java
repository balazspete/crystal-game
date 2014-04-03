package com.example.crystalgame.library.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * A utility to generate random identifiers
 * @author Balazs Pete, Shen Chen
 *
 */
public class RandomID {
	
	/**
	 * Get a random ID
	 * @return A random String ID
	 */
	public static String getRandomId() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}
}
