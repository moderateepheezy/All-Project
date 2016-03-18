package com.simpumind.poultry.management.googlemap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import managment.poultry.simpumind.poultrymanagmentsystem.R;

public class MedicalWelfare extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medical_welfare);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.medical_welfare, menu);
		return true;
	}

}
