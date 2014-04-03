package com.example.crystalgame.groups;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.game.GameBoundaryActivity;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;

/**
 * Creating a group functionality
 * @author Balazs Pete
 */
public class JoinGroupActivity extends Activity {
	private Zone gameBoundary = new Zone();
	public static final String 
		LAUNCH_INTENT_FIELD = "com.example.crystalgame.joingameactivity.launch_intent",
		LAUNCH_INTENT_CREATE = "CREATE",
		LAUNCH_INTENT_JOIN = "JOIN";
	
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
			this.findViewById(R.id.boundary_select).setVisibility(View.INVISIBLE);
		}
	}
	
    public void createBoundary(View view) {
    	if(this.gameBoundary.isEmpty())
    	{
    		startActivityForResult(new Intent(getApplicationContext(), GameBoundaryActivity.class),2);
    	}
    	else
    	{
    		Intent intent = new Intent(getApplicationContext(), GameBoundaryActivity.class);
	    	intent.putExtra("locations", (Serializable) gameBoundary);
	    	startActivityForResult(intent,2);
    	}
    }
    
    public void launchJoinGroupActivity(String extra) {
    	Intent intent = new Intent(this, JoinGroupActivity.class);
    	intent.putExtra(JoinGroupActivity.LAUNCH_INTENT_FIELD, extra);
    	startActivity(intent);
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
		if (requestCode == 2)
		{
			if(resultCode == RESULT_OK)
			{
				Bundle extras = data.getExtras();
				this.gameBoundary = (Zone) extras.getSerializable(GameBoundaryActivity.LOCATIONS);
			}
		}
	}
	
	public void joinGroup(View view) {
		String name = ((EditText) findViewById(R.id.player_name)).getText().toString();
		CrystalGame.setMyName(name);
		sendInstruction(GroupInstruction.joinGroup(name, otherId));
	}
	
	public void createGroup(View view) {
		String name = ((EditText) findViewById(R.id.player_name)).getText().toString();
		CrystalGame.setMyName(name);
		String groupName = ((EditText) findViewById(R.id.group_name)).getText().toString();
		
		if(gameBoundary!=null && name !=null && groupName!=null) {
			sendInstruction(GroupInstruction.createGroup(groupName, 20, name, gameBoundary.getLocation(0), gameBoundary.getLocation(1), gameBoundary.getLocation(2), gameBoundary.getLocation(3)));	
		} else {
			Toast.makeText(this, "Provide full info", Toast.LENGTH_SHORT).show();
		}
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
			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) {}
			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) {}
		};
		game.getCommunication().in.addInstructionEventListener(listener);
	}
	
	private void goToGroupUI() {
		CrystalGame game = ((CrystalGame) getApplication());
		game.getCommunication().in.removeInstructionEventListener(listener);
		
		Intent intent = new Intent(this, GroupLobbyActivity.class);
		intent.putExtra(GroupLobbyActivity.KEY_LOAD_DW, false);
		startActivity(intent);
	}

}
