package com.renyu.alumni.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.model.PrivateLetterModel;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;
import com.renyu.alumni.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class DB {
	
	private final Context mContext;
	private static DB mInstance=null;
	private SQLiteHelper mSQLiteHelper=null;
	
	public synchronized static DB getInstance(final Context context) {
        if (DB.mInstance==null) {
            return new DB(context);
        }
        return DB.mInstance;
    }
	
	private DB(final Context context) {
        this.mContext=context;
        this.mSQLiteHelper=new SQLiteHelper(this.mContext);
        DB.mInstance=this;
    }
	
	/**
	 * �ϴ�ʧ�ܣ��������
	 * @param path
	 * @param params
	 */
	public synchronized void insertErrorImage(String path, String params) {
		ContentValues cv=new ContentValues();
		cv.put(SQLiteHelper.UPLOADPATH, path);
		cv.put(SQLiteHelper.UPPARAMS, params);
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		db.insert(SQLiteHelper.UPLOADTABLE, null, cv);
		db.close();
	}
	
	/**
	 * �ϴ���ϣ�ɾ������
	 * @param params
	 */
	public synchronized void deleteErrorImage(String params) {
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		db.delete(SQLiteHelper.UPLOADTABLE, SQLiteHelper.UPPARAMS+"=?", new String[]{params});
		db.close();
	}
	
	/**
	 * �������û�����
	 * @param model
	 */
	private synchronized void insertUserModel(UserModel model) {
		byte[] bytes=serialize((UserModel) model);
		ContentValues cv=new ContentValues(3);
		cv.put(SQLiteHelper.USERINFO, bytes);
		cv.put(SQLiteHelper.USERSTATE, 1);
		cv.put(SQLiteHelper.USERID, model.getUser_id());
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		db.beginTransaction();
		db.insert(SQLiteHelper.USERTABLE, null, cv);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();	
	}
	
	/**
	 * �����û���¼״̬
	 * @param user_id
	 */
	public synchronized void offlineUser(int user_id, int state) {
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		ContentValues cv=new ContentValues(1);
		cv.put(SQLiteHelper.USERSTATE, state);
		db.update(SQLiteHelper.USERTABLE, cv, SQLiteHelper.USERID+"=?", new String[]{""+user_id});
		db.close();
	}
	
	/**
	 * �û���¼����
	 * @param model
	 */
	public synchronized void updateUserModel(UserModel model) {
		SQLiteDatabase db=this.mSQLiteHelper.getReadableDatabase();
		Cursor cs=db.query(SQLiteHelper.USERTABLE, null, SQLiteHelper.USERID+"=?", new String[]{""+model.getUser_id()}, null, null, null);
		cs.moveToFirst();
		int count=cs.getCount();
		cs.close();
		db.close();
		if(count>0) {
			offlineUser(model.getUser_id(), 1);
		}
		else {
			insertUserModel(model); 
		}
	}
	
	/**
	 * ��ȡ�û���Ϣ
	 * @return
	 */
	public synchronized UserModel getUserModel() {
		SQLiteDatabase db=this.mSQLiteHelper.getReadableDatabase();
		Cursor cs=db.query(SQLiteHelper.USERTABLE, null, SQLiteHelper.USERSTATE+"=?", new String[]{"1"}, null, null, null);
		cs.moveToFirst();
		if(cs.getCount()>0) {
			UserModel model=deserializeUserModel(cs.getBlob(cs.getColumnIndex(SQLiteHelper.USERINFO)));
			cs.close();
			db.close();
			return model;
		}
		else {
			cs.close();
			db.close();
			return null;
		}
	}
	
	/**
	 * �޸��û�ͷ��
	 * @param model
	 */
	public synchronized void updateUserAvatar(UserModel model) {
		byte[] bytes=serialize((UserModel) model);
		ContentValues cv=new ContentValues(1);
		cv.put(SQLiteHelper.USERINFO, bytes);
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		db.update(SQLiteHelper.USERTABLE, cv, SQLiteHelper.USERSTATE+"=?", new String[]{"1"});
		db.close();
	}
	
	/**
	 * �������޸���ϵ���б���Ϣ
	 * @param model
	 */
	public synchronized void insertPrivateMessageList(Context context, ReceiverPrivateLetterModel model) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		Cursor cs=db.query(SQLiteHelper.PRIVATELISTTABLE, null, SQLiteHelper.PRIVATELISTUSERID+"=?", new String[] {""+model.getUser_id()}, null, null, null);
		cs.moveToFirst();
		int count=cs.getCount();
		cs.close();
		ContentValues cv=new ContentValues();
		cv.put(SQLiteHelper.PRIVATELISTTOUSERID, umodel.getUser_id());
		cv.put(SQLiteHelper.PRIVATELISTUSERID, model.getUser_id());
		cv.put(SQLiteHelper.PRIVATELISTUSERNAME, model.getUsername());
		cv.put(SQLiteHelper.PRIVATELISTAVATAR, model.getAvatar_large());
		cv.put(SQLiteHelper.PRIVATELISTCONTENT, model.getContent());
		cv.put(SQLiteHelper.PRIVATELISTTIME, ""+model.getTime());
		cv.put(SQLiteHelper.PRIVATELISTNOREADCOUNT, model.getNoReadCount());
		cv.put(SQLiteHelper.PRIVATELISTTYPE, model.getType());
		//�������ݣ�ֱ�Ӹ���
		if(count>0) {
			db.update(SQLiteHelper.PRIVATELISTTABLE, cv, SQLiteHelper.PRIVATELISTUSERID+"=? and "+SQLiteHelper.PRIVATELISTTOUSERID+"=?", new String[] {""+model.getUser_id(), ""+umodel.getUser_id()});
		}
		else {
			db.insert(SQLiteHelper.PRIVATELISTTABLE, null, cv);
		}
		db.close();
	}
	
	/**
	 * ��ϵ���б�δ����Ϣ����Ϊ0
	 * @param context
	 * @param user_id
	 */
	public synchronized void clearMessage(Context context, int user_id) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		ContentValues cv=new ContentValues();
		cv.put(SQLiteHelper.PRIVATELISTNOREADCOUNT, 0);
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		db.update(SQLiteHelper.PRIVATELISTTABLE, cv, SQLiteHelper.PRIVATELISTUSERID+"=? and "+SQLiteHelper.PRIVATELISTTOUSERID+"=?", new String[] {""+user_id, ""+umodel.getUser_id()});
		db.close();
	}
	
	/**
	 * ��ȡ������ϵ���б���Ϣ
	 * @param context
	 * @return
	 */
	public synchronized ArrayList<ReceiverPrivateLetterModel> getProvateMessageList(Context context) {
		ArrayList<ReceiverPrivateLetterModel> models=new ArrayList<ReceiverPrivateLetterModel>();
		UserModel umodel=DB.getInstance(context).getUserModel();
		if(umodel==null) {
			return models;
		}
		SQLiteDatabase db=this.mSQLiteHelper.getReadableDatabase();
		Cursor cs=db.query(SQLiteHelper.PRIVATELISTTABLE, null, SQLiteHelper.PRIVATELISTTOUSERID+"=?", new String[]{""+umodel.getUser_id()}, null, null, SQLiteHelper.PRIVATELISTTIME+" DESC");
		cs.moveToFirst();
		for(int i=0;i<cs.getCount();i++) {
			cs.moveToPosition(i);
			ReceiverPrivateLetterModel model=new ReceiverPrivateLetterModel();
			model.setAvatar_large(CommonUtils.convertNull(cs.getString(cs.getColumnIndex(SQLiteHelper.PRIVATELISTAVATAR))));
			model.setContent(cs.getString(cs.getColumnIndex(SQLiteHelper.PRIVATELISTCONTENT)));
			model.setTime(cs.getLong(cs.getColumnIndex(SQLiteHelper.PRIVATELISTTIME)));
			model.setUser_id(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELISTUSERID)));
			model.setUsername(cs.getString(cs.getColumnIndex(SQLiteHelper.PRIVATELISTUSERNAME)));
			model.setNoReadCount(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELISTNOREADCOUNT)));
			model.setType(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELISTTYPE)));
			models.add(model);
		}
		cs.close();
		db.close();
		return models;
	}
	
	/**
	 * �жϵ�ǰ˽���˱��Ƿ��Ѿ�����
	 * @param tableName
	 */
	private void checkUserDbExists(String tableName) {
		SQLiteDatabase db_r=this.mSQLiteHelper.getReadableDatabase();
		try {
			Cursor cs=db_r.rawQuery("select count(*) as c from "+SQLiteHelper.DBNAME+" where type='table' and name='"+tableName+"';", null);
			cs.moveToFirst();
			if(cs.moveToNext()) {
				int count=cs.getInt(0);
				cs.close();
				db_r.close();
				if(count==0) {
					SQLiteDatabase db_w=this.mSQLiteHelper.getWritableDatabase();
					db_w.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"("+SQLiteHelper.ID+" integer primary key AUTOINCREMENT, "+SQLiteHelper.PRIVATELETTERUSERID+" integer, "+SQLiteHelper.PRIVATELETTERTOUSERID+" integer, "+SQLiteHelper.PRIVATELETTERUSERNAME+" text, "+SQLiteHelper.PRIVATELETTERUSERIMAGE+" text, "+SQLiteHelper.PRIVATELETTERUSERCONTENT+" text, "+SQLiteHelper.PRIVATELETTERCONTENTTIME+" long, "+SQLiteHelper.PRIVATELETTERTO+" integer, "+SQLiteHelper.PRIVATELETTERTYPE+" integer, "+SQLiteHelper.PRIVATELETTERSUCCESS+" integer)");
					db_w.close();
				}
			}
		} catch (Exception e) {
			db_r.close();
			SQLiteDatabase db_w=this.mSQLiteHelper.getWritableDatabase();
			db_w.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"("+SQLiteHelper.ID+" integer primary key AUTOINCREMENT, "+SQLiteHelper.PRIVATELETTERUSERID+" integer, "+SQLiteHelper.PRIVATELETTERTOUSERID+" integer, "+SQLiteHelper.PRIVATELETTERUSERNAME+" text, "+SQLiteHelper.PRIVATELETTERUSERIMAGE+" text, "+SQLiteHelper.PRIVATELETTERUSERCONTENT+" text, "+SQLiteHelper.PRIVATELETTERCONTENTTIME+" long, "+SQLiteHelper.PRIVATELETTERTO+" integer, "+SQLiteHelper.PRIVATELETTERTYPE+" integer, "+SQLiteHelper.PRIVATELETTERSUCCESS+" integer)");
			db_w.close();
		}
		
	}
	
	/**
	 * �����û�˽�ż�¼
	 * @param model
	 */
	public synchronized void insertPrivateLetter(ArrayList<PrivateLetterModel> models, Context context) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		String tableName="";
		if(models.size()>0) {
			checkUserDbExists("db_"+models.get(0).getPrivate_letter_userid()+"_"+umodel.getUser_id());
			tableName="db_"+models.get(0).getPrivate_letter_userid()+"_"+umodel.getUser_id();
		}
		else {
			return ;
		}
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		for(int i=0;i<models.size();i++) {
			PrivateLetterModel model=models.get(i);
			ContentValues cv=new ContentValues();
			cv.put(SQLiteHelper.PRIVATELETTERUSERID, model.getPrivate_letter_userid());
			cv.put(SQLiteHelper.PRIVATELETTERTOUSERID, umodel.getUser_id());
			cv.put(SQLiteHelper.PRIVATELETTERUSERCONTENT, model.getPrivate_letter_usercontent());
			cv.put(SQLiteHelper.PRIVATELETTERCONTENTTIME, model.getPrivate_letter_contenttime());
			cv.put(SQLiteHelper.PRIVATELETTERTYPE, model.getPrivate_letter_type());
			//0���ظ����� 1���յ�����
			cv.put(SQLiteHelper.PRIVATELETTERTO, model.getPrivate_letter_to());
			cv.put(SQLiteHelper.PRIVATELETTERSUCCESS, ""+model.getPrivate_letter_success());
			db.insert(tableName, null, cv);
		}
		db.close();
	}
	
	/**
	 * �ϴ��ɹ���ɱ�־λ����
	 * @param userId
	 * @param imageUrl
	 * @param context
	 */
	public synchronized void updateSuccess(int userId, String imageUrl, Context context) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		checkUserDbExists("db_"+userId+"_"+umodel.getUser_id());
		String tableName="db_"+userId+"_"+umodel.getUser_id();
		SQLiteDatabase db=this.mSQLiteHelper.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(SQLiteHelper.PRIVATELETTERSUCCESS, "1");
		JSONObject obj=new JSONObject();
		try {
			obj.put("image", imageUrl);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.update(tableName, cv, SQLiteHelper.PRIVATELETTERUSERCONTENT+"=?", new String[]{obj.toString()});
		db.close();
	}
	
	/**
	 * ��ȡ��ǰ�Ի���userId�б�
	 * @param userId
	 * @param time
	 * @return
	 */
	public synchronized ArrayList<PrivateLetterModel> getPrivateLetterModel(int userId, long time, Context context) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		checkUserDbExists("db_"+userId+"_"+umodel.getUser_id());
		String tableName="db_"+userId+"_"+umodel.getUser_id();
		ArrayList<PrivateLetterModel> models=new ArrayList<PrivateLetterModel>();
		SQLiteDatabase db=this.mSQLiteHelper.getReadableDatabase();
		Cursor cs=null;
		if(time!=-1) {
			cs=db.query(tableName, null, SQLiteHelper.PRIVATELETTERCONTENTTIME+"<? and "+SQLiteHelper.PRIVATELETTERUSERID+"=? and "+SQLiteHelper.PRIVATELETTERTOUSERID+"=?", new String[]{""+time, ""+userId, ""+umodel.getUser_id()}, null, null, SQLiteHelper.PRIVATELETTERCONTENTTIME+" desc", "20");
		}
		else {
			cs=db.query(tableName, null, SQLiteHelper.PRIVATELETTERUSERID+"=? and "+SQLiteHelper.PRIVATELETTERTOUSERID+"=?", new String[]{""+userId, ""+umodel.getUser_id()}, null, null, SQLiteHelper.PRIVATELETTERCONTENTTIME+" desc", "20");
		}
		cs.moveToFirst();
		for(int i=cs.getCount()-1;i>=0;i--) {
			cs.moveToPosition(i);
			PrivateLetterModel model=new PrivateLetterModel();
			model.setPrivate_letter_userid(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERUSERID)));
			model.setPrivate_letter_usercontent(cs.getString(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERUSERCONTENT)));
			model.setPrivate_letter_contenttime(cs.getLong(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERCONTENTTIME)));
			model.setPrivate_letter_to(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERTO)));
			model.setPrivate_letter_type(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERTYPE)));
			model.setPrivate_letter_success(cs.getInt(cs.getColumnIndex(SQLiteHelper.PRIVATELETTERSUCCESS)));
			models.add(model);
		}
		cs.close();
		db.close();
		return models;
	}
	
	/**
	 * ���л�
	 * @param obj
	 * @return
	 */
	public byte[] serialize(Object obj){ 
        try { 
        	ByteArrayOutputStream mem_out=new ByteArrayOutputStream(); 
        	ObjectOutputStream out = new ObjectOutputStream(mem_out);  
        	out.writeObject((UserModel) obj);
            out.close(); 
            mem_out.close();  
            byte[] bytes=mem_out.toByteArray(); 
            return bytes; 
        } catch (IOException e) { 
            return null; 
        } 
    }
	
	/**
	 * �����л�
	 * @param bytes
	 * @return
	 */
	public UserModel deserializeUserModel(byte[] bytes){ 
		try { 
			ByteArrayInputStream mem_in=new ByteArrayInputStream(bytes); 
			ObjectInputStream in=new ObjectInputStream(mem_in);  
			UserModel model=(UserModel)in.readObject();  
			in.close(); 
			mem_in.close();  
			return model; 
		} catch (StreamCorruptedException e) { 

		} catch (ClassNotFoundException e) { 

		} catch (IOException e) { 
		
		} 
		return null; 
	}

}
