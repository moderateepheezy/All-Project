package com.renyu.alumni.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	final static String DBNAME="alumni";
	final static int DBVERSION=2;
	
	final static String ID="_id";
	
	//发布活动表
	final static String UPLOADTABLE="upload_table";
	final static String UPLOADPATH="upload_path";
	final static String UPPARAMS="upload_params";
	
	//用户表
	final static String USERTABLE="user_table";
	final static String USERINFO="user_info";
	final static String USERSTATE="user_state";
	final static String USERID="user_id";
	
	//私信联系人列表
	final static String PRIVATELISTTABLE="private_list_table";
	final static String PRIVATELISTUSERID="private_list_userid";
	final static String PRIVATELISTTOUSERID="private_list_touserid";
	final static String PRIVATELISTUSERNAME="private_list_username";
	final static String PRIVATELISTAVATAR="private_list_avatar";
	final static String PRIVATELISTCONTENT="private_list_content";
	final static String PRIVATELISTTIME="private_list_time";
	final static String PRIVATELISTNOREADCOUNT="private_list_noreadtime";
	final static String PRIVATELISTTYPE="private_list_type";
	
	//私信表
	final static String PRIVATELETTERUSERID="private_letter_userid";
	final static String PRIVATELETTERTOUSERID="private_letter_touserid";
	final static String PRIVATELETTERUSERNAME="private_letter_username";
	final static String PRIVATELETTERUSERIMAGE="private_letter_userimage";
	final static String PRIVATELETTERUSERCONTENT="private_letter_usercontent";
	final static String PRIVATELETTERCONTENTTIME="private_letter_contenttime";
	final static String PRIVATELETTERTYPE="private_letter_type";
	//0：回复别人 1：收到别人
	final static String PRIVATELETTERTO="private_letter_to";
	final static String PRIVATELETTERSUCCESS="private_letter_sucess";
	public SQLiteHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS "+UPLOADTABLE+"("+ID+" integer primary key AUTOINCREMENT, "+UPLOADPATH+" text, "+UPPARAMS+" text)");
		db.execSQL("CREATE TABLE IF NOT EXISTS "+USERTABLE+"("+ID+" integer primary key AUTOINCREMENT, "+USERINFO+" blob, "+USERSTATE+" integer, "+USERID+" integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + PRIVATELISTTABLE + "(" + ID
				+ " integer primary key autoincrement," + PRIVATELISTTOUSERID
				+ " integer, " + PRIVATELISTUSERID + " integer,"
				+ PRIVATELISTUSERNAME + " text," + PRIVATELISTAVATAR + " text,"
				+ PRIVATELISTCONTENT + " text," + PRIVATELISTTIME + " text,"
				+ PRIVATELISTNOREADCOUNT + " integer, " + PRIVATELISTTYPE
				+ " integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
