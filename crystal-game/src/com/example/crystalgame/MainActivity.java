package com.example.crystalgame;

import java.util.Arrays;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;
import com.example.crystalgame.ui.GameMenuActivity;

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
		
		//setContentView(R.layout.activity_gamemenu);
		/* Calling GPS activity*/
		Intent intent = new Intent(getApplicationContext(), GameMenuActivity.class);
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
	
	public void sendUnicast(View view) {
		sendTestMessage(3);
	}
	
	public void sendMulticast(View view) {
		sendTestMessage(4);
	}
	
	public void getMembers(View view) {
		sendTestMessage(5);
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
				public void onMessageEvent(MessageEvent event) {
					System.out.println("Message: " + event.getMessage().getData());
				}

				@Override
				public void onGroupStatusMessageEvent(MessageEvent event) {
					System.out.print("GroupStatusMessage: ");
					System.out.println(Arrays.toString(((GroupStatusInstruction) event.getMessage().getData()).arguments));
				}

				@Override
				public void onControlMessage(MessageEvent event) {
					System.out.println("ControlMessage: " + event.getMessage().getData());
				}

				@Override
				public void onInstructionRelayMessage(MessageEvent event) {
					System.out.println("Instruction relay");
				}
	    	});
    	}
    	
    	if(option == 0) {
    		out.sendGroupInstructionToServer(GroupInstruction.createGroup(text1, 20, text2));
    	} else if (option == 1) {
    		out.sendGroupInstructionToServer(GroupInstruction.joinGroup(text1, text2));
    	} else if (option == 2) {
    		out.sendGroupInstructionToServer(GroupInstruction.leaveGroup());
    	} else if (option == 3) {
    		out.sendTestUnicastData(text2, text1);
    	} else if (option == 4) {
    		out.sendTestMulticastData(text2);
    	} else if (option == 5) {
    		out.sendGroupStatusInstruction(GroupStatusInstruction.createGroupMembershipListRequestIntruction());
    	}
    	
    }
    
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }
    
}
