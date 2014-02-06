package com.example.crystalgame;

import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.communication.CommunicationService;
import com.example.crystalgame.library.communication.messages.TestMessage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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
    	super.onStop();
    	getApplicationContext().stopService(communicationIntent);
    }

    //protected void onDestroy();
	
    public void sendTestMessage(View view) {
    	CrystalGame app = (CrystalGame) getApplication();
    	ClientOutgoingMessages messages = (ClientOutgoingMessages) app.getCommunication().out;
    	TestMessage message = new TestMessage();
    	message.setData("test message");
    	messages.sendDataToServer("message");
    }
    
    private Intent createCommunictionIntent() {
    	return new Intent(getBaseContext(), CommunicationService.class);
    }
}
