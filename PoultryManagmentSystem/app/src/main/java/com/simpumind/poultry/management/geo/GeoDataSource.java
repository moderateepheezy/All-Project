package com.simpumind.poultry.management.geo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GeoDataSource {

	private static final String LOGTAG = "NewDB";
	SQLiteDatabase database;
	SQLiteOpenHelper dbhelper;
	private static final String[] allcolumns = { SQLiteDBHelper.COLUMN_ID,
			SQLiteDBHelper.COLUMN_DATE, SQLiteDBHelper.COLUMN_TIME,
			SQLiteDBHelper.COLUMN_LATITUDE, SQLiteDBHelper.COLUMN_LOGITUDE ,
			SQLiteDBHelper.COLUMN_LOCATION_NAME};

	public GeoDataSource(Context context) {
		dbhelper = new SQLiteDBHelper(context);
	}

	public void open() {
		Log.i(LOGTAG, "open");
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "close");
		dbhelper.close();
	}

	public Geo create(Geo geo) {
		
		ContentValues values = new ContentValues();
		Log.i(LOGTAG, "In the create");
		values.put(SQLiteDBHelper.COLUMN_DATE, geo.getDate());
		values.put(SQLiteDBHelper.COLUMN_TIME, geo.getTime());
		values.put(SQLiteDBHelper.COLUMN_LATITUDE, geo.getlatitude());
		values.put(SQLiteDBHelper.COLUMN_LOGITUDE, geo.getlongitude());
		values.put(SQLiteDBHelper.COLUMN_LOCATION_NAME,geo.getLocationName());
		long insertid = database.insert(SQLiteDBHelper.TABLE_GEO, null, values);
		geo.setId(insertid);
		return geo;
	}

	public List<Geo> findall() {
		
		Cursor cursor = database.query(SQLiteDBHelper.TABLE_GEO, allcolumns,
				null, null, null, null, null);
		Log.i(LOGTAG, "returned" + cursor.getCount() + "rows");
		List<Geo> geo = cursorToList(cursor);
		return geo;
	}
	
	
	public List<Geo> findfilter(String selection) {
		
		Cursor cursor = database.query(SQLiteDBHelper.TABLE_GEO, allcolumns,
				selection, null, null, null,null);
		Log.i(LOGTAG, "returned"+cursor.toString()+"rows");
		List<Geo> geo = cursorToList(cursor);
		return geo;
	}

	private List<Geo> cursorToList(Cursor cursor) {
		List<Geo> geo = new ArrayList<Geo>();
		if (cursor.getCount() > 0) {
			Log.i(LOGTAG,"In Cursor");
			while (cursor.moveToNext()) {
				Geo geos = new Geo();

				Log.i(LOGTAG,"In While");
				geos.setId(cursor.getLong(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_ID)));
				geos.setDate(cursor.getString(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_DATE)));
				geos.setTime(cursor.getString(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_TIME)));
				geos.setlatitude(cursor.getDouble(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_LATITUDE)));
				geos.setlongitude(cursor.getDouble(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_LOGITUDE)));
				geos.setLocationName(cursor.getString(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_LOCATION_NAME)));
				geo.add(geos);
			}
		}
		return geo;
	}

}
