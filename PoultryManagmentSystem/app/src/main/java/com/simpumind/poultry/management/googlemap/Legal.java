package com.simpumind.poultry.management.googlemap;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import managment.poultry.simpumind.poultrymanagmentsystem.R;

public class Legal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal);
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Urban");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Urban");
		

		TabSpec spec2=tabHost.newTabSpec("Rural");
		spec2.setIndicator("Rural");
		spec2.setContent(R.id.tab2);

		//TabSpec spec3=tabHost.newTabSpec("Tab 3");
		//spec3.setIndicator("Expert System");
		//spec3.setContent(R.id.tab3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		//tabHost.addTab(spec3);
		
	}

}

