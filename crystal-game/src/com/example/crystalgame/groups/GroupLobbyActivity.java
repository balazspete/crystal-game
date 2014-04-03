package com.example.crystalgame.groups;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.DataWarehouseTestActivity;
import com.example.crystalgame.R;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.game.GameActivity;
import com.example.crystalgame.game.InventoryManager;
import com.example.crystalgame.library.data.Information;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.data.Character;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GroupLobbyActivity extends Activity {

	public static final String KEY_LOAD_DW = "com.example.crystalgame.groups.LOAD_DW";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_lobby);
		
		final Toast toast = Toast.makeText(this, "Loading group information...", Toast.LENGTH_LONG);
		toast.show();
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (ClientDataWarehouse.isNull) {
					try {
						synchronized(this) {
							this.wait(100);
						}
					} catch (InterruptedException e) {
					}
					System.out.println("Waiting");
				}
				
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						switchToGameIfNeeded();
						setTitle();
						toast.cancel();
					}
				});
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_lobby, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_leave_group:
				leaveGroup();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	public void requestGameStart(View view) {
		((CrystalGame) getApplication()).getCommunication().
			out.relayInstructionToServer(GameInstruction.createStartGameRequestGameInstruction());
	}
	
	public void setTitle() {
		String title = null;
		try {
			Information nameInfo = (Information) ClientDataWarehouse.getInstance().get(Information.class, Information.GROUP_NAME);
			if (nameInfo != null) {
				title = (String) (nameInfo).getValue();
			}
		} catch (DataWarehouseException e) {
			// Handling it in finally
		} finally {
			if (title == null) {
				title = "Group";
			}
		}
		
		this.setTitle(title);
	}
	
    public void launchDW(MenuItem item) {
    	startActivity(new Intent(this, DataWarehouseTestActivity.class));
    }
    
    private void leaveGroup() {
    	GroupInstruction instruction = GroupInstruction.leaveGroup();
    	((CrystalGame)getApplication()).getCommunication().out.sendGroupInstructionToServer(instruction);
    }
    
    private void switchToGameIfNeeded() {
    	Character c = InventoryManager.getInstance().getCharacter();
    	System.out.println("character " + c);
    	if (c != null) {
    		Intent intent = new Intent(this, GameActivity.class);
    		startActivity(intent);
    	}
    }
    
}
