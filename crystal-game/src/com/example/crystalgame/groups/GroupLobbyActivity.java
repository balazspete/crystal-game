package com.example.crystalgame.groups;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.library.instructions.GameInstruction;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GroupLobbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_lobby);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_lobby, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	public void requestGameStart(View view) {
		((CrystalGame) getApplication()).getCommunication().
			out.relayInstructionToServer(GameInstruction.createStartGameRequestGameInstruction());
	}
	
	public void setTitle() {
		//ClientDataWarehouse.getInstance().get()
	}
	
}
