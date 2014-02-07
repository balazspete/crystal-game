package com.example.crystalgame;

import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.communication.CommunicationService;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.MessageEventListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
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
		
		/*Calling GPS activity*/
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
    	EditText text = (EditText) findViewById(R.id.editText1);

    	if (in == null) {
	    	CrystalGame app = (CrystalGame) getApplication();
	    	out = (ClientOutgoingMessages) app.getCommunication().out;
	    	in = app.getCommunication().in;
	    	in.addMessageEventListener(new MessageEventListener(){
				@Override
				public void messageEvent(Message message) {
					Log.i("ServerMessage", (String) message.getData());
				}
	    	});
    	}
    	
    	out.sendTestDataToServer(text.getText().toString());
    }
    
    private Intent createCommunictionIntent() {
    	return new Intent(getBaseContext(), CommunicationService.class);
    }
}
