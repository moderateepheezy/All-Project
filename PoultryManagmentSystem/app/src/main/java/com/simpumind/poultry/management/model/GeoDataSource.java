package com.simpumind.poultry.management.model;

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
			SQLiteDBHelper.COLUMN_NAME, SQLiteDBHelper.COLUMN_MALE_PRICE,
			SQLiteDBHelper.COLUMN_FEMALE_PRICE, SQLiteDBHelper.COLUMN_TRADE };

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
		values.put(SQLiteDBHelper.COLUMN_NAME, geo.getName());
		values.put(SQLiteDBHelper.COLUMN_MALE_PRICE, geo.getMalePrice());
		values.put(SQLiteDBHelper.COLUMN_FEMALE_PRICE, geo.getFemalePrice());
		values.put(SQLiteDBHelper.COLUMN_TRADE, geo.getTrade());
		long insertid = database.insert(SQLiteDBHelper.TABLE_HENS, null, values);
		geo.setId(insertid);
		return geo;
	}

	public List<Geo> findall() {

		Cursor cursor = database.query(SQLiteDBHelper.TABLE_HENS, allcolumns,
				null, null, null, null, null);
		Log.i(LOGTAG, "returned" + cursor.getCount() + "rows");
		List<Geo> geo = cursorToList(cursor);
		return geo;
	}

	public List<Geo> findfilter(String selection) {

		Cursor cursor = database.query(SQLiteDBHelper.TABLE_HENS, allcolumns,
				selection, null, null, null, null);
		Log.i(LOGTAG, "returned" + cursor.toString() + "rows");
		List<Geo> geo = cursorToList(cursor);
		return geo;
	}

	private List<Geo> cursorToList(Cursor cursor) {
		List<Geo> geo = new ArrayList<Geo>();
		if (cursor.getCount() > 0) {
			Log.i(LOGTAG, "In Cursor");
			while (cursor.moveToNext()) {
				Geo geos = new Geo();

				Log.i(LOGTAG, "In While");
				geos.setId(cursor.getLong(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_ID)));
				geos.setName(cursor.getString(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_NAME)));
				geos.setMalePrice(cursor.getLong(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_MALE_PRICE)));
				geos.setFemalePrice(cursor.getDouble(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_FEMALE_PRICE)));
				geos.setTrade(cursor.getString(cursor
						.getColumnIndex(SQLiteDBHelper.COLUMN_TRADE)));
				geo.add(geos);
			}
		}
		return geo;
	}

}
