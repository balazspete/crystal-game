package com.example.crystalgame.ui;

import com.example.crystalgame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.app.Activity; 
import android.content.Context; 
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;  
import android.view.View;  
import android.view.Window;  
import android.widget.TextView;  
  
public class StartGameActivity extends Activity 
{  
	
    /** Called when the activity is first created. */  
	private GoogleMap map;
    private TextView Energy_Label; 
    private TextView Crystal_Label; 
    private TextView Magic_Label; 
    
    private LocationManager locationManager; 
    private String locationFind;
    
    public void onCreate(Bundle savedInstanceState) 
    {  
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_start_game);  
        
        Crystal_Label=(TextView) findViewById(R.id.Crystal_message);  
        Crystal_Label.setVisibility(View.VISIBLE);  
        Crystal_Label.setText("5");  
        
        Energy_Label=(TextView) findViewById(R.id.Energy_message);  
        Energy_Label.setVisibility(View.VISIBLE);  
        Energy_Label.setText("5");  

        Magic_Label=(TextView) findViewById(R.id.Magic_message);  
        Magic_Label.setVisibility(View.VISIBLE);  
        Magic_Label.setText("10");
        
        this.map = ((MapFragment) getFragmentManager().findFragmentById(R.id.GameMap)).getMap();
        this.map.setMyLocationEnabled(true);
        
        /*location manager class to get current location latitude and longitude values*/
        this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
        Criteria criteria = new Criteria();
        
        /*Returns the name of the provider that best matches the given criteria*/
        this.locationFind = this.locationManager.getBestProvider(criteria, false);
        
        android.location.Location location = locationManager.getLastKnownLocation(locationFind);
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 17));
    }
        
      
     
}  