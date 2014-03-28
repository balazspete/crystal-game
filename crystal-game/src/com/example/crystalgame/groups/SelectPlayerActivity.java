package com.example.crystalgame.groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.R.id;
import com.example.crystalgame.R.layout;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupInstruction.GroupInstructionType;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SelectPlayerActivity extends Activity implements OnClickListener {

	private ListView listView;
	private PlayerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_player);

		listView = (ListView) findViewById(R.id.PlayersList);
		adapter = new PlayerListAdapter(this);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) 
			{
		    	Intent intent = new Intent();
		    	Player player = adapter.getItem(arg2);
		    	intent.putExtra(Player.ID, player.id);
		    	intent.putExtra(Player.NAME, player.name);
		    	
		    	setResult(RESULT_OK, intent);
		    	finish();
			}
		});
		
		((CrystalGame) getApplication()).getCommunication().in.addInstructionEventListener(new InstructionEventListener() {
			@Override
			public void onGroupInstruction(InstructionEvent event) {
				final GroupInstruction instruction = (GroupInstruction) event.getInstruction();
				if (instruction.groupInstructionType == GroupInstructionType.MEMBER_LIST_RESPONSE) {
					if (instruction.arguments.length > 0) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.refreshData((String[]) instruction.arguments);
							}
						});
						
					}
				}
			}

			@Override
			public void onGroupStatusInstruction(InstructionEvent event) { }
			
			@Override
			public void onGameInstruction(InstructionEvent event) { }
			
			@Override
			public void onDataSynchronisationInstruction(InstructionEvent event) { }

			@Override
			public void onDataTransferInstruction(InstructionEvent event) { }

			@Override
			public void onCommunicationStatusInstruction(InstructionEvent event) { }

			@Override
			public void onCharacterInteractionInstruction(InstructionEvent event) { }
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		((CrystalGame) getApplication()).getCommunication().out
			.sendGroupInstructionToServer(GroupInstruction.createMembershipListRequestInstruction());	
	}
	
	@Override
	public void onClick(View view) 
	{
		switch(view.getId())
		{
		    case R.id.button_get_player_alias:
		    	
		    	break;
		    case R.id.button_join:
		    	
		    	break;
		    default:
		    	break;    	
	    }		
	}
}
