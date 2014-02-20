package com.example.crystalgame;

import android.content.Intent;

import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.ui.CreateGameActivity;

public class ServerInstructionsHandler extends InstructionEventListener {

	private CrystalGame application;
	
	public ServerInstructionsHandler(CrystalGame application) {
		this.application = application;
	}
	
	@Override
	public void onGroupInstruction(InstructionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupStatusInstruction(InstructionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameInstruction(InstructionEvent event) {
		Intent intent = new Intent(application, CreateGameActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		application.startActivity(intent);
	}

}
