package com.example.crystalgame;


import com.example.crystalgame.communication.CommunicationService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private Intent communicationIntent;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	protected void onStart() {
		super.onStart();

		Log.d("CommunicationService", "oncreate");
		communicationIntent = createCommunictionIntent();
		getApplicationContext().startService(communicationIntent);
	}
    
    //protected void onRestart();

    //protected void onResume();

    //protected void onPause();

    protected void onStop() {
    	getApplicationContext().stopService(communicationIntent);
    }

    //protected void onDestroy();
	
    
    private Intent createCommunictionIntent() {
    	return new Intent(getBaseContext(), CommunicationService.class);
    }
}
