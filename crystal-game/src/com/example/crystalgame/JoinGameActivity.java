package com.example.crystalgame;

import java.io.Serializable;

import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.ui.GameBoundaryActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoinGameActivity extends Activity implements OnClickListener
{
	private ProgressDialog pDialog;
	String Name = null;
	private Button btnPlayers;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        this.btnPlayers = (Button) findViewById(R.id.btnPlayers);
        this.btnPlayers.setOnClickListener(this);

	}
	@Override
	public void onClick(View view) 
	{
		switch(view.getId())
		{
		    case R.id.btnPlayers:
	    		Intent intent = new Intent(getApplicationContext(), SelectPlayersActivity.class);
		    	startActivityForResult(intent,1);
		    	break;
		    case R.id.btnJoin:
		    	
		    	break;
		    default:
		    	break;
		    	
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
				this.Name = (String) extras.getSerializable("name");
				if(this.Name != null)
				{
					this.btnPlayers.setText(this.Name);
				}
			}
		}
	}
	
	class JoinGame extends AsyncTask<String, String, String> 
	{

		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(JoinGameActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}


		protected String doInBackground(String... args) 
		{
			return null;
			
		}

		
		protected void onPostExecute(String file_url) 
		{
			
		}

	}
	
}
