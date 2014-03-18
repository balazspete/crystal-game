package com.example.crystalgame.game;

import com.example.crystalgame.R;
import com.example.crystalgame.R.layout;
import com.example.crystalgame.R.menu;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Sage;
import com.example.crystalgame.library.data.Warrior;
import com.example.crystalgame.library.data.Wizard;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.ui.GPSTracker;
import com.example.crystalgame.ui.GameActivity;
import com.example.crystalgame.ui.InventoryManager;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class CharacterSelectionActivity extends Activity implements OnCheckedChangeListener, OnClickListener
{

	private TextView textViewCharacterType ;
	private Button button;
	private int checkedButton = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_selection);
		textViewCharacterType = (TextView)findViewById(R.id.textViewCharacterType);
		button = (Button)findViewById(R.id.buttonChooseCharacter);
		RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.select_character_group);
		radioGroup.setOnCheckedChangeListener(this);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.character_selection, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		switch (checkedId) {
		case R.id.sageImage:
			checkedButton = 0;
			textViewCharacterType.setText("Sage");
			break;
		case R.id.warriorImage:
			checkedButton = 1;
			textViewCharacterType.setText("Warrior");
			break;
		case R.id.wizardImage:
			checkedButton = 2;
			textViewCharacterType.setText("Wizard");
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) 
		{
			case R.id.buttonChooseCharacter:
				launchGame();
				break;
			default:
				break;
		}

	}
	
	public void launchGame()
	{
		if (checkedButton < 0) {
			Toast.makeText(getApplicationContext(), R.string.select_character_warning, Toast.LENGTH_LONG ).show();
			return;
		}
		
		Location location = GPSTracker.getInstance().getLocation();
		System.out.println("location:" + location + " " + GPSTracker.getInstance().getLatitude());
		
		Character character = InventoryManager.getInstance().getCharacter();
		Character myCharacter;
		switch(checkedButton) {
			case 0: 
				myCharacter = Sage.create(character, location.getLatitude(), location.getLongitude());
				break;
			case 1:
				myCharacter = Warrior.create(character, location.getLatitude(), location.getLongitude());
				break;
			default:
				myCharacter = Wizard.create(character, location.getLatitude(), location.getLongitude());
				break;
		}
		
		try {
			ClientDataWarehouse.getInstance().put(Character.class, myCharacter);
			
			Intent intent = new Intent(getApplicationContext(), GameActivity.class);
			startActivity(intent);
		} catch (DataWarehouseException e) {
			Toast.makeText(this, R.string.communication_failure, Toast.LENGTH_SHORT).show();
		}
		
		
	}

}
