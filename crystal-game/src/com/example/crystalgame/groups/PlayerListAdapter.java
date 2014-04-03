package com.example.crystalgame.groups;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.crystalgame.R;
import com.example.crystalgame.R.id;
import com.example.crystalgame.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerListAdapter extends BaseAdapter {
	
	private Activity activity;
	private List<Player> players;
	
    public PlayerListAdapter(Activity act) 
    {
        this.activity = act;
        
        players = new ArrayList<Player>();
    }
    
    public void refreshData(HashMap<String, String> playerInfo) {
    	players.clear();
    	
    	for (String key : playerInfo.keySet()) {
        	players.add(new Player(key, playerInfo.get(key)));
        }
    	
    	this.notifyDataSetChanged();
    }
    
    public void refreshData(String[] playerInfo) {
    	players.clear();
    	
    	for (int i = 0; i < playerInfo.length; i+=2) {
    		players.add(new Player(playerInfo[i], playerInfo[i+1]));
    	}
    	
    	this.notifyDataSetChanged();
    }
    
	@Override
	public int getCount() 
	{
		return players.size();
	}

	@Override
	public Player getItem(int position) 
	{
		return players.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view=convertView;
        if(convertView==null)
        {
        	view = activity.getLayoutInflater().inflate(R.layout.select_player_item, null);
        }
        
        TextView playerName = (TextView)view.findViewById(R.id.Player_Label);
        playerName.setText(players.get(position).name);
        
        return view;
	}
}
