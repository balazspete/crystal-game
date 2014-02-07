package com.example.crystalgame.communication;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.R;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * The service responsible for the management of communication
 * @author Balazs Pete, Shen Chen
 *
 */
public class CommunicationService extends Service {

	private ClientCommunication communication;
	
	public void onCreate() {
		super.onCreate();
		// TODO: get IP from config
		addCommunication();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startService(intent);
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void addCommunication() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Integer port  = null;
		try {
			port = Integer.parseInt(sp.getString(getString(R.string.PORT), "3000"));
		} catch(NumberFormatException e) {
			Log.e("CommunicationService", e.getMessage());
		} finally {
			if (port == null) {
				port = 3000;
			}
		}
		
		ClientCommunicationManager manager = new ClientCommunicationManager(
				sp.getString(getString(R.string.SERVER_ADDRESS), "example.com"), 
				port);
		
		ClientOutgoingMessages out = new ClientOutgoingMessages();
		communication = new ClientCommunication(manager, out);
		
		CrystalGame app = (CrystalGame) getApplication();
    	app.addCommunication(communication);
	}
}
