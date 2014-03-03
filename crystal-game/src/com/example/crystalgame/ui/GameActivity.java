/**
 * 
 */
package com.example.crystalgame.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.crystalgame.R;
import com.example.crystalgame.library.data.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class GameActivity extends Activity implements GameUIComponentCommunicationListener, UIControllerHelperInter {

	GoogleMap map;
	public GameActivity() {	
	}

	// Initialize all the components in the client side
	private void initializeGameComponents() {
		// Enabling different components
		UIController.getInstance().startComponents();
		
		// Passing a refrerence to the UIController
		UIController.getInstance().setCurrentActivity(this);
				
		// Starting the location tracking service
		startService(new Intent(this,GPSTracker.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM);

		initializeGameComponents();
		
		/*Get the map instance*/
         map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        /*Set my location true on map*/
        map.setMyLocationEnabled(true);
        //Toast.makeText(getBaseContext(), GameManager.getInstance().getData(),Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
        LocalMapInformation gameObjectList = (LocalMapInformation)UIController.getInstance().loadGamePlayData(GamePlayState.LOCAL_MAP);
        
        // Displaying Crystals
        for(Location location : gameObjectList.getCrystalList()) {
	        map.addMarker(new MarkerOptions()
	        	.position(new LatLng(location.getLatitude(),location.getLongitude()))
	        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
	        );
        }
        
        // Displaying Magical Items
        for(Location location2 : gameObjectList.getMagicalItemList()){
	        map.addMarker(new MarkerOptions()
		    	.position(new LatLng(location2.getLatitude(),location2.getLongitude()))
		    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
	        );
        }
        
        // Displaying Characters
        for(Location location3 : gameObjectList.getCharacterList()){
	        map.addMarker(new MarkerOptions()
		    	.position(new LatLng(location3.getLatitude(),location3.getLongitude()))
		    	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
	        ); 
        }
		
	}
    
	@Override
	public void updateGameMagicalItemInfo(int noOfMagicalItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGameEnergy(int noOfEnergyUnits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoneChanged(ZoneChangeEvent zoneChangeEvent) {
		Toast.makeText(getApplicationContext(), zoneChangeEvent.toString(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void updateGameCrystalInfo(int noOfCrystals) {
		// TODO Auto-generated method stub
		
	}
}
