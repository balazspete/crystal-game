package com.example.crystalgame;

import com.example.crystalgame.groups.GroupLobbyActivity;
import com.example.crystalgame.groups.JoinGroupActivity;
import com.example.crystalgame.ui.GPSTracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CrystalGameLaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		startService(new Intent(this, GPSTracker.class));
	}
	
	protected void onResume() {
		super.onResume();
		
		if (((CrystalGame) getApplication()).getGroupID() != null) {
			startActivity(new Intent(this, GroupLobbyActivity.class));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }

    public void createGroup(View view) {
    	launchJoinGroupActivity(JoinGroupActivity.LAUNCH_INTENT_CREATE);
    }
    
    public void joinGroup(View view) {
    	launchJoinGroupActivity(JoinGroupActivity.LAUNCH_INTENT_JOIN);
    }
    
    public void launchJoinGroupActivity(String extra) {
    	Intent intent = new Intent(this, JoinGroupActivity.class);
    	intent.putExtra(JoinGroupActivity.LAUNCH_INTENT_FIELD, extra);
    	startActivity(intent);
    }
    
}
