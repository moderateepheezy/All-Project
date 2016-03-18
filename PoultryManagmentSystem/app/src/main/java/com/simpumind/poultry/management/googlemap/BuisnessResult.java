package com.simpumind.poultry.management.googlemap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.simpumind.poultry.management.model.Geo;
import com.simpumind.poultry.management.model.GeoDataSource;
import com.simpumind.poultry.management.model.SQLiteDBHelper;

import managment.poultry.simpumind.poultrymanagmentsystem.R;

public class BuisnessResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buisness_result);
		Intent getIntent = getIntent();
		int rValue = getIntent.getIntExtra(Buisness.SECRET, 0);
		Toast.makeText(this, "" + rValue, Toast.LENGTH_LONG).show();
		if (rValue == 0)
			getData("Egg");
		else if (rValue == 1)
			getData("Chicken");
		else if (rValue == 2)
			getData("Egg Chicken");

	}

	protected void getData(String str) {
		try {
			GeoDataSource gd = new GeoDataSource(getApplicationContext());
			gd.open();
			List<Geo> gm = gd.findfilter(SQLiteDBHelper.COLUMN_TRADE + "='"
					+ str + "'");
			Log.i("101", gm.size() + "");

			ArrayAdapter<Geo> dap = new ArrayAdapter<Geo>(BuisnessResult.this,
					android.R.layout.simple_list_item_1, gm);

			Toast.makeText(getApplicationContext(), "" + gm.size(),
					Toast.LENGTH_SHORT).show();
			ListView lst = (ListView) findViewById(R.id.listView1);
			lst.setAdapter(dap);
			gd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("101", e.toString());
		}
	}

}
