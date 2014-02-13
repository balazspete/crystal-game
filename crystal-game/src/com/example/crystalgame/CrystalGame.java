package com.example.crystalgame;

import android.app.Application;

import com.example.crystalgame.communication.ClientCommunication;

public class CrystalGame extends Application {

	private ClientCommunication communication;
	private String playerID = "PlayerID-123";
	//private String groupID = "GroupID-123";
	private String groupID = "";
	public void addCommunication(ClientCommunication communication) {
		this.communication = communication;
	}
	
	public ClientCommunication getCommunication() {
		return communication;
	}
	
	public void serverAddressChange(String address) {
		communication.serverAddressChange(address);
	}
	
	public void portChange(int port) {

		System.out.println("changing port");
		communication.portChange(port);
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
