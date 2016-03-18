package com.simpumind.poultry.management.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import managment.poultry.simpumind.poultrymanagmentsystem.R;

public class PlaceOrder extends Activity {

	String[] lst = new String[] { "Eggs", "Chicken" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_order);
		// Type of Business
		Spinner spn = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adap = new ArrayAdapter<CharSequence>(
				getApplicationContext(), R.layout.spinner, lst);
		spn.setAdapter(adap);

		Button btn = (Button) findViewById(R.id.btnInsert);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Created",Toast.LENGTH_SHORT).show();
				Intent n = new Intent(PlaceOrder.this, ServiceNew.class);
				startService(n);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_order, menu);
		return true;
	}

}
