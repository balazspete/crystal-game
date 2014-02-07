package com.example.crystalgame.communication;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.library.communication.Communication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

/**
 * The service responsible for the management of communication
 * @author Balazs Pete, Shen Chen
 *
 */
public class CommunicationService extends Service {

	private Communication communication;
	
	public void onCreate() {
		super.onCreate();
		// TODO: get IP from config
		ClientCommunicationManager manager = new ClientCommunicationManager("192.168.0.3", 3000);
		ClientOutgoingMessages out = new ClientOutgoingMessages();
		communication = new Communication(manager, out);
		
		CrystalGame app = (CrystalGame) getApplication();
    	app.addCommunication(communication);
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
