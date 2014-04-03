package com.example.crystalgame.game;

import java.text.DecimalFormat;

import com.example.crystalgame.CrystalGame;

import android.util.Log;


/**
 * Management of game time
 * @author Allen Thomas Varghese
 */
public class GameTimer extends Thread {

	private boolean isRunning = false;
	private static GameTimer gameTimer = null;
	
	private GameTimer() {
		super("GameTimer");
	}

	public static GameTimer getInstance() {
		if(null == gameTimer) {
			gameTimer = new GameTimer();
		}
		
		return gameTimer;
	}

	/**
	 * Initiate the tracking of time as a thread in the background
	 */
	public synchronized void startTimeManager() {
		// Checking if the thread has already started
		if(!this.isAlive()) {
			this.start();
		}
	}
	
	/**
	 * Stop the time manager thread
	 */
	public synchronized void stopTimeManager() {
		if(this.isAlive()) {
			this.isRunning = false;
		}
	}
	
	@Override
	public void run() {
		Log.d("GameTimer", "Time Tracking thread started...");
		isRunning = true;
		
		int gameTime = CrystalGame.getTimeDuration();
		int timeCounter = gameTime;
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		StringBuffer sbf = new StringBuffer();
		
		try {
			while(isRunning) {
				Thread.sleep(3000);
				timeCounter -= 3;
				
				sbf.delete(0, sbf.length());
				sbf.append(""+decimalFormat.format(timeCounter/60.0));
				GameManager.getInstance().timeChangeCallback(sbf.toString());
			}
		} catch(InterruptedException e) {
			isRunning = false;
		}
	}
}
