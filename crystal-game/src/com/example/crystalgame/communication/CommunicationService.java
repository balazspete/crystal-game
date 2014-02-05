package com.example.crystalgame.communication;

import com.example.crystalgame.library.communication.Communication;

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

	private Communication communication;
	
	public void onCreate() {
		super.onCreate();
		ClientCommunicationManager manager = new ClientCommunicationManager("192.168.1.8", 3000);
		communication = new Communication(manager);
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
		// TODO Auto-generated method stub
		return null;
	}

}
