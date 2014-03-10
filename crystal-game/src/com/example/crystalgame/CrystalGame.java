package com.example.crystalgame;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientCommunicationManager;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.groups.GroupLobbyActivity;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.GameInstruction.GameInstructionType;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;
import com.example.crystalgame.ui.CreateGameActivity;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.ui.InformationPresenter;
import com.example.crystalgame.ui.UIController;

import java.util.Arrays;
import java.util.ArrayList;


/**
 * The Android application class for the CrystalGame project
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class CrystalGame extends Application {

	private static ClientCommunication communication;
	private String playerID;
	private String groupID;
	
	@Override
	public void onCreate() {
		super.onCreate();
		addCommunication();
		incomingCommunicationsSetup();
		setupDataWarehouse(); // This will have to be called again when we get the client ID
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
	}
	
	private void incomingCommunicationsSetup() {
		getCommunication().in.addMessageEventListener(new MessageEventListener() {
			@Override
			public void onMessageEvent(MessageEvent event) {
				System.out.print(event.getMessage().getMessageType());
				System.out.println("Message: " + event.getMessage().getData());
			}
			
			@Override
			public void onInstructionRelayMessage(MessageEvent event) {
				System.out.println("Instruction relay");
			}
			
			@Override
			public void onGroupStatusMessageEvent(MessageEvent event) {
				System.out.print("GroupStatusMessage: ");
			}

			@Override
			public void onControlMessage(MessageEvent event) {
				System.out.println("Control Message");
			}

			@Override
			public void onIdMessageEvent(MessageEvent event) {
				IdMessage message = (IdMessage) event.getMessage();
				playerID = (String) message.getData();
				Log.i("CrystalGame", "Client ID updated to " + playerID);
				
				setupDataWarehouse();
			}
		});
		
		getCommunication().in.addInstructionEventListener(new InstructionEventListener() {
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {
				// Forward the instruction to the DW (casting will not throw an exception, ever, due to type check b4!)
				ClientDataWarehouse.getInstance().passInstruction((DataSynchronisationInstruction) event.getInstruction());
			}
			
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			
			@Override
			public void onGroupInstruction(InstructionEvent event) {
				GroupInstruction instruction = (GroupInstruction) event.getInstruction();
				switch(instruction.groupInstructionType) {
					case SUCCESS:
						if(instruction.arguments.length > 0) {
							groupID = (String) instruction.arguments[0];
							Log.i("CrystalGame", "Group ID updated to " + groupID);
						}
						
						break;
					default:
						System.out.println("Unhandled group instruction");
						break;
				}
			}
			
			@Override
			public void onGameInstruction(InstructionEvent event) {
				GameInstruction instruction = (GameInstruction) event.getInstruction();
				switch (instruction.gameInstruction) {
					case CREATE_GAME_REQUEST:
						Intent intent = new Intent(getApplicationContext(), CreateGameActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						break;
				}
			}
		});
	}
	
	private void setupDataWarehouse() {
		if (playerID == null) {
			return;
		}
		
		ClientDataWarehouse.DB_PATH = this.getExternalCacheDir().getAbsolutePath();
		ClientDataWarehouse.myID = playerID;
		ClientDataWarehouse.getInstance().addInstructionEventListener(new InstructionEventListener() {
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {
				// Send the emitted instruction to the server
				getCommunication().out.relayInstructionToServer(event.getInstruction());
			}
			
			// Ignore these events as they will not be emitted
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			@Override
			public void onGroupInstruction(InstructionEvent event) {}
			@Override
			public void onGameInstruction(InstructionEvent event) {}
		});
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
	public ArrayList<Location> getGameBoundaryPoints()
	{
		return UIController.getInstance().getGameBoundaryPoints();
		
	}
	public ArrayList<Location> getGameLocationPoints()
	{
		return UIController.getInstance().getGameLocationPoints();
	}
	
}
