package com.example.crystalgame.game;

import com.example.crystalgame.R;
import com.example.crystalgame.R.layout;
import com.example.crystalgame.R.menu;

import android.os.Bundle;
import android.app.Activity;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_selection);
		textViewCharacterType = (TextView)findViewById(R.id.textViewCharacterType);
		button = (Button)findViewById(R.id.buttonChooseCharacter);
		 RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.select_character_group);
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
			textViewCharacterType.setText("Sage");
			break;
		case R.id.warriorImage:
			textViewCharacterType.setText("Warrior");
	
			break;
		case R.id.wizardImage:
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
		Toast.makeText(getApplicationContext(),"launchGame",Toast.LENGTH_LONG ).show();
	}

}
