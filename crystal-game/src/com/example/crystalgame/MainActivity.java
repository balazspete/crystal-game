package com.example.crystalgame;

import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.library.communication.incoming.IncomingMessages;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
//		Intent intent = new Intent(getApplicationContext(), GameMenuActivity.class);
//		startActivity(intent);
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
	
	public void requestGameStart(View view) {
		sendTestMessage(6);
	}
	
	public void requestAllMembers(View view) {
		sendTestMessage(7);
	}
	
    // TODO: temporary solution - remove
    public void sendTestMessage(int option) {
    	String text1 = ((EditText) findViewById(R.id.group_name)).getText().toString();
    	String text2 = ((EditText) findViewById(R.id.editText2)).getText().toString();
    	String text3 = ((EditText) findViewById(R.id.editText2)).getText().toString();

    	if (in == null) {
	    	ClientCommunication communication = 
	    			((CrystalGame) getApplication()).getCommunication();
	    	out = communication.out;
	    	in = communication.in;
    	}
    	
    	if(option == 0) {
    		out.sendGroupInstructionToServer(GroupInstruction.createGroup(text1, 20, text2, null, null, null, null));
    	} else if (option == 1) {
    		out.sendGroupInstructionToServer(GroupInstruction.joinGroup(text1, text2));
    	} else if (option == 2) {
    		out.sendGroupInstructionToServer(GroupInstruction.leaveGroup());
    	} else if (option == 3) {
    		out.sendTestUnicastData(text2, text3);
    	} else if (option == 4) {
    		out.sendTestMulticastData(text3);
    	} else if (option == 5) {
    		//out.sendGroupStatusInstruction(GroupStatusInstruction.createGroupMembershipListRequestIntruction());
    		out.sendGroupInstructionToServer(GroupInstruction.createMembershipListRequestInstruction());
    	} else if (option == 6) {
    		out.relayInstructionToServer(GameInstruction.createStartGameRequestGameInstruction());
    	} else if (option == 7) {
     		//Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT ).show();
     	}
    	
    }
    
    public boolean showSettings(MenuItem item) {
    	startActivity(new Intent(this, SettingsActivity.class));
    	return true;
    }
    
}
