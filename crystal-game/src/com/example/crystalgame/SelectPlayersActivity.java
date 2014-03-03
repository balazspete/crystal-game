package com.example.crystalgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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

public class SelectPlayersActivity extends Activity implements OnClickListener
{
	private static String NameKey = "Name";
	private ListView listView;
	public ArrayList<HashMap<String, String>> playerList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> player = new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_players);
		player.put(NameKey, "dasdasds");

		listView = (ListView) findViewById(R.id.PlayersList);
		playerList = new ArrayList<HashMap<String, String>>();
		playerList.add(player);
		try
        {
			ListAdapter playerListadapter = new SimpleAdapter(SelectPlayersActivity.this, playerList,R.layout.player_list_item, new String[]{NameKey}, new int[] {R.id.Player_Label});
			listView.setAdapter(playerListadapter);
			

        }
        catch (NullPointerException e) 
		{
			e.printStackTrace();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) 
			{
		    	Intent intent = new Intent();
		    	String Name = ((TextView) view.findViewById(R.id.Player_Label)).getText().toString();
		    	intent.putExtra("name", (Serializable)Name);
		    	setResult(RESULT_OK, intent);
		    	finish();
			}
		});
	}
	@Override
	public void onClick(View view) 
	{
		switch(view.getId())
		{
		    case R.id.btnPlayers:
		    	
		    	break;
		    case R.id.btnJoin:
		    	
		    	break;
		    default:
		    	break;
		    	
	    }		
	}
}
