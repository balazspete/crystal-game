package com.example.crystalgame.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import android.app.Dialog;
import android.graphics.Color;
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
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.GameEndActivity.GameEndType;
import com.example.crystalgame.game.energy.EnergyEvent;
import com.example.crystalgame.game.energy.EnergyManager;
import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.CrystalZone;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.Character.CharacterType;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction.RPSSelection;
import com.example.crystalgame.location.LocationManager;
import com.example.crystalgame.location.ZoneChangeEvent;
import com.example.crystalgame.ui.UIController;
import com.example.crystalgame.ui.UIControllerHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Activity where game play happens 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class GameActivity extends FragmentActivity implements UIControllerHelper, OnMarkerClickListener {

	GoogleMap map;
	private TextView Energy_Label = null; 
	private TextView Crystal_Label = null; 
	private TextView Magic_Label = null; 
	private TextView Time_Label = null; 
	View mapView;
	float level = 13.0f;
	private ArrayList<Location> gameBoundaryPoints= null;
	private ArrayList<Location> gameLocationPoints = null;
	private ArrayList<MagicalItem> magicalItemsList = null;
	private CrystalZone[] crystalZones = null;
	private ConcurrentHashMap<String, Marker> markersOnMap = new ConcurrentHashMap<String, Marker>();

	// Threads
	Thread gameUpdateThread, gameTimeThread;
	
	// Refresh the position of markers every 5 seconds
	private int UI_REFRESH_FREQUENCY = 5000;
	
	private InstructionEventListener instructionEventListener;

	public GameActivity() {	
	}

	// Initialize all the components in the client side
	private synchronized void initializeGameComponents() {
		// Passing a refrerence to the UIController
		UIController.getInstance().setCurrentActivity(this);
		
		//Toast.makeText(getApplicationContext(), "LocationTracking status : "+GPSTracker.getInstance().isEnableLocationCallbacks(), Toast.LENGTH_SHORT).show();
		
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
		//map.setOnMarkerClickListener(this);

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
		
		Time_Label=(TextView) findViewById(R.id.Time_message);  
		Time_Label.setVisibility(View.VISIBLE);  
		Time_Label.setText("0");

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

		System.out.println("In onStart()");
		
		// Passing a refrerence to the UIController
		UIController.getInstance().setCurrentActivity(this);
		
		createMap();
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
	
	private Polygon drawPolygon(List<Location> points, PolygonOptions options) {
		for(Location location : points){
			LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
			options.add(l);
		}
		
		return map.addPolygon(options);
	}
	
	private PolygonOptions addHole(List<Location> points, PolygonOptions options) {
		ArrayList<LatLng> it = new ArrayList<LatLng>();
		for(Location location : points){
			LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
			it.add(l);
		}
		
		return options.addHole(it);
	}
	
	private void drawMap() {
		map.clear();
		markersOnMap.clear();
		
		final ThroneRoom throneRoom = UIController.getInstance().getThroneRoom();
		
		PolygonOptions options = new PolygonOptions();
		options.strokeColor(Color.BLACK);
		options.strokeWidth(2);
		options.fillColor(Color.argb(100, 0, 0, 0));
		
		options = addHole(gameLocationPoints, options);
		drawPolygon(gameBoundaryPoints, options);
		
		if(null != throneRoom) {
			Location location = throneRoom.getLocation();
			map.addMarker(new MarkerOptions()
				.position(new LatLng(location.getLatitude(),location.getLongitude()))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.throne)));
			
			CircleOptions cOptions = new CircleOptions();
			cOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
			cOptions.fillColor(Color.argb(50, 210, 85, 70));
			cOptions.radius(5);
			cOptions.strokeWidth(1);
			cOptions.strokeColor(Color.rgb(210, 85, 70));
			
			map.addCircle(cOptions);
		}
	}
	
	private boolean debug = false;
	private void createMap() {
		gameBoundaryPoints = UIController.getInstance().getGameBoundaryPoints();
		gameLocationPoints = UIController.getInstance().getGameLocationPoints();
		crystalZones = UIController.getInstance().getCrystalZones();
		
		drawMap();
		
		// Update the map information
		gameUpdateThread = new Thread(new Runnable() {
			Character character;
			
			Crystal[] crystals;
			MagicalItem[] items;
			Character[] characters;
			boolean busy = false;
			
			@Override
			public void run() {
				Runnable updater = new Runnable(){
					@Override
					public void run() {
						List<String> currentIDs = new ArrayList<String>();

						if (character == null) {
							busy = false;
							return;
						}
						
						updateGameCrystalInfo(character.getCrystals().size());
						updateGameMagicalItemInfo(character.getMagicalItems().size());
					
					
						android.location.Location l = map.getMyLocation();
						if (l != null) {
							character.setLatitude(l.getLatitude());
							character.setLongitude(l.getLongitude());
							
							LocationManager.getInstance().setCharacterLocation(character);
						}
					
						
//						if (gameLocation != null) {
//							if (gameLocation.isWithin(character)) {
//								Toast.makeText(GameActivity.this, "Out of game location!", Toast.LENGTH_SHORT).show();
//								GameManager.getInstance().endGame(GameEndType.OUT_OF_TIME);
//							}
//						}
						
						for (Crystal crystal : crystals) {
							addDebugMark(crystal);
							boolean checks[] = crystal.rangeChecks(character);
							if (checks[0]) {
								if (markersOnMap.get(crystal.getID()) == null) {
									markersOnMap.put(crystal.getID(), 
										map.addMarker(new MarkerOptions()
											.position(new LatLng(crystal.getLatitude(),crystal.getLongitude()))
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.crystal_small))));
								}
								
								currentIDs.add(crystal.getID());
								
								if (checks[1]) {
									Toast.makeText(GameActivity.this, "In capture range of crystal", Toast.LENGTH_SHORT).show();
									if (!captureCrystal(character, crystal)) {
										Toast.makeText(GameActivity.this, "Failed to pick up crystal.", Toast.LENGTH_SHORT).show();
									} else {
										markersOnMap.remove(crystal.getID()).remove();
									}
								}
							} 
						} 
						
						for (MagicalItem item : items) {
							addDebugMark(item);
							boolean checks[] = item.rangeChecks(character);
							if (character.getCharacterType() == CharacterType.SAGE ||
									checks[0]) {
								if (markersOnMap.get(item.getID()) == null) {
									markersOnMap.put(item.getID(), 
										map.addMarker(new MarkerOptions()
											.position(new LatLng(item.getLatitude(), item.getLongitude()))
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.magical_item)))); 
								}
								
								currentIDs.add(item.getID());
								
								if (checks[1]) {
									Toast.makeText(GameActivity.this, "In capture range of magical item", Toast.LENGTH_SHORT).show();
									if (!captureMagicalItem(character, item)) {
										Toast.makeText(GameActivity.this, "Failed to pick up magical item.", Toast.LENGTH_SHORT).show();
									} else {
										markersOnMap.remove(item.getID()).remove();
									}
								}
							}
						}
						
						for (Character c : characters) {
							addDebugMark(c);
							if (c.getID().equals(character.getID()) || c.getCharacterType() == CharacterType.UNKNOWN) {
								continue;
							}
							
							boolean checks[] = c.rangeChecks(character);
							if (character.getCharacterType() == CharacterType.WARRIOR ||
									checks[0]) {
								if (markersOnMap.get(c.getID()) == null) {
									markersOnMap.put(c.getID(), 
										map.addMarker(new MarkerOptions()
											.position(new LatLng(c.getLatitude(), c.getLongitude()))
											.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
											.title(c.getName())
											.snippet("Magical items: " + c.getMagicalItems().size())
										)); 
								}
								
								currentIDs.add(c.getID());
								
//								if (checks[1]) {
//									Toast.makeText(GameActivity.this, "In interaction range of character", Toast.LENGTH_SHORT).show();
//									InteractionManager.getInstance().initiateInteraction(
//											CrystalGame.getCommunication().out, c.getClientId());
//								}
							} 
						}
						
						removeUnusedMarkers(currentIDs);
						
						busy = false;
						debug = false;
					}
				};
				
				while(true) {
					if (!busy) {
						busy = true;
						character = InventoryManager.getInstance().getCharacter();
		
						try {
							crystals = ClientDataWarehouse.getInstance().getList(Crystal.class).toArray(new Crystal[0]);
							items = ClientDataWarehouse.getInstance().getList(MagicalItem.class).toArray(new MagicalItem[0]);
							characters = ClientDataWarehouse.getInstance().getList(Character.class).toArray(new Character[0]);
						} catch (DataWarehouseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						runOnUiThread(updater);
					}
					
					try {
						Thread.sleep(UI_REFRESH_FREQUENCY);
					} catch (InterruptedException e) {
						Log.e("GameActivity", "Map update thread");
					}
				}
			}
		});
		
		gameUpdateThread.start();
	}
	
	private DateTime lastCaptureTime = DateTime.now();
	
	private boolean captureCrystal(Character character, Crystal crystal) {
		if (lastCaptureTime.plusSeconds(10).isAfter(DateTime.now())) {
			return false;
		}
		
		return GameStateManager.getInstance().captureCrystal(character, crystal);
	}
	
	private boolean captureMagicalItem(Character character, MagicalItem item) {
		if (lastCaptureTime.plusSeconds(10).isAfter(DateTime.now())) {
			return false;
		}
		
		return GameStateManager.getInstance().captureMagicalItem(character, item);
	}
	
	private void addDebugMark(Artifact location) {
		if (!debug) {
			return;
		}
		
		CircleOptions cOptions = new CircleOptions();
		cOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
		cOptions.radius(1);
		cOptions.strokeWidth(1);
		cOptions.strokeColor(Color.WHITE);
		
		switch(location.getArtifactType()) {
		case CHARACTER: 
			cOptions.fillColor(Color.argb(50, 25, 180, 30)); break;
		case CRYSTAL:
			cOptions.fillColor(Color.argb(50, 30, 170, 170)); break;
		case MAGICAL_ITEM:
			cOptions.fillColor(Color.argb(50, 180, 40, 160)); break;
		}
		
		map.addCircle(cOptions);
	}
	
	private void removeUnusedMarkers(List<String> ids) {
		Enumeration<String> enumeration = markersOnMap.keys();
		while (enumeration.hasMoreElements()) {
			String id = enumeration.nextElement();
			if (!ids.contains(id)) {
				Marker marker= markersOnMap.remove(id);
				if (marker != null) {
					marker.remove();
				}
			}
		}
	}

	@Override
	public synchronized void zoneChanged(final ZoneChangeEvent zoneChangeEvent) {
		/*
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
		*/
	} 
	
	@Override
	public synchronized  void updateGameCrystalInfo(final int noOfCrystals) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("GameActivity", "Crystal Count updated to "+noOfCrystals);
				Crystal_Label.setText(""+noOfCrystals);
			}
		});
	}

	@Override
	public synchronized void updateGameMagicalItemInfo(final int noOfMagicalItems) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("GameActivity", "Magical Item count updated to "+noOfMagicalItems);
				Magic_Label.setText(""+noOfMagicalItems);
			}
		});
	}

	@Override
	public void updateGameEnergyInfo(final String energy) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Energy_Label.setText(energy);
			}
		});
	}
	
	public void lowEnergyWarning(final EnergyEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(GameActivity.this, "Your energy is low!", Toast.LENGTH_SHORT).show();
			}
		});
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
		case R.id.debug:
			boolean checked = !item.isChecked();
			item.setChecked(checked);
			debug = checked;
			drawMap();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private synchronized void leaveGroup() {
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
					if (InteractionManager.getInstance().isMaster()) {
						showDuelDialog();
					}
				}
				break;
			case RPS_SELECTION_REPLY:
				int result = InteractionManager.getInstance().onRPSSelectionReply(instruction);
				if (result == -1) {
					Toast.makeText(this, "You lost the duel", Toast.LENGTH_SHORT).show();
					InteractionManager.getInstance().remoteWin(out);
				} else if (result == 1) {
					Toast.makeText(this, "You won the duel", Toast.LENGTH_SHORT).show();
					InteractionManager.getInstance().localWin(out);
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

	@Override
	public void onStop() {
		super.onStop();
		gameTimeThread.destroy();
		gameUpdateThread.destroy();
		EnergyManager.getInstance().stopEnergyManager();
		System.out.println("Threads destroyed");
	}

	/**
	 * Invoke the location tracker callback to detect the item manually
	 */
	@Override
	public boolean onMarkerClick(Marker clickedMarker) {
		//Toast.makeText(getBaseContext(), "Inside Marker click handler : "+clickedMarker, Toast.LENGTH_LONG).show();
		if(null != markersOnMap) {
			for(Entry <String, Marker> entry : markersOnMap.entrySet()) {
				if(clickedMarker.getId().equals(entry.getValue().getId())) {
					Crystal crystalItem = null;
					MagicalItem magicalItem = null;
					
					try {
						crystalItem = (Crystal)ClientDataWarehouse.getInstance().get(Crystal.class, entry.getKey());
						magicalItem = (MagicalItem)ClientDataWarehouse.getInstance().get(MagicalItem.class, entry.getKey());
					} catch (DataWarehouseException e) {
						e.printStackTrace();
					}
					
					if(crystalItem != null) {
						com.example.crystalgame.library.data.Character c = UIController.getInstance().getGameCharacter();
						
						android.location.Location location1 = new android.location.Location("character");
						location1.setLatitude(c.getLatitude());
						location1.setLongitude(c.getLongitude());
						
						android.location.Location location2 = new android.location.Location("crystal");
						location1.setLatitude(crystalItem.getLatitude());
						location1.setLongitude(crystalItem.getLongitude());
						
						float distancebetween  = location1.distanceTo(location2);
						
						Toast.makeText(getBaseContext(), "Crystal Detected : "+distancebetween, Toast.LENGTH_LONG).show();
					}
					
					if(magicalItem != null) {
						//Toast.makeText(getBaseContext(), "Magical Item Detected : "+magicalItem, Toast.LENGTH_LONG).show();
					}
					
					// If none of the items exist, then remove it from the game map
					if(null == crystalItem && null == magicalItem) {
						markersOnMap.get(entry.getKey()).setVisible(false);
						markersOnMap.get(entry.getKey()).remove();
						Toast.makeText(getBaseContext(), "Item has been picked up by another player", Toast.LENGTH_LONG).show();
					}
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void removeCrystalFromMap(final Crystal item) {
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				markersOnMap.get(item.getID()).setVisible(false);
				markersOnMap.get(item.getID()).remove();
				System.out.println("Crystal Item removed...");
			}
		});
	}

	@Override
	public void removeMagicalItemFromMap(final MagicalItem item) {
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				markersOnMap.get(item.getID()).setVisible(false);
				markersOnMap.get(item.getID()).remove();
				System.out.println("Magical Item removed...");
			}
		});
	}

	@Override
	public void timeChangeCallback(final String newTime) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Time_Label.setVisibility(View.INVISIBLE);
//				Time_Label.setText(newTime);
			}
		});
	}
}
