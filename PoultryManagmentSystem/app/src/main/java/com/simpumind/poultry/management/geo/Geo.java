package com.simpumind.poultry.management.geo;

import android.util.Log;

public class Geo {
	private static final String LOGTAG = "GPS";
	private long id;
	private String Date;
	private String Time;
	private Double latitude;
	private Double longitude;
	private String locationName;

	public Geo() {
	}

	public long getId() {
		Log.i(LOGTAG, "" + id);
		return id;

	}

	public void setId(long id) {
		this.id = id;
		Log.i(LOGTAG, "" + id);

	}

	public String getDate() {
		Log.i(LOGTAG, "" + Date);
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
		Log.i(LOGTAG, "" + Date);
	}

	public String getTime() {
		Log.i(LOGTAG, "" + Time);
		return Time;
	}

	public void setTime(String Time) {
		this.Time = Time;
		Log.i(LOGTAG, "" + Time);
	}

	public Double getlatitude() {
		Log.i(LOGTAG, "" + latitude);
		return latitude;
	}

	public void setlatitude(Double latitude) {
		this.latitude = latitude;
		Log.i(LOGTAG, "" + latitude);
	}

	public Double getlongitude() {
		Log.i(LOGTAG, "" + longitude);
		return longitude;
	}

	public void setlongitude(Double longitude) {
		this.longitude = longitude;
		Log.i(LOGTAG, "" + longitude);
	}
	@Override
	public String toString() {
				return "Date= "+Date +"Time= "+Time+"\nLatitude= " +latitude + " Longitude= "+longitude;
	}

	public String getLocationName() {
		
		return locationName;
	}

	public void setLocationName(String location) {
		this.locationName = location;
		Log.i(LOGTAG, "" + location);
	}

}
