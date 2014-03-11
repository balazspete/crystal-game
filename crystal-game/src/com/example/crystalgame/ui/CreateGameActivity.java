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
import android.widget.Toast;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.InstructionFormatException;

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
			
			// Sending instruction to server
			String name = ((EditText)findViewById(R.id.GameDuration)).getText().toString();
	    	try {
				appInstance.getCommunication().out.relayInstructionToServer(GameInstruction
		    			.createCreateGameGameInstruction(name, 
		    					gameBoundary.getLocation(0), 
		    					gameBoundary.getLocation(1), 
		    					gameBoundary.getLocation(2),
		    					gameBoundary.getLocation(3)));
			} catch (InstructionFormatException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			} catch (IndexOutOfBoundsException e) {
				Toast.makeText(this, "Game location requires 4 points", Toast.LENGTH_SHORT).show();
			}
		} else if(view.getId() == R.id.btnResetCreateGame) {
		    	resetFormValues();
		} else {
		    	
	    }	
		
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
				this.gameBoundary = (Zone) extras.getSerializable("locations");
			}
		}
	}
	
	/**
	 * Reset the values in the form
	 */
	private void resetFormValues() {
		// Clear the game duration
		((EditText)findViewById(R.id.GameDuration)).setText("");
	}
}
