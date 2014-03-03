package com.example.crystalgame.ui;
import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GameMenuActivity extends Activity implements OnClickListener
{
	private Button btnCreateGame;
	private Button btnCreateGroup;
	private Button btnJoinGame;
	private CrystalGame appInstance;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        this.appInstance = (CrystalGame)getApplication();
        
		this.btnCreateGame 		= (Button) findViewById(R.id.btnCreateGame);
        this.btnJoinGame		= (Button) findViewById(R.id.btnJoinGame);
        this.btnCreateGroup		= (Button) findViewById(R.id.btnCreateGroup);
        
        if(this.appInstance.getGroupID() == null || this.appInstance.getGroupID().isEmpty())
		{
        	this.btnCreateGame.setEnabled(false);
		}
        else
        {
        	this.btnCreateGame.setEnabled(true);
        }
        
        this.btnCreateGame.setOnClickListener(this);
        this.btnCreateGroup.setOnClickListener(this);
        this.btnJoinGame.setOnClickListener(this);
	}
	
	protected void onStart() 
	{
		super.onStart();

	}

    protected void onPause() 
    {
    	super.onStop();
    }

	@Override
	public void onClick(View view) 
	{
		if(view.getId() == R.id.btnCreateGame) {
		    	startActivity(new Intent(getApplicationContext(), CreateGameActivity.class));
		} else if(view.getId() == R.id.btnCreateGroup) {		    	
		    	startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
		} else if(view.getId() == R.id.btnJoinGame) {
			
		} else {	
		}	
	}

    /*protected void onStop() {
    	super.onStop();
    }*/
}
