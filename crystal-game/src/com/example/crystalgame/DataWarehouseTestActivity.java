package com.example.crystalgame;

import java.util.List;

import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.data.StringWithID;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity for testing the data warehouse
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataWarehouseTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_warehouse_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void saveText(View view) {
		StringWithID s = new StringWithID(((EditText)findViewById(R.id.editText2)).getText().toString());
		try {
			ClientDataWarehouse.getInstance().put(StringWithID.class, s);
		} catch (DataWarehouseException e) {
			Log.e("DataWarehouseTestActivity", e.getMessage());
		}
	}
	
	public void getText(View view) {
		try {
			List<HasID> list = ClientDataWarehouse.getInstance().getList(StringWithID.class);
			StringBuffer str = new StringBuffer();
			for (HasID e : list) {
				str.append(((StringWithID)e).getValue());
				str.append("\n");
			}
			if (list.size() == 0) {
				str.append("No data in DW");
			}
			
			((EditText) findViewById(R.id.group_name)).setText(str);
		} catch (DataWarehouseException e) {
			Log.e("DataWarehouseTestActivity", e.getMessage());
		}
	}
	
	public void showSettings(MenuItem item) {
		Toast.makeText(this, "not supported", Toast.LENGTH_SHORT).show();
	}
	
}
