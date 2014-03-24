package com.example.crystalgame.game;

import java.io.Serializable;

import com.example.crystalgame.R;
import com.example.crystalgame.R.layout;
import com.example.crystalgame.R.menu;
import com.example.crystalgame.groups.GroupLobbyActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_end, menu);
		return true;
	}
	
	public void goToLobby(View view) {
		startActivity(new Intent(this, GroupLobbyActivity.class));
	}

}
