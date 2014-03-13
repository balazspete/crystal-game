/**
 * 
 */
package com.example.crystalgame.ui;

/**
 * Interface for communication between fragments in the Game UI
 * 
 * @author Allen Thomas Varghese, Chen Shen
 */
public interface GameUIComponentCommunicationListener {
	public void updateGameCrystalInfo(int noOfCrystals);
	public void updateGameMagicalItemInfo(int noOfMagicalItems);
	
}
