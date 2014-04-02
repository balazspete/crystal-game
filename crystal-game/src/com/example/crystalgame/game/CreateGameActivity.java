/**
 * This activity is for creating a game
 * 
 * @author Chen Shen, Allen Thomas Varghese
 */
package com.example.crystalgame.game;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.groups.GroupLobbyActivity;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.library.instructions.InstructionFormatException;

/**
 * 
 * @author Allen Thomas Varghese, Chen Shen, Rajan Verma
 *
 */
public class CreateGameActivity extends Activity {
	private Zone gameBoundary = new Zone();
	Location myLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
	}
	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				Bundle extras = data.getExtras();
				this.gameBoundary = (Zone) extras.getSerializable(GameBoundaryActivity.LOCATIONS);
				android.location.Location location = (android.location.Location) extras.get(GameBoundaryActivity.MY_LOCATION);
				if (location != null) {
					myLocation = new Location(location.getLatitude(), location.getLongitude());
				}
			}
		}
	}
	
	/**
	 * Reset the values in the form
	 */
	private void resetFormValues() {
		// Clear the game duration
		((EditText) findViewById(R.id.game_name)).setText("");
		((EditText) findViewById(R.id.game_length)).setText("");
		gameBoundary.clear();
		Toast.makeText(this, getString(R.string.reset_game_info), Toast.LENGTH_SHORT).show();
	}
	
	public void createGame(View view) {
		if (gameBoundary.isEmpty()) {
			showErrorToast();
			return;
		}
		
		int gameTime = -1;
		try {
			gameTime = Integer.parseInt(((EditText) findViewById(R.id.game_length)).getText().toString());
		} catch (NumberFormatException e) {
			// handled in the next step
		}
		
		if (gameTime < 10) {
			gameTime = 10;
			Toast.makeText(this, "Invalid game length value", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (myLocation == null) {
			Toast.makeText(this, "No location! Open map to get it...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String name = ((EditText) findViewById(R.id.game_name)).getText().toString();
		
		Instruction instruction;
		try {
			instruction = GameInstruction.createCreateGameGameInstruction(
					name 															// Name of game
				,	gameBoundary.getLocation(0) 									// Game Location Top-Left
				,	gameBoundary.getLocation(1)  									// Game Location Top-Right
				,	gameBoundary.getLocation(2)  									// Game Location Bottom-Right
				,	gameBoundary.getLocation(3) 									// Game Location Bottom-Left
				,	myLocation														// Pass the location of the client who initiates create game
				,   gameTime
			);
		} catch (InstructionFormatException e) {
			showErrorToast();
			return;
		}
		
		// Saving the game time in seconds
		((CrystalGame) getApplication()).setTimeDuration(gameTime * 60);
		
		GameManager.getInstance().saveGameDuration(gameTime * 60);
		
		((CrystalGame) getApplication()).getCommunication().out.relayInstructionToServer(instruction);
		Intent intent = new Intent(this, GroupLobbyActivity.class);
		intent.putExtra(GroupLobbyActivity.KEY_LOAD_DW, false);
		startActivity(intent);
	}
	
	private void showErrorToast() {
		Toast.makeText(this, "Define the game location!", Toast.LENGTH_SHORT).show();
	}
	
	public void resetLocations(View view) {
		resetFormValues();
	}
	
	public void selectLocations(View view) {
		if(this.gameBoundary.isEmpty())
    	{
    		startActivityForResult(new Intent(getApplicationContext(), GameBoundaryActivity.class),1);
    	}
    	else
    	{
    		Intent intent = new Intent(getApplicationContext(), GameBoundaryActivity.class);
	    	intent.putExtra(GameBoundaryActivity.LOCATIONS, (Serializable) gameBoundary);
	    	startActivityForResult(intent,1);
    	}
	}
}
