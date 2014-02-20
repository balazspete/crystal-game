package com.example.crystalgame;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientCommunicationManager;
import com.example.crystalgame.communication.ClientOutgoingMessages;

/**
 * The Android application class for the CrystalGame project
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class CrystalGame extends Application {

	private static ClientCommunication communication;
	private String playerID = "PlayerID-123";
	private String groupID = "";
	
	@Override
	public void onCreate() {
		super.onCreate();
		addCommunication();
	}
	
	/**
	 * Get the communication module
	 * @return the communication module
	 */
	public ClientCommunication getCommunication() {
		return communication;
	}
	
	/**
	 * Change the address to connect to
	 * @param address The new address
	 */
	public void serverAddressChange(String address) {
		communication.serverAddressChange(address);
	}
	
	/**
	 * Change the port used for the connection
	 * @param port The new port number
	 */
	public void portChange(int port) {
		communication.portChange(port);
	}
	
	private void addCommunication() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Integer port  = null;
		try {
			// Get the port from settings
			port = Integer.parseInt(sp.getString(getString(R.string.PORT), "3000"));
		} catch(NumberFormatException e) {
			Log.e("CommunicationService", e.getMessage());
		} finally {
			if (port == null) {
				port = 3000;
			}
		}
		
		// Create the communication manager and get the address form the config
		ClientCommunicationManager manager = new ClientCommunicationManager(
				sp.getString(getString(R.string.SERVER_ADDRESS), "example.com"), 
				port);
		
		ClientOutgoingMessages out = new ClientOutgoingMessages();
		communication = new ClientCommunication(manager, out);
		communication.in.addInstructionEventListener(new ServerInstructionsHandler(this));
	}

	public String getPlayerID()
	{
		return this.playerID;
	}
	
	public void setPlayerID(String ID)
	{
		this.playerID = ID;
	}
	
	public String getGroupID()
	{
		return this.groupID;
	}
	
	public void setGroupID(String ID)
	{
		this.groupID = ID;
	}
}
