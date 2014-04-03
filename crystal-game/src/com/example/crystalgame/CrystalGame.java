package com.example.crystalgame;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientCommunicationManager;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.CharacterSelectionActivity;
import com.example.crystalgame.game.CreateGameActivity;
import com.example.crystalgame.game.GameEndActivity;
import com.example.crystalgame.game.GameEndActivity.GameEndType;
import com.example.crystalgame.groups.GroupLobbyActivity;
import com.example.crystalgame.library.communication.messages.IdMessage;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.instructions.DataTransferInstruction;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.ui.UIController;


/**
 * The Android application class for the CrystalGame project
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class CrystalGame extends Application {

	public static final String 
		CLIENT_ID_KEY = "com.example.crystalgame.client_id",
		GROUP_ID_KEY = "com.example.crystalgame.group_id";
	
	private static ClientCommunication communication;
	private static String clientID;
	private static String groupID;
	private static String myName;
	
	private SharedPreferences preferences;
	private InstructionEventListener listenerForDW;
	
	private static int timeDuration = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.preferences = getSharedPreferences("crystal-game", Context.MODE_PRIVATE);
		restoreValues();
		
		addCommunication();
		incomingCommunicationsSetup();
		configDataWarehouse(); // This will have to be called again when we get the client ID or if we join a group
//		setupDataWarehouse(null);
	}
	
	/**
	 * Get the communication module
	 * @return the communication module
	 */
	public static ClientCommunication getCommunication() {
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
				setClientID((String) message.getData());
				Log.i("CrystalGame", "Client ID updated to " + clientID);
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
						System.out.println("Group SUCCESS: " + instruction.arguments);
						if(instruction.arguments != null && instruction.arguments.length > 0 && instruction.arguments[0] != null) {
							setGroupID((String) instruction.arguments[0]);
							Log.i("CrystalGame", "Group ID updated to " + groupID + ". Initialising data warehouse...");
							configDataWarehouse();
						} else {
							setGroupID(null);
							Intent intent = new Intent(getApplicationContext(), CrystalGameLaunchActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
						break;
					case IS_MEMBER_REPLY:
						if ((Boolean) instruction.arguments[0]) {
							Intent intent = new Intent(getApplicationContext(), GroupLobbyActivity.class);
							intent.putExtra(GroupLobbyActivity.KEY_LOAD_DW, true);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else {
							CrystalGameLaunchActivity.getInstance().tryToRejoinGroupFailed();
						}
					default:
						System.out.println("Unhandled group instruction");
						break;
				}
			}
			
			@Override
			public void onGameInstruction(InstructionEvent event) {
				GameInstruction instruction = (GameInstruction) event.getInstruction();
				Intent intent;
				switch (instruction.gameInstruction) {
					case CREATE_GAME_REQUEST:
						intent = new Intent(getApplicationContext(), CreateGameActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						break;
					case GAME_STARTED:
						Log.i("CrystalGame", "Game Started");
						intent = new Intent(getApplicationContext(), CharacterSelectionActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						break;
					case GAME_ENDED:
						Log.i("CrystalGame", "Game Ended");
						endGame(GameEndType.OUT_OF_TIME);
						break;
					case GAME_BOUNDARY_OUTSIDE_RESPONSE:
						endGame(GameEndType.OUT_OF_BOUNDARY);
						break;
					case ENERGY_DISQUALIFY_RESPONSE:
						endGame(GameEndType.OUT_OF_ENERGY);
						break;
					default:
						// ingored
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onDataTransferInstruction(InstructionEvent event) {
				System.out.println("Data transfer instruction");
				DataTransferInstruction instruction = (DataTransferInstruction) event.getInstruction();
				switch (instruction.transferInstructionType) {
					case DW_DOWNLOAD_REPLY:
						System.out.println("DOWNLOAD REPLY, " + instruction.arguments.length);
						setupDataWarehouse(instruction);
						break;
					default: 
						// ignore
						break;
				}
			}

			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) {
				// TODO this can be used to detect disconnection and reconnection to the server
			}

			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) {
				// TODO 
			}
		});
	}
	
	public void endGame(GameEndType type) {
		Intent intent = new Intent(getApplicationContext(), GameEndActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(GameEndActivity.GAME_END_TYPE, type);
		startActivity(intent);
	}
	
	public void configDataWarehouse() {
		ClientDataWarehouse.DB_PATH = this.getExternalCacheDir().getAbsolutePath();
		ClientDataWarehouse.myID = clientID;
		ClientDataWarehouse.groupID = groupID;
		
		listenerForDW = new InstructionEventListener() {
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {
				// Send the emitted instruction to the server
				getCommunication().out.relayInstructionToServer(event.getInstruction());
			}
			
			@Override
			public void onDataTransferInstruction(InstructionEvent event) {
				getCommunication().out.relayInstructionToServer(event.getInstruction());
			}
			
			// Ignore these events as they will not be emitted
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			@Override
			public void onGroupInstruction(InstructionEvent event) {}
			@Override
			public void onGameInstruction(InstructionEvent event) {}
			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) {}
			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) {}
		};
		
		getCommunication().out.relayInstructionToServer(DataTransferInstruction.createDataWarehouseDownloadRequestInstruction());
	}
	
	public void setupDataWarehouse(DataTransferInstruction instruction) {
		try {
			if (ClientDataWarehouse.isNull) {
				if (instruction != null) {
					ClientDataWarehouse.createFromWrappers(instruction.arguments);
				} else {
					ClientDataWarehouse.create();
				}
				
				ClientDataWarehouse.getInstance().addInstructionEventListener(listenerForDW);
			} else {
				ClientDataWarehouse.getInstance().updateWithWrappers(instruction.arguments);
			}
		} catch (DataWarehouseException e) {
			Log.e("CrystalGame", e.getMessage());
		}
	}

	public static String getClientID()
	{
		return clientID;
	}
	
	public void setClientID(String ID)
	{
		if (clientID != null) {
			return;
		}
		
		clientID = ID;
		
		Editor e = preferences.edit();
		e.putString(CLIENT_ID_KEY, clientID);
		e.apply();
	}
	
	public static String getGroupID()
	{
		return groupID;
	}
	
	public void setGroupID(String ID)
	{
		groupID = ID;
		
		Editor e = preferences.edit();
		e.putString(GROUP_ID_KEY, groupID);
		e.apply();
	}
	
	/**
	 * @return the myName
	 */
	public static String getMyName() {
		return myName;
	}

	/**
	 * @param myName the myName to set
	 */
	public static void setMyName(String myName) {
		CrystalGame.myName = myName;
	}
	
	public ArrayList<Location> getGameBoundaryPoints() {
		return UIController.getInstance().getGameBoundaryPoints();	
	}
	
	public ArrayList<Location> getGameLocationPoints() {
		return UIController.getInstance().getGameLocationPoints();
	}
	
	private void restoreValues() {
		clientID = preferences.getString(CLIENT_ID_KEY, null);
		groupID = preferences.getString(GROUP_ID_KEY, null);	
		
		System.out.println("Restored preferences: " + clientID + " " + groupID);
		if (clientID!= null) {
			if (groupID != null) {
	//			configDataWarehouse();
				System.out.println("Restoring application state, downloading data warehouse...");
			} 
		} else {
			System.out.println("Nothing to restore");
		}
	}

	@Override
	public void onTerminate() {
		Log.d("CrystalGame:onTerminate()","Started cleaning process...");
		
		//UIController.getInstance().stopComponents();
		// Stopping GPS tracking service
		
		Log.d("CrystalGame:onTerminate()","Finished cleaning process successfully!");
	}

	public static int getTimeDuration() {
		return timeDuration;
	}

	public static void setTimeDuration(int duration) {
		timeDuration = duration;
	}
	
}
