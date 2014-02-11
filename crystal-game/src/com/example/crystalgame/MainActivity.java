package com.example.crystalgame;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.communication.CommunicationService;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Intent communicationIntent;
	private ClientOutgoingMessages out;
	private IncomingMessages in;
	
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
		
		/* Calling GPS activity*/
		Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
		startActivity(intent);
	}
	
	protected void onStart() {
		super.onStart();

		Log.d("CommunicationService", "oncreate");
		communicationIntent = createCommunictionIntent();
		getApplicationContext().startService(communicationIntent);
	}
    
	/*@Override
    protected void onRestart(){
    	super.onRestart();
    }*/

    //protected void onResume();

    protected void onPause() {
    	super.onStop();
    	getApplicationContext().stopService(communicationIntent);
    }

    /*protected void onStop() {
    	super.onStop();
    }*/

    //protected void onDestroy();
	
    // TODO: temporary solution - remove
    public void sendTestMessage(View view) {
    	String text1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
    	String text2 = ((EditText) findViewById(R.id.editText2)).getText().toString();

    	if (in == null) {
	    	ClientCommunication communication = 
	    			((CrystalGame) getApplication()).getCommunication();
	    	out = communication.out;
	    	in = communication.in;
	    	in.addMessageEventListener(new MessageEventListener(){
				@Override
				public void messageEvent(Message message) {
					System.out.println(message.getData());
				}
	    	});
    	}
    	
    	//out.sendTestDataToServer(text.getText().toString());
    	
    	out.sendGroupInstructionToServer(GroupInstruction.createGroup(text1, 20, text2));
    }
    
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }
    
    private Intent createCommunictionIntent() {
    	return new Intent(getBaseContext(), CommunicationService.class);
    }
    
    
}
