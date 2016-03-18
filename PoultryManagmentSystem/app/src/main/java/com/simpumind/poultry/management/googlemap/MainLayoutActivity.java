package com.simpumind.poultry.management.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import managment.poultry.simpumind.poultrymanagmentsystem.R;

public class MainLayoutActivity extends Activity  implements OnClickListener{

	private Button btnBuis;
	private Button btnBuy;
	private Button btnFeeds;
	private Button btnLegal;
	private Button btnPrb;
	private Button btnMedic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		btnBuis=(Button)findViewById(R.id.btnBuisness);
		btnBuis.setOnClickListener(this);
		
		btnBuy=(Button)findViewById(R.id.btnBuy);
		btnBuy.setOnClickListener(this);
		
		btnFeeds=(Button)findViewById(R.id.btnFeeds);
		btnFeeds.setOnClickListener(this);
		
		btnLegal=(Button)findViewById(R.id.btnLegal);
		btnLegal.setOnClickListener(this);
		
		btnPrb=(Button)findViewById(R.id.btnPrb);
		btnPrb.setOnClickListener(this);
		
		btnMedic=(Button)findViewById(R.id.btnMedic);
		btnMedic.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_layout, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.btnBuisness:
			openActivity(new Intent(MainLayoutActivity.this,Buisness.class));
			break;
		case R.id.btnBuy:
			openActivity(new Intent(MainLayoutActivity.this,MapView.class));
			break;
		case R.id.btnFeeds:
			openActivity(new Intent(MainLayoutActivity.this,Feeds.class));
			break;
		case R.id.btnLegal:
			openActivity(new Intent(MainLayoutActivity.this,Legal.class));
			break;
		case R.id.btnPrb:
			openActivity(new Intent(MainLayoutActivity.this,Problem.class));
			break;
		case R.id.btnMedic:
			openActivity(new Intent(MainLayoutActivity.this,MedicalWelfare.class));
			break;
		}
	}
	
	private void openActivity(Intent n)
	{
		startActivity(n);
	}
	

}
