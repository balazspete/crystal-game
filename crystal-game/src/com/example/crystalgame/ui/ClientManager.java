package com.example.crystalgame.ui;

import com.example.crystalgame.library.data.GameBoundary;

/**
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 *
 */
public class ClientManager {
	
	private static ClientManager clientManagerInstance = null;
	
	private ClientManager() {	}

	public static ClientManager getInstance() {
		if(null == clientManagerInstance) {
			clientManagerInstance = new ClientManager();
		}
		return clientManagerInstance;
	}

	public void saveGameBoundary(GameBoundary gameBoundary) {
		GameManager.getInstance().saveGameBoundary(gameBoundary);
	}
	
	public void saveGameLocation(GameBoundary gameBoundary) {
		GameManager.getInstance().saveGameLocation(gameBoundary);
	}
	
}
