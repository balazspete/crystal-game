/**
 * This activity is for creating a game
 * 
 * @author Chen Shen, Allen Thomas Varghese
 */
package com.example.crystalgame.ui;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Zone;

/**
 * 
 * @author Allen Thomas Varghese, Chen Shen, Rajan Verma
 *
 */
public class CreateGameActivity extends Activity implements OnClickListener
{
	private Button btnGameLocation;
	private Button btnSubmit;
	private Button btnReset;
	private CrystalGame appInstance;
	private Zone gameBoundary = new Zone();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
		this.btnGameLocation 		= (Button) findViewById(R.id.btnGameLocation);
        this.btnSubmit				= (Button) findViewById(R.id.btnSubmitCreateGame);
        this.btnReset			    = (Button) findViewById(R.id.btnResetCreateGame);
        this.btnGameLocation.setOnClickListener(this);
        this.btnSubmit.setOnClickListener(this);
        this.btnReset.setOnClickListener(this);
        appInstance = (CrystalGame)getApplication();
        /**
         * Get Player ID and Group ID from the Application object
         */
        if(null != appInstance)
		{
			((EditText)findViewById(R.id.PlayerID)).setText(appInstance.getPlayerID());
			((EditText)findViewById(R.id.GroupID)).setText(appInstance.getGroupID());
		}
	}
	
	@Override
	public void onClick(View view) 
	{
		if(view.getId() == R.id.btnGameLocation)
		{
		    	if(this.gameBoundary.isEmpty())
		    	{
		    		startActivityForResult(new Intent(getApplicationContext(), GameBoundaryActivity.class),1);
		    	}
		    	else
		    	{
		    		Intent intent = new Intent(getApplicationContext(), GameBoundaryActivity.class);
			    	intent.putExtra("locations", (Serializable)gameBoundary);
			    	startActivityForResult(intent,1);
		    	}
		} else if(view.getId() == R.id.btnSubmitCreateGame) {
			ClientManager.getInstance().saveGameLocation(new GameBoundary(gameBoundary.getLocationList()));
		} else if(view.getId() == R.id.btnResetCreateGame) {
		    	resetFormValues();
		} else {
		    	
	    }	
		
	}
	

	
	/**
	 * Reset the values in the form
	 */
	private void resetFormValues() {
		// Clear the game start time
		((EditText)findViewById(R.id.GameStartTime)).setText("");
		// Clear the game duration
		((EditText)findViewById(R.id.GameDuration)).setText("");
		
		// Get the Player ID and Group ID again from the application object
		// These may change when communication fails
		if(null != appInstance)
		{
			((EditText)findViewById(R.id.PlayerID)).setText(appInstance.getPlayerID());
			((EditText)findViewById(R.id.GroupID)).setText(appInstance.getGroupID());
		}
	}
}
