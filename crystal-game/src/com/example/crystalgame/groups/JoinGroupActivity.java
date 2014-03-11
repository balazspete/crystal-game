package com.example.crystalgame.groups;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinGroupActivity extends Activity {
	
	public static final String 
		LAUNCH_INTENT_FIELD = "com.example.crystalgame.joingameactivity.launch_intent",
		LAUNCH_INTENT_CREATE = "CREATE",
		LAUNCH_INTENT_JOIN = "JOIN";
	
	private ProgressDialog pDialog;
	private String otherName, otherId;
	private InstructionEventListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);
        setupActionBar();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		String extra = this.getIntent().getStringExtra(LAUNCH_INTENT_FIELD);
		if (extra != null && extra.equals(LAUNCH_INTENT_CREATE)) {
			this.findViewById(R.id.button_get_player_alias).setVisibility(View.INVISIBLE);
			this.findViewById(R.id.button_join).setVisibility(View.INVISIBLE);
		} else {
			this.findViewById(R.id.group_name).setVisibility(View.INVISIBLE);
			this.findViewById(R.id.button_create).setVisibility(View.INVISIBLE);
		}
	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	public void selectPlayer(View view) {
		Intent intent = new Intent(getApplicationContext(), SelectPlayerActivity.class);
    	startActivityForResult(intent,1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			otherName = (String) extras.getSerializable(Player.NAME);
			otherId = extras.getString(Player.ID);
			if(otherName != null) {
				((Button) this.findViewById(R.id.button_get_player_alias)).setText(otherName);
			}
		}
	}
	
	public void joinGroup(View view) {
		String name = ((EditText) findViewById(R.id.player_name)).getText().toString();
		sendInstruction(GroupInstruction.joinGroup(name, otherId));
	}
	
	public void createGroup(View view) {
		String name = ((EditText) findViewById(R.id.player_name)).getText().toString();
		String groupName = ((EditText) findViewById(R.id.group_name)).getText().toString();
		sendInstruction(GroupInstruction.createGroup(groupName, 20, name, null, null, null, null));
	}
	
	private void sendInstruction(GroupInstruction instruction) {
		CrystalGame game = ((CrystalGame) getApplication());
		game.getCommunication().out.sendGroupInstructionToServer(instruction);
		listener = new InstructionEventListener() {
			@Override
			public void onGroupInstruction(InstructionEvent event) {
				GroupInstruction instruction = (GroupInstruction) event.getInstruction();
				switch(instruction.groupInstructionType) {
					case SUCCESS:
						if(instruction.arguments.length > 0) {
							goToGroupUI();
						}
						break;
					default:
						//ignored
				}
			}

			@Override
			public void onGroupStatusInstruction(InstructionEvent event) {}
			@Override
			public void onGameInstruction(InstructionEvent event) {}
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) {}
			@Override
			public void onDataTransferInstruction(InstructionEvent event) {}
		};
		game.getCommunication().in.addInstructionEventListener(listener);
	}
	
	private void goToGroupUI() {
		CrystalGame game = ((CrystalGame) getApplication());
		game.getCommunication().in.removeInstructionEventListener(listener);
		
		Intent intent = new Intent(this, GroupLobbyActivity.class);
		startActivity(intent);
	}
	
}
