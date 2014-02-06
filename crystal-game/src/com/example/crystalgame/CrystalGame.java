package com.example.crystalgame;

import android.app.Application;

import com.example.crystalgame.library.communication.Communication;

public class CrystalGame extends Application {

	private Communication communication;
	
	public void addCommunication(Communication communication) {
		this.communication = communication;
	}
	
	public Communication getCommunication() {
		return communication;
	}
	
}
