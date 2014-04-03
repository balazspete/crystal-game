package com.example.crystalgame.game;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.crystalgame.R;
import com.example.crystalgame.game.energy.EnergyManager;
import com.example.crystalgame.groups.GroupLobbyActivity;
import com.example.crystalgame.ui.UIController;

/**
 * End-of-game screen which is displayed for the following conditions :
 * 1. Out of time
 * 2. Out of energy
 * 3. Out of boundary
 * 
 * @author Balazs Pete, Allen Thomas Varghese
 */
public class GameEndActivity extends Activity {

	public static final String
		GAME_END_TYPE = "com.example.crystalgame.game.game_end_type";
	
	public static enum GameEndType implements Serializable {
		OUT_OF_TIME, 
		OUT_OF_ENERGY,
		OUT_OF_BOUNDARY
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_end);
		
		GameEndType endType = (GameEndType) getIntent().getSerializableExtra(GAME_END_TYPE);
		
		EnergyManager.getInstance().stopEnergyManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_end, menu);
		return true;
	}
	
	public synchronized void goToLobby(View view) {
		startActivity(new Intent(this, GroupLobbyActivity.class));
	}

}
