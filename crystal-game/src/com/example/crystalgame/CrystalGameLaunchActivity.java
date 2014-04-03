package com.example.crystalgame;

import com.example.crystalgame.groups.JoinGroupActivity;
import com.example.crystalgame.library.instructions.GroupInstruction;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class CrystalGameLaunchActivity extends Activity {

	private static CrystalGameLaunchActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		setContentView(R.layout.activity_launch);
	}
	
	protected void onResume() {
		super.onResume();
		
		if (CrystalGame.getClientID() != null && CrystalGame.getGroupID() != null) {
			tryToRejoinGroup();
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
    
    private void tryToRejoinGroup() {
    	((CrystalGame) getApplication()).getCommunication().out.sendGroupInstructionToServer(
    			GroupInstruction.createIsMemberQueryInstruction(CrystalGame.getGroupID()));
    	
    	Toast.makeText(this, "Trying to rejoin the previous group...", Toast.LENGTH_SHORT).show();
    	
    	findViewById(R.id.button_create_group).setEnabled(false);
    	findViewById(R.id.button_join_group).setEnabled(false);
    }
    
    public void tryToRejoinGroupFailed() {
    	final Activity a = this;
    	runOnUiThread(new Runnable(){
			@Override
			public void run() {
		    	Toast.makeText(a, "Failed to rejoin the previous group...", Toast.LENGTH_SHORT).show();
		    	
		    	findViewById(R.id.button_create_group).setEnabled(true);
		    	findViewById(R.id.button_join_group).setEnabled(true);
			}
		});
    }
    
    public static CrystalGameLaunchActivity getInstance() {
    	return instance;
    }
    
}
