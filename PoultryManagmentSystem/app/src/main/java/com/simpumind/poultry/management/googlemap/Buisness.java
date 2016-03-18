package com.simpumind.poultry.management.googlemap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.simpumind.poultry.management.model.Geo;
import com.simpumind.poultry.management.model.GeoDataSource;
import com.simpumind.poultry.management.model.SQLiteDBHelper;

import managment.poultry.simpumind.poultrymanagmentsystem.R;


public class Buisness extends Activity {
	private static final String LOGTAG = "HENS";
	protected static final String SECRET = "com.example.model";
	private int  val=0;
	GeoDataSource datasource;
	String[] lst = new String[] { "Eggs", "Chicken", "Eggs & Chicken" };
	String[] lst1 = new String[] { "100 $ to 200$ ", "200 $ to 300 $",
			" 300 $ to 400 $" };

	// Chicken based Hens
	String[] strChicken = new String[] { "Black Sexlink Chicks",
			"Red Sexlink Chicks", "Production Red Chicks",
			"Cornish Rock Chicks", "White Laced Red Cornish Chicks" };
	double[][] strChkValue = new double[][] { { 2.95, 3.65 }, { 2.95, 3.75 },
			{ 2.95, 3.75 }, { 2.80, 3.0 }, { 4.82, 2.95 } };

	// Egg based Hens
	String[] strEgg = new String[] { "White Leghorn Chicks",
			"California White Chicks", "Gold Sexlink Chicks",
			"Red Sexlink Chicks" };

	double[][] strEggValue = new double[][] { { 3.30, 3.95 }, { 2.75, 3.95 },
			{ 2.75, 3.95 }, { 2.75, 3.0 } };

	// Common Hens having both capability
	String[] strEggChicken = new String[] { "California Grey Chicks",
			"Delaware Chicks", "White Rock Chicks", "New Hampshire Chicks",
			"Black Jersey Giant Chicks" };

	double[][] strEggChkValue = new double[][] { { 2.75, 3.95 },
			{ 2.75, 4.25 }, { 2.75, 3.95 }, { 2.75, 3.95 }, { 2.95, 4.80 } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buisness);
		getData("Egg");
		// Type of Buisness
		Spinner spn = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adap = new ArrayAdapter<CharSequence>(
				getApplicationContext(), R.layout.spinner, lst);
		spn.setAdapter(adap);

		// Budget
		Spinner spn1 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> adap1 = new ArrayAdapter<CharSequence>(
				getApplicationContext(), R.layout.spinner, lst1);
		spn1.setAdapter(adap1);

		spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				val=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				val=0;
			}
		});

		Button btn=(Button)findViewById(R.id.btnInsert);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(val>=0 && val<=2)
				{
				Intent n=new Intent(getApplicationContext(), BuisnessResult.class);
				n.putExtra(SECRET, val);
				startActivity(n);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please select hens option",Toast.LENGTH_SHORT ).show();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void getData(String str) {
		try {
			GeoDataSource gd = new GeoDataSource(getApplicationContext());
			gd.open();
			List<Geo> gm = gd.findfilter(SQLiteDBHelper.COLUMN_TRADE + "='"
					+ str + "'");
			Log.i("101", gm.size() + "");
			if(gm.size()<1)
				insertData();
			Toast.makeText(getApplicationContext(), "" + gm.size(),
					Toast.LENGTH_SHORT).show();
			gd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("101", e.toString());
		}
	}


	private void insertData() {
		for (int i = 0; i < strEggValue.length; i++)
			this.createdata(strEgg[i], strEggValue[i], "Egg");

		for (int i = 0; i < strChkValue.length; i++)
			this.createdata(strChicken[i], strChkValue[i], "Chicken");

		for (int i = 0; i < strEggChicken.length; i++)
			this.createdata(strEggChicken[i], strEggChkValue[i], "Egg Chicken");

	}

	private void createdata(String name, double mPrice[], String trade) {
		datasource = new GeoDataSource(getApplicationContext());
		datasource.open();
		Geo geo = new Geo();
		geo.setName(name);
		geo.setMalePrice(mPrice[0]);
		geo.setFemalePrice(mPrice[1]);
		geo.setTrade(trade);
		geo = datasource.create(geo);
		Log.i(LOGTAG, "Inserted INTO DAtabase" + geo.getId());
		datasource.close();
	}
}
