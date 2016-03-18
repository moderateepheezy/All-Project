package com.simpumind.poultry.management.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simpumind.poultry.management.model.Geo;
import com.simpumind.poultry.management.model.GeoDataSource;

import java.util.List;
import java.util.Random;

import managment.poultry.simpumind.poultrymanagmentsystem.R;


public class MapView extends Activity {
	static final LatLng HAMBURG = new LatLng(28, 77);
	static final LatLng HAMBURG1 = new LatLng(27, 78);
	static final LatLng HAMBURG2 = new LatLng(30, 75);
	static final LatLng HAMBURG3 = new LatLng(40, 70);

	private GoogleMap map;

	private static final String LOGTAG = "GPS";
	private GeoDataSource dataSource;
	private List<Geo> geoObject;

	private void getDataFromDB() {

		try {
			dataSource = new GeoDataSource(this);
			dataSource.open();
			geoObject = dataSource.findall();
			Log.i(LOGTAG, "" + geoObject.size());
			dataSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getDataFromDB();
		Toast.makeText(getApplicationContext(), "found=" + geoObject.size(),
				Toast.LENGTH_SHORT).show();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		Marker kiel = map.addMarker(new MarkerOptions().position(HAMBURG)
				.title("Eggs").snippet(random() + " number of eggs")
				.icon(BitmapDescriptorFactory.fromResource(R.mipmap.sail)));
		
		Marker kiem = map.addMarker(new MarkerOptions().position(HAMBURG1)
				.title("Eggs").snippet(random() + " number of eggs")
				.icon(BitmapDescriptorFactory.fromResource(R.mipmap.sail)));

		Marker kie = map.addMarker(new MarkerOptions().position(HAMBURG2)
				.title("Eggs").snippet(random() + " number of eggs")
				.icon(BitmapDescriptorFactory.fromResource(R.mipmap.sail)));
		
		Marker k = map.addMarker(new MarkerOptions().position(HAMBURG3)
				.title("Chicken").snippet(random() + " kg of chicken")
				.icon(BitmapDescriptorFactory.fromResource(R.mipmap.buy)));

		// Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);

	}

	private int random() {
		// TODO Auto-generated method stub
		Random rn = new Random();
		int maximum = 40;
		int minimum = 10;
		int n = maximum - minimum + 1;
		int i = rn.nextInt() % n;
		return minimum + i;
	}

	public void placeOrder(View v) {
		Intent newInt = new Intent(MapView.this, PlaceOrder.class);
		startActivity(newInt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
