package com.simpumind.poultry.management.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDBHelper extends SQLiteOpenHelper {
	private static final String LOGTAG = "GEO";
	private static final String DATABASE_NAME = "db1";
	private static final int DATABASE_VERSION = 4;

	public static final String TABLE_HENS = "hens";
	public static final String COLUMN_ID = "henId";
	public static final String COLUMN_NAME = "breedName";
	public static final String COLUMN_MALE_PRICE = "malePrice";
	public static final String COLUMN_FEMALE_PRICE = "femalePrice";
	public static final String COLUMN_TRADE = "trade";

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_HENS
			+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME + " TEXT, " + COLUMN_MALE_PRICE
			+ " REAL NOT NULL, " + COLUMN_FEMALE_PRICE + " REAL NOT NULL, "
			+ COLUMN_TRADE + " TEXT NOT NULL)";

	public SQLiteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "DATABASE IS CREATED");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HENS);
		onCreate(db);

	}

}
