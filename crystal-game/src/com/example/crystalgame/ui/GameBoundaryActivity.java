package com.example.crystalgame.ui;

import java.io.Serializable;

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

import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Activity for defining a boundary
 * @author Chen Shen, Allen Thomas Varghese
 * @version 1.0
 */
public class GameBoundaryActivity extends FragmentActivity implements LocationListener, OnClickListener,OnMapClickListener, OnMarkerDragListener
{
	private LocationManager locationManager;
	private String locationFind;
	private Button btnSavePoints;
	private GoogleMap map;
	private Zone gameBoundary = new Zone();
	//private ArrayList<LatLng> boundaryPoints = new ArrayList<LatLng>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_boundary);

		
		this.btnSavePoints = (Button) findViewById(R.id.btnSavePoints);
		this.btnSavePoints.setOnClickListener(this);

        this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.GameBoundaryMap)).getMap();
        this.map.setMyLocationEnabled(true);
        
        /*location manager class to get current location latitude and longitude values*/
        this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
        Criteria criteria = new Criteria();
        
        /*Returns the name of the provider that best matches the given criteria*/
        this.locationFind = this.locationManager.getBestProvider(criteria, false);
        
        android.location.Location location = locationManager.getLastKnownLocation(locationFind);
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 17));
        this.map.setOnMapClickListener(this);
		
        this.map.setOnMarkerDragListener(this);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps, menu);
		return true;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if(getIntent() != null) {
			Bundle extras = getIntent().getExtras();
			if(extras !=null) {
				this.gameBoundary = (Zone) extras.getSerializable("locations");
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
	public void onClick(View view) 
	{
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		    case R.id.btnSavePoints:
		    	//Toast.makeText(this, "You have chosen" + boundaryPoints.toString(), Toast.LENGTH_LONG).show();
		    	Intent intent = new Intent();
		    	intent.putExtra("locations", (Serializable)gameBoundary);
		    	setResult(RESULT_OK, intent);
		    	finish();
		    	break;
		    default:
		    	break;
	    }	
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

}