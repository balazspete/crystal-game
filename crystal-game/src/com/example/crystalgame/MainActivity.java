package com.example.crystalgame;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

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
	
	public void createGroup(View view) {
		sendTestMessage(0);
	}
	
	public void joinGroup(View view) {
		sendTestMessage(1);
	}
	
	public void leaveGroup(View view) {
		sendTestMessage(2);
	}
	
    // TODO: temporary solution - remove
    public void sendTestMessage(int option) {
    	String text1 = ((EditText) findViewById(R.id.editText1)).getText().toString();
    	String text2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
    	String text3 = ((EditText) findViewById(R.id.editText2)).getText().toString();

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
    	
    	if(option == 0) {
    		out.sendGroupInstructionToServer(GroupInstruction.createGroup(text1, 20, text2));
    	} else if (option == 1) {
    		out.sendGroupInstructionToServer(GroupInstruction.joinGroup(text1, text2));
    	} else if (option == 2) {
    		out.sendGroupInstructionToServer(GroupInstruction.leaveGroup());
    	}
    	
    }
    
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }
    
}
