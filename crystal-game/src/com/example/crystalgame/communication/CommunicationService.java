package com.example.crystalgame.communication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * The service responsible for the management of communication
 * @author Balazs Pete, Shen Chen
 *
 */
public class CommunicationService extends Service {

	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startService(intent);
		
		// TODO: remove 
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
