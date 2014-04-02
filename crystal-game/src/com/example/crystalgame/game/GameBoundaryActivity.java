package com.example.crystalgame.game;

import java.io.Serializable;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.crystalgame.R;
import com.example.crystalgame.SettingsActivity;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity for defining a boundary
 * @author Chen Shen, Allen Thomas Varghese
 * @version 1.0
 */
public class GameBoundaryActivity extends FragmentActivity implements LocationListener, OnMapClickListener, OnMarkerDragListener
{
	public static final String
		LOCATIONS = "com.exaple.xrystalgame.game.Game_boundary_activity.locations",
		MY_LOCATION = "com.exaple.xrystalgame.game.Game_boundary_activity.my_location";
	
	private GoogleMap map;
	private Zone gameBoundary = new Zone();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_boundary);

        this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.GameBoundaryMap)).getMap();
        this.map.setMyLocationEnabled(true);
        
        this.map.setOnMapClickListener(this);
		
        this.map.setOnMarkerDragListener(this);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_game_boundary, menu);
		return true;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if(getIntent() != null) {
			Bundle extras = getIntent().getExtras();
			if(extras !=null) {
				this.gameBoundary = (Zone) extras.getSerializable(LOCATIONS);
				map.clear();
				
				for(int i = 0; i< gameBoundary.getLength(); i++) { 
					Location l = gameBoundary.getLocation(i);
					LatLng position = new LatLng(l.getLatitude(), l.getLongitude());
					Marker m = map.addMarker(new MarkerOptions()
						.position(position)
						.draggable(true)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
					l.setMarkerID(m.getId());
				}
			}
		}
	}
	
	@Override
	public void onLocationChanged(android.location.Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }

	@Override
	public void onMapClick(LatLng position) 
	{
		Marker marker = map.addMarker(new MarkerOptions()
			.position(position)
			.draggable(true)
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		Location location = new Location(position.latitude, position.longitude);
		location.setMarkerID(marker.getId());
		
		if (!gameBoundary.addLocation(location)) {
			marker.remove();
		}
		
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker markerID) {
		Location location = gameBoundary.getLocation(markerID.getId());
		location.setMarkerID(markerID.getId());
		LatLng pos = markerID.getPosition();
		location.setLatitude(pos.latitude);
		location.setLongitude(pos.longitude);
	}

	@Override
	public void onMarkerDragStart(Marker markerID) {
		// TODO Auto-generated method stub
		
	}

	public void saveBoundary(MenuItem item) {
		Intent intent = new Intent();
    	intent.putExtra(LOCATIONS, gameBoundary);
    	intent.putExtra(MY_LOCATION, map.getMyLocation());
    	setResult(RESULT_OK, intent);
    	finish();
	}
	
}
