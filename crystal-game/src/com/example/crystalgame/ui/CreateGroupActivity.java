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

import com.example.crystalgame.R;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Zone;

public class CreateGroupActivity extends Activity implements OnClickListener
{
	private Button btnGameBoundary;
	private Button btnSubmit;
	private Button btnReset;
	private Zone gameBoundary = new Zone();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        
		this.btnGameBoundary 		= (Button) findViewById(R.id.btnGameBoundary);
        this.btnSubmit				= (Button) findViewById(R.id.btnSubmitCreateGroup);
        this.btnReset				= (Button) findViewById(R.id.btnResetCreateGroup);
        
        this.btnGameBoundary.setOnClickListener(this);
        this.btnSubmit.setOnClickListener(this);
        this.btnReset.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) 
	{
		// TODO Auto-generated method stub
		if(view.getId() == R.id.btnGameBoundary)
		{
		    	if(this.gameBoundary.isEmpty())
		    	{
		    		startActivityForResult(new Intent(getApplicationContext(), GameBoundaryActivity.class),1);
		    		//Toast.makeText(this, "Null", Toast.LENGTH_LONG).show();
		    	}
		    	else
		    	{
		    		Intent intent = new Intent(getApplicationContext(), GameBoundaryActivity.class);
			    	intent.putExtra("locations", (Serializable)gameBoundary);
			    	startActivityForResult(intent,1);
		    	}
		} else if(view.getId() == R.id.btnSubmitCreateGame) {
				ClientManager.getInstance().saveGameBoundary(new GameBoundary(gameBoundary.getLocationList()));
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
//				Toast.makeText(this, "You have chosen" + this.gameBoundary.getLocations().toString(), Toast.LENGTH_LONG).show();
			}
			if (resultCode == RESULT_CANCELED) 
			{    
		    
			}
		}
	}
	
	/**
	 * Reset the values in the form
	 */
	private void resetFormValues() 
	{
		// Clear the number of participants
		((EditText)findViewById(R.id.GameParticipants)).setText("");
		((EditText)findViewById(R.id.GroupName)).setText("");
	}
}
