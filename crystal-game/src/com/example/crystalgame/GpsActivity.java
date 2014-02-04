package com.example.crystalgame;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

/**
 * @author Rajan Verma, Allen Thomas Varghese
 * @version 1.0
 */
public class GpsActivity extends FragmentActivity implements LocationListener{
	
	LocationManager locationManager;
	String locationFind;
	LocalMapPolygon localMapPolygon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		/*Get the map instance*/
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        /*Set my location true on map*/
       map.setMyLocationEnabled(true);
       
       /*location manager class to get current location latitude and longitude values*/
       locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
       Criteria criteria = new Criteria();
       
       /*Returns the name of the provider that best matches the given criteria*/
       locationFind = locationManager.getBestProvider(criteria, false);
       
       Location location = locationManager.getLastKnownLocation(locationFind);
       if(location == null)
       {
    	   Toast.makeText(getApplicationContext(),"Could not found current Location",Toast.LENGTH_LONG ).show();
       }
       
       //get latitude and longitude values
       double latitude = location.getLatitude();
       double longitude = location.getLongitude();
       String display = "My Location is : Latitude + Longitude -> "+latitude+ " "+longitude;
       
       /*display on the map*/
       //Toast.makeText(getApplicationContext(),display,Toast.LENGTH_LONG ).show();
       
       LocationEvent place1 = new LocationEvent(53.3436688, -6.247169);
       LocationEvent place2 = new LocationEvent(53.347067, -6.250805);
       LocationEvent place3 = new LocationEvent(53.3446688, -6.264616);
      
       
       // Add three dummy locations that are nearby
       map.addMarker(new MarkerOptions()
	       .title("Place1")
	       .snippet("Player1")
	       .position(new LatLng(place1.getLattitudePosition(), place1.getLongitudePosition()))
	   );
       
       map.addMarker(new MarkerOptions()
       .title("Place2")
       .snippet("Player2")
       .position(new LatLng(place2.getLattitudePosition(), place2.getLongitudePosition()))
       );
   
       map.addMarker(new MarkerOptions()
       .title("Place3")
       .snippet("Player3")
       .position(new LatLng(place3.getLattitudePosition(), place3.getLongitudePosition()))
       );
       
       localMapPolygon = new LocalMapPolygon();
       map = localMapPolygon.createPolygon(map);
       
       Boolean boolean1 = localMapPolygon.playerPositionLocalMap(place3.getLattitudePosition(),place3.getLongitudePosition());
       if(boolean1 == true)
       {
    	   Toast.makeText(getApplicationContext(), "true for player",Toast.LENGTH_SHORT).show();
    	   LatLng latLng = localMapPolygon.zoomCenterPoint();
    	   map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    	   
       }
       /*zoom Camera*/
       //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place1.getLattitudePosition(),place1.getLongitudePosition()), 13));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location arg0) {
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

}
