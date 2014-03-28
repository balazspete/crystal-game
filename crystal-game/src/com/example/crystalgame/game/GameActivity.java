package com.example.crystalgame.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.maps.LocalMapPolygon;
import com.example.crystalgame.library.communication.outgoing.OutgoingMessages;
import com.example.crystalgame.library.data.Character.PlayerType;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.data.Warrior;
import com.example.crystalgame.library.data.Wizard;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.InstructionFormatException;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction.RPSSelection;
import com.example.crystalgame.location.GPSTracker;
import com.example.crystalgame.location.ZoneChangeEvent;
import com.example.crystalgame.location.ZoneChangeEvent.ZoneType;
import com.example.crystalgame.ui.UIController;
import com.example.crystalgame.ui.UIControllerHelperInter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity where game play happens 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class GameActivity extends FragmentActivity implements UIControllerHelperInter,LocationListener {

	GoogleMap map;
	private TextView Energy_Label = null; 
	private TextView Crystal_Label = null; 
	private TextView Magic_Label = null; 
	View mapView;
	float level = 13.0f;
	private ArrayList<Location> gameBoundaryPoints= null;
	private ArrayList<Location> gameLocationPoints = null;
	private ArrayList<MagicalItem> magicalItemsList = null;
	private Map<String, Marker> markersOnMap = new HashMap<String, Marker>();

	// Refresh the position of markers every 5 seconds
	private int UI_REFRESH_FREQUENCY = 5000;
	
	private InstructionEventListener instructionEventListener;

	public GameActivity() {	
	}

	// Initialize all the components in the client side
	private void initializeGameComponents() {
		// Passing a refrerence to the UIController
		UIController.getInstance().setCurrentActivity(this);

		// Starting the location tracking service
		startService(new Intent(this,GPSTracker.class));

		// Enabling different components
		UIController.getInstance().startComponents();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		initializeGameComponents();

		/*Get the map instance*/
		FragmentManager fragmentManager = getSupportFragmentManager();
		SupportMapFragment supportMapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
		map = supportMapFragment.getMap();
		mapView = supportMapFragment.getView();

		/*Set my location true on map*/
		map.setMyLocationEnabled(true);

		// Action Bar
		Crystal_Label=(TextView) findViewById(R.id.Crystal_message);  
		Crystal_Label.setVisibility(View.VISIBLE);  
		Crystal_Label.setText("0");  

		Energy_Label=(TextView) findViewById(R.id.Energy_message);  
		Energy_Label.setVisibility(View.VISIBLE);  
		Energy_Label.setText("10");  

		Magic_Label=(TextView) findViewById(R.id.Magic_message);  
		Magic_Label.setVisibility(View.VISIBLE);  
		Magic_Label.setText("0");

		// Searching for location services as a thread to avoid application deadlock
		new Thread(new Runnable() {
			@Override
			public void run() {
				android.location.Location location = null;
				do {
					location = GPSTracker.getInstance().getLocation();
				} while(null != location);

				LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 17));
			}
		}).start();

		final RadioButton magicItemButton = (RadioButton) findViewById(R.id.main_tab_magic);
		magicItemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ArrayList<HashMap<String, String>> dataList;

						final String KEY_ICON = "icon";
						final String KEY_DESC = "desc";

						ListView listOfItems = new ListView(getApplicationContext());
						magicalItemsList = UIController.getInstance().getMagicalItemInfoList();
						HashMap<String, String> map = null;

						dataList = new ArrayList<HashMap<String, String>>();

						for(MagicalItem magicalItem :magicalItemsList)
						{
							map = new HashMap<String, String>();
							map.put(KEY_ICON, Integer.toString(R.drawable.bg_info_panel_icon_2));
							map.put(KEY_DESC, magicalItem.getMagicalItemDescription());
							dataList.add(map);
						}

						ListAdapter listAdapter = new SimpleAdapter(
							getApplicationContext()
							,	dataList
							,	R.layout.dialog_magical_item
							,	new String[] {
									KEY_ICON
								,	KEY_DESC
							}
							,	new int[] {
									R.id.magicalItemImage
								,	R.id.magicalItemDesc
							}
						);
						
						listOfItems.setAdapter(listAdapter);

						final Dialog dialog = new Dialog(GameActivity.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(listOfItems);

						dialog.show();
					}
				});

				magicItemButton.setChecked(false);
			}
		});

		// Update the map information
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isRunning = true;
				while(isRunning) {
					try {
						// Check if elements are already added to the marker map
						if(null != markersOnMap && markersOnMap.size() > 0) {
							Map<String, Marker> accessedMarkers = new HashMap<String, Marker>();

							for(Crystal crystal : UIController.getInstance().getGameCrystals()) {
								// Add the marker to the temp map
								accessedMarkers.put(crystal.getID(), markersOnMap.get(crystal.getID()));

								// Remove the marker from the existing map
								markersOnMap.remove(crystal.getID());
							}

							for(MagicalItem magicalItem : UIController.getInstance().getGameMagicalItems()) {
								// Add the marker to the temp map
								accessedMarkers.put(magicalItem.getID(), markersOnMap.get(magicalItem.getID()));

								// Remove the marker from the existing map
								markersOnMap.remove(magicalItem.getID());
							}

							for(com.example.crystalgame.library.data.Character character : UIController.getInstance().getGameCharacters()) {
								// Add the marker to the temp map
								accessedMarkers.put(character.getID(), markersOnMap.get(character.getID()));

								// Remove the marker from the existing map
								markersOnMap.remove(character.getID());
							}

							// If there are any marker left in the map, then those items 
							// are no longer available. So remove it.
							for(Object itemID : markersOnMap.keySet().toArray()) {
								((Marker)markersOnMap.get(itemID)).remove();
							}

							markersOnMap = accessedMarkers;
						}
						
						Thread.sleep(UI_REFRESH_FREQUENCY);
					} catch (InterruptedException e) {
						// Stop thread execution
						isRunning = false;
					}
				}
			}
		}).start();
		
		instructionEventListener = new InstructionEventListener() {
			@Override
			public void onGroupStatusInstruction(InstructionEvent event) { }
			@Override
			public void onGroupInstruction(InstructionEvent event) { }
			@Override
			public void onGameInstruction(InstructionEvent event) { }
			@Override
			public void onDataTransferInstruction(InstructionEvent event) { }
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) { }
			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) { }
			
			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) {
				handleCharacterInteractionInstruction((CharacterInteractionInstruction) event.getInstruction());
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Passing a refrerence to the UIController
		UIController.getInstance().setCurrentActivity(this);

		
		// Each time the whole set of markers are created, clear the marker map
		markersOnMap.clear();

		gameBoundaryPoints = UIController.getInstance().getGameBoundaryPoints();

		if(null != gameBoundaryPoints) {
			for(Location location: gameBoundaryPoints) {
				map.addMarker(new MarkerOptions()
				.position(new LatLng(location.getLatitude(),location.getLongitude()))
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
			}
		}

		gameLocationPoints = UIController.getInstance().getGameLocationPoints();

		if(null != gameLocationPoints) {
			for(Location location: gameLocationPoints) {
				map.addMarker(new MarkerOptions()
					.position(new LatLng(location.getLatitude(),location.getLongitude()))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
			}
		}

		Crystal[] crystals = UIController.getInstance().getGameCrystals();

		if(null != crystals) {
			Marker tempMarker = null;
			// Displaying Crystals
			for(Location location : crystals) {
				tempMarker = map.addMarker(new MarkerOptions()
					.position(new LatLng(location.getLatitude(),location.getLongitude()))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
				markersOnMap.put(location.getID(), tempMarker);
			}
		}

		MagicalItem[] magicalItems = UIController.getInstance().getGameMagicalItems();

		if(null != magicalItems) {
			Marker tempMarker = null;

			// Displaying Magical Items
			for(Location location : magicalItems) {
				tempMarker = map.addMarker(new MarkerOptions()
					.position(new LatLng(location.getLatitude(),location.getLongitude()))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				markersOnMap.put(location.getID(), tempMarker);
			}
		}

		com.example.crystalgame.library.data.Character gameCharacter = UIController.getInstance().getGameCharacter();
		com.example.crystalgame.library.data.Character[] gameCharacters = UIController.getInstance().getGameCharacters(); 

		if(null != gameCharacter && null != gameCharacters) {
			// Displaying Characters and different markers based on player types
			for(com.example.crystalgame.library.data.Character player : gameCharacters) {
				Marker tempMarker = null;

				// If the character is same as the game character, skip
				if(gameCharacter.getID().equals(player.getID())) {
					continue;
				}

				// If warrior, show players as red dots
				else if(gameCharacter instanceof Warrior && player.getPlayerType().equals(PlayerType.PLAYER)) {
					tempMarker = map.addMarker(new MarkerOptions()
						.position(new LatLng(gameCharacter.getLatitude(),gameCharacter.getLongitude()))
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				}

				// If wizard, show NPCs as purple dots
				else if(gameCharacter instanceof Wizard && player.getPlayerType().equals(PlayerType.NPC)) {
					tempMarker = map.addMarker(new MarkerOptions()
						.position(new LatLng(gameCharacter.getLatitude(),gameCharacter.getLongitude()))
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
				}
				else {
					tempMarker = map.addMarker(new MarkerOptions()
						.position(new LatLng(player.getLatitude(),player.getLongitude()))
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))); 
				}
				markersOnMap.put(player.getID(), tempMarker);
			}
		}

		// Adding throne room to the current location of the host
		ThroneRoom throneRoom = UIController.getInstance().getThroneRoom();
		if(null != throneRoom) {
			Location location = throneRoom.getLocation();
			map.addMarker(new MarkerOptions()
				.position(new LatLng(location.getLatitude(),location.getLongitude()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.throne)));
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		((CrystalGame) getApplication()).getCommunication().in.addInstructionEventListener(instructionEventListener);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		((CrystalGame) getApplication()).getCommunication().in.removeInstructionEventListener(instructionEventListener);
	}

	@Override
	public void zoneChanged(final ZoneChangeEvent zoneChangeEvent) {
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
			new OnGlobalLayoutListener() {
				public void onGlobalLayout() {
					mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					
					while(true) {
						Point point;
						int counter = 0;
						Projection projection = map.getProjection();
						if(zoneChangeEvent.getZoneType().equals(ZoneType.GAME_LOCATION)) {
							for(Location location: gameLocationPoints)
							{
								point = projection.toScreenLocation(new LatLng(location.getLatitude(),location.getLongitude()));
								if(point.x>0 && point.x<size.x && point.y>0 && point.y<size.y) {
									counter++;
								}
							}

							if(counter == 4) {
								level+= 0.1;
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LocalMapPolygon().zoomCenterPoint(gameBoundaryPoints), level));
			
							} else {
								break;
							}
						} else if(zoneChangeEvent.getZoneType().equals(ZoneType.GAME_BOUNDARY)) {
							for(Location location: gameBoundaryPoints) {
								point = projection.toScreenLocation(new LatLng(location.getLatitude(),location.getLongitude()));
								if(point.x>0 && point.x<size.x && point.y>0 && point.y<size.y) {
									counter++;
								}
							}
							
							if(counter < 4)	{
								level-= 0.1;
								System.out.println("level :"+ level);
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LocalMapPolygon().zoomCenterPoint(gameBoundaryPoints), level));
			
							} else if (counter==4) {
								break;
							}
						}
			
					}
				}
			});
		}
	} 
	
	@Override
	public void updateGameCrystalInfo(final int noOfCrystals) {
		Log.d("GameActivity", "Crystal Count updated to "+noOfCrystals);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Crystal Count Updated to "+noOfCrystals, Toast.LENGTH_LONG).show();
				Crystal_Label.setText(""+noOfCrystals);
			}
		});
	}

	@Override
	public void updateGameMagicalItemInfo(final int noOfMagicalItems) {
		Log.d("GameActivity", "Magical Item count updated to "+noOfMagicalItems);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Magical Item count updated to "+noOfMagicalItems, Toast.LENGTH_LONG).show();
				Magic_Label.setText(""+noOfMagicalItems);
			}
		});
	}

	@Override
	public synchronized void energyLow(EnergyEvent energyEvent) {
		Log.d("GameActivity", "energy is low"+energyEvent);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "energy is low", Toast.LENGTH_LONG).show();
			}
		});
	}


	@Override
	public synchronized void energyChangeCallBack(final String energyLevel) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Energy_Label.setText(""+energyLevel);
			}
		});
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_leave_group:
			leaveGroup();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void leaveGroup() {
		GroupInstruction instruction = GroupInstruction.leaveGroup();
		((CrystalGame)getApplication()).getCommunication().out.sendGroupInstructionToServer(instruction);
	}
	
	private void handleCharacterInteractionInstruction(CharacterInteractionInstruction instruction) {
		ClientOutgoingMessages out = ((CrystalGame) getApplication()).getCommunication().out;
		switch (instruction.characterInteractionInstructionType) {
			case INTERACTION_REQUEST:
				InteractionManager.getInstance().onInteractionRequest(out, instruction);
				break;
			case INTERACTION_REQUEST_ACK:
				if (InteractionManager.getInstance().onInteractionRequestAcknowledgment(instruction)) {
					showDuelDialog();
				}
				break;
			case RPS_SELECTION_REPLY:
				int result = InteractionManager.getInstance().onRPSSelectionReply(instruction);
				if (result == -1) {
					Toast.makeText(this, "You lost the duel", Toast.LENGTH_SHORT).show();
					InteractionManager.getInstance().removeWin();
				} else if (result == 1) {
					Toast.makeText(this, "You won the duel", Toast.LENGTH_SHORT).show();
					InteractionManager.getInstance().localWin();
				} else {
					showDuelDialog();
				}
				break;
			case RPS_SELECTION_REQUEST:
				showDuelDialog();
				break;
			case RESULT:
				boolean r = (Boolean) instruction.arguments[0];
				Toast.makeText(this, r ? "You won the duel" : "You lost the duel", Toast.LENGTH_SHORT).show();
				break;
			default:
				System.err.println("GameActivity|handleCaracterInteractionInstruction: Unhandled CharacterInteractionInstruction");
		}
	}
	
	private void showDuelDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.layout_duel);
		dialog.setTitle("Duel");
		TextView text = (TextView) dialog.findViewById(R.id.instruction_text);
		text.setText("TODO");
		
		Button[] buttons = new Button[] {
			(Button) dialog.findViewById(R.id.button_rock),
			(Button) dialog.findViewById(R.id.button_paper),
			(Button) dialog.findViewById(R.id.button_scissor)
		};
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				CharacterInteractionInstruction.RPSSelection selection;
				switch (v.getId()) {
					case R.id.button_paper:
						selection = RPSSelection.PAPER;
						break;
					case R.id.button_rock:
						selection = RPSSelection.ROCK;
					case R.id.button_scissor:
						selection = RPSSelection.SCISSORS;
					default:
						selection = null;
				}
				
				ClientOutgoingMessages out = ((CrystalGame) getApplication()).getCommunication().out;
				if (InteractionManager.getInstance().onRPSSelection(out, selection)) {
					dialog.hide();
				}
			}
		};
		
		for (Button button : buttons) {
			button.setOnClickListener(listener);
		}
		
		dialog.show();
	}

}
