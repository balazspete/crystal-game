package com.example.crystalgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter implements OnClickListener 
{
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater =null;
	private HashMap<String, String> player = new HashMap<String, String>();
    public PlayListAdapter(Activity act, ArrayList<HashMap<String,String>> playerList) 
    {
        this.activity = act;
        this.data = playerList;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	@Override
	public int getCount() 
	{
		return data.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
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
        	view = inflater.inflate(R.layout.player_list_item, null);
        }

        TextView PlayerName = (TextView)view.findViewById(R.id.Player_Label);
        player = data.get(position);
        PlayerName.setText(player.get("Name"));
        
        view.setOnClickListener(this);
        return view;
        
	}

	@Override
	public void onClick(View v) 
	{

	}

}
