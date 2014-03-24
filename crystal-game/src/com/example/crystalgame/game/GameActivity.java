/**
 * 
 */
package com.example.crystalgame.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.library.data.Character.PlayerType;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.MagicalItem;
import com.example.crystalgame.library.data.ThroneRoom;
import com.example.crystalgame.library.data.Warrior;
import com.example.crystalgame.library.data.Wizard;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.ui.ZoneChangeEvent.ZoneType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class GameActivity extends FragmentActivity implements UIControllerHelperInter,LocationListener {

	GoogleMap map;
    private TextView Energy_Label = null; 
    private TextView Crystal_Label = null; 
    private TextView Magic_Label = null; 
    private LocationManager locationManager; 
    private String locationFind;
    View mapView;
    float level = 13.0f;
    private ArrayList<Location> gameBoundaryPoints= null;
    private ArrayList<Location> gameLocationPoints = null;
    private ArrayList<MagicalItem> magicalItemsList = null;
	
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
        
        //Chen Shen - Action Bar
        Crystal_Label=(TextView) findViewById(R.id.Crystal_message);  
        Crystal_Label.setVisibility(View.VISIBLE);  
        Crystal_Label.setText("0");  
        
        Energy_Label=(TextView) findViewById(R.id.Energy_message);  
        Energy_Label.setVisibility(View.VISIBLE);  
        Energy_Label.setText("10");  

        Magic_Label=(TextView) findViewById(R.id.Magic_message);  
        Magic_Label.setVisibility(View.VISIBLE);  
        Magic_Label.setText("0");
        
      //  this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.GameMap)).getMap();
       // this.map.setMyLocationEnabled(true);
        
        /*location manager class to get current location latitude and longitude values*/
        this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
        Criteria criteria = new Criteria();
        
        /*Returns the name of the provider that best matches the given criteria*/
        this.locationFind = this.locationManager.getBestProvider(criteria, false);
        
        android.location.Location location = locationManager.getLastKnownLocation(locationFind);
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 17));
        
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
        				MagicalItem tempItem = null;
        				HashMap<String, String> map = null;

        				dataList = new ArrayList<HashMap<String, String>>();
        				
        				System.out.println("List size: = "+magicalItemsList.size());
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
	}

	@Override
	protected void onStart() {
		super.onStart();
        LocalMapInformation mapInformation = (LocalMapInformation) UIController.getInstance().loadGamePlayData(GamePlayState.LOCAL_MAP);
        
        if(null != mapInformation) {
		    
        	gameBoundaryPoints = UIController.getInstance().getGameBoundaryPoints();
		    
		    if(null != gameBoundaryPoints) {
			    for(Location location: gameBoundaryPoints)
			    {
			    	  map.addMarker(new MarkerOptions()
			        	.position(new LatLng(location.getLatitude(),location.getLongitude()))
			        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
			        );
			    }
		    }
		    
		    gameLocationPoints = UIController.getInstance().getGameLocationPoints();
		    
		    if(null != gameLocationPoints) {
			    for(Location location: gameLocationPoints)
			    {
			    	  map.addMarker(new MarkerOptions()
			        	.position(new LatLng(location.getLatitude(),location.getLongitude()))
			        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
			        );
			    }
		    }
		    
		    Crystal[] crystals = mapInformation.getCrystalList();
		    
		    if(null != crystals) {
			    // Displaying Crystals
			    for(Location location : crystals) {
			        map.addMarker(new MarkerOptions()
			        	.position(new LatLng(location.getLatitude(),location.getLongitude()))
			        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
			        );
			    }
		    }
		    
		    MagicalItem[] magicalItems = mapInformation.getMagicalItemList();
		    if(null != magicalItems) {
			    // Displaying Magical Items
			    for(Location location : magicalItems){
			        map.addMarker(new MarkerOptions()
				    	.position(new LatLng(location.getLatitude(),location.getLongitude()))
				    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
			        );
			    }
		    }
		    
		    com.example.crystalgame.library.data.Character gameCharacter = null;
		    gameCharacter = mapInformation.gameStateInformation.getGameCharacter();
		    
		    // Display different markers based on player types
	    	if(gameCharacter instanceof Warrior) {
	    		map.addMarker(new MarkerOptions()
		    	.position(new LatLng(gameCharacter.getLatitude(),gameCharacter.getLongitude()))
		    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
	    		);
	    	}
		    
		    // Displaying Characters
		    for(com.example.crystalgame.library.data.Character player : mapInformation.getCharacterList()) {
		    	// If the character is same as the game character, skip
		    	if(gameCharacter.getID().equals(player.getID())) {
		    		continue;
		    	} else if(gameCharacter instanceof Wizard && player.getPlayerType().equals(PlayerType.NPC)) {
		    		map.addMarker(new MarkerOptions()
			    	.position(new LatLng(gameCharacter.getLatitude(),gameCharacter.getLongitude()))
			    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		    		);
		    	} else {
			        map.addMarker(new MarkerOptions()
				    	.position(new LatLng(player.getLatitude(),player.getLongitude()))
				    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
			        ); 
		    	}
		    }
		    
		    // Adding throne room to the current location of the host
		    ThroneRoom throneRoom = UIController.getInstance().getThroneRoom();
		    if(null != throneRoom) {
		        Location location = throneRoom.getLocation();
		        map.addMarker(new MarkerOptions()
		    		.position(new LatLng(location.getLatitude(),location.getLongitude()))
		    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.throne))
		        );
		    }
        }
	}
    
	@Override
	public void zoneChanged(final ZoneChangeEvent zoneChangeEvent) {
		Toast.makeText(getApplicationContext(), zoneChangeEvent.toString(), Toast.LENGTH_SHORT).show();
 	   Toast.makeText(getApplicationContext(), "true for player",Toast.LENGTH_SHORT).show();
 	  
 	   
 	   if (mapView.getViewTreeObserver().isAlive()) 
 	   {
 		   mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
 			  public void onGlobalLayout() 
		        {
		        		        	
		        	mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		        	Display display = getWindowManager().getDefaultDisplay();
		        	Point size = new Point();
					    display.getSize(size);
					   while(true)
					   {
						   Point point;
						   int counter = 0;
						   Projection projection = map.getProjection();
						   if(zoneChangeEvent.getZoneType().equals(ZoneType.GAME_LOCATION))
						   {
							   for(Location location: gameLocationPoints)
							   {
								   point = projection.toScreenLocation(new LatLng(location.getLatitude(),location.getLongitude()));
								   System.out.println("Pixel value: " + point.x + " " + point.y);
								   System.out.println("Pixel value: " + size.x + " " + size.y);
								   if(point.x>0 && point.x<size.x && point.y>0 && point.y<size.y)
								   {
									   counter++;
									   System.out.println("counter :"+counter);
								   }
								   
							   }
							   if(counter == 4)
							   {
								   level+= 0.1;
								   System.out.println("level :"+ level);
								   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LocalMapPolygon().zoomCenterPoint(gameBoundaryPoints), level));
								   
							   }
							   else
							   {
								   break;
							   }
						   }
						   else if(zoneChangeEvent.getZoneType().equals(ZoneType.GAME_BOUNDARY))
						   {
							   for(Location location: gameBoundaryPoints)
							   {
								   point = projection.toScreenLocation(new LatLng(location.getLatitude(),location.getLongitude()));
								   System.out.println("Pixel value: " + point.x + " " + point.y);
								   System.out.println("Pixel value: " + size.x + " " + size.y);
								   if(point.x>0 && point.x<size.x && point.y>0 && point.y<size.y)
								   {
									   counter++;
									   System.out.println("counter :"+counter);
								   }
								   
							   }
							   if(counter < 4)
							   {
								   level-= 0.1;
								   System.out.println("level :"+ level);
								   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LocalMapPolygon().zoomCenterPoint(gameBoundaryPoints), level));
								   
							   }
							   else if (counter==4)
							   {
								   break;
							   }
						   }

	   		         }
		        	
		        	
		        }
		});
 		    
 	  
 	   
    }} 
		
	
	private float increaseZoomLevel(float number)
	{
		return number += 0.1;
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
	public synchronized void energyChangeCallBack(final double energyLevel) {

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

}
