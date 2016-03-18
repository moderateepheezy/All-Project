package com.simpumind.poultry.management.googlemap;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.os.Bundle;
import java.util.Locale;
import android.location.Address;
import android.location.Geocoder;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.simpumind.poultry.management.geo.Geo;
import com.simpumind.poultry.management.geo.GeoDataSource;

public class ServiceNew extends Service {

	private static final String TAG = "SERVICE_TAG";
	private LocationManager mLocationManager = null;
	private static final int LOCATION_INTERVAL = 10000;
	private static final float LOCATION_DISTANCE = 1000f;

	private class LocationListener implements android.location.LocationListener {
		Location mLastLocation;
		private static final String LOGTAG = "GPS";
		GeoDataSource datasource;
		private String address;

		@SuppressLint("SimpleDateFormat")
		private String getTime() {
			Calendar cal = Calendar.getInstance();
			cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			return sdf.format(cal.getTime());
		}

		@SuppressLint("SimpleDateFormat")
		private String getDate() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			return dateFormat.format(date);
		}

		// Function to get Location Address by Longitude and latitude
		private String getAddress(double LATITUDE, double LONGITUDE) {

			Log.i(LOGTAG,LONGITUDE+" "+LATITUDE);
			// Declaring Geo COder CLass
			Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.ENGLISH);

			try {
				List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 1);
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("Address:: ");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				this.address = strReturnedAddress.toString();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(LOGTAG, "Address Error");
				address="Address not found";
			}
			return address;
		}

		private void createdata(double lat, double longi) {
			datasource = new GeoDataSource(getApplicationContext());
			datasource.open();
			Geo geo = new Geo();
			geo.setDate(this.getDate());
			geo.setTime(this.getTime());
			geo.setlatitude(lat);
			geo.setlongitude(longi);
			try {
				geo.setLocationName(this.getAddress(lat, longi));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(LOGTAG, "Problem in Geo Address");
				e.printStackTrace();
			}
			geo = datasource.create(geo);
			Log.i(LOGTAG, "Inserted INto DAtabase" + geo.getId());
			datasource.close();
		}

		public LocationListener(String provider) {
			Log.e(LOGTAG, "LocationListener " + provider);
			mLastLocation = new Location(provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.e(LOGTAG, "onLocationChanged: " + location);
			mLastLocation.set(location);
			Log.i(LOGTAG,location.getLatitude()+" "+ location.getLongitude());
			createdata(location.getLatitude(), location.getLongitude());
			ServiceNew.this.stopSelf();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.e(LOGTAG, "onProviderDisabled: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.e(LOGTAG, "onProviderEnabled: " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.e(TAG, "onStatusChanged: " + provider);
		}
	}

	LocationListener[] mLocationListeners = new LocationListener[] {
			new LocationListener(LocationManager.GPS_PROVIDER),
			new LocationListener(LocationManager.NETWORK_PROVIDER) };

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate");
		initializeLocationManager();
		try {
			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL,
					LOCATION_DISTANCE, mLocationListeners[1]);
		} catch (java.lang.SecurityException ex) {
			Log.i(TAG, "fail to request location update, ignore", ex);
		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "network provider does not exist, " + ex.getMessage());
		}
		try {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, LOCATION_INTERVAL,
					LOCATION_DISTANCE, mLocationListeners[0]);
		} catch (java.lang.SecurityException ex) {
			Log.i(TAG, "fail to request location update, ignore", ex);
		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "gps provider does not exist " + ex.getMessage());
		}
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
		if (mLocationManager != null) {
			for (int i = 0; i < mLocationListeners.length; i++) {
				try {
					mLocationManager.removeUpdates(mLocationListeners[i]);
				} catch (Exception ex) {
					Log.i(TAG, "fail to remove location listners, ignore", ex);
				}
			}
		}
	}

	private void initializeLocationManager() {
		Log.e(TAG, "initializeLocationManager");
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}
	}
}