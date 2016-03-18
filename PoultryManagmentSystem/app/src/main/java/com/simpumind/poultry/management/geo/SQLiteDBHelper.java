package com.simpumind.poultry.management.geo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDBHelper extends SQLiteOpenHelper {
private static final String LOGTAG="GEO";
private static final String DATABASE_NAME="gps5.db";
private static final int DATABASE_VERSION=1;

public static final String TABLE_GEO="geoposition";
public static final String COLUMN_ID="geoId";
public static final String COLUMN_DATE="geodate";
public static final String COLUMN_TIME="geotime";
public static final String COLUMN_LATITUDE="geolatitude";
public static final String COLUMN_LOGITUDE="geologitude";
public static final String COLUMN_LOCATION_NAME="address";

private static final String TABLE_CREATE=
				"CREATE TABLE "+ TABLE_GEO +"("+ COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+COLUMN_DATE + " TEXT, "
				+COLUMN_TIME+" TEXT, "
				+COLUMN_LATITUDE+" REAL NOT NULL, "
				+COLUMN_LOGITUDE+" REAL NOT NULL, "
				+COLUMN_LOCATION_NAME+" TEXT)";


	
	public SQLiteDBHelper(Context context) {
		super(context,DATABASE_NAME, null,DATABASE_VERSION);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "DATABASE IS CREATED");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_GEO );
		onCreate(db);

	}

}
