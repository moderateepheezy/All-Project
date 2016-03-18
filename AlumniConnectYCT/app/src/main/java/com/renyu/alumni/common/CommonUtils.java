package com.renyu.alumni.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;
import com.renyu.alumni.R;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.CityModel;
import com.renyu.alumni.model.ImageChoiceModel;
import com.renyu.alumni.model.ImageUpdateModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.qqapi.QQActivity;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.sinaweiboapi.WBMainActivity;
import com.renyu.alumni.wxapi.SendWeixin;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
	
	/**
	 * ������ţ���͵���ͼƬ����֧��ͼƬ����
	 * @param context
	 * @param imagePath
	 */
	public static void prepareImage(final Context context, String imagePath, int imageState) {
		ImageUpdateModel models=new ImageUpdateModel();
		models.setActivity_id(context.getResources().getInteger(R.integer.avatar_activity_id));
		models.setActivity_state(imageState);
		ImageChoiceModel model=new ImageChoiceModel();
		model.setId(context.getResources().getInteger(R.integer.avatar_activity_picid));
		model.setPath(imagePath);
		model.setFlag(ImageChoiceModel.ADD);
		model.setContent("");
		ArrayList<ImageChoiceModel> modelList=new ArrayList<ImageChoiceModel>();
		modelList.add(model);
		models.setImageList(modelList);
		prepareImage(context, models);
	}

	/**
	 * ������ţ����ͼƬ
	 * @param map �ַ�����Σ��ϴ��ɹ���������������̨�����
	 */
	public static void prepareImage(final Context context, final ImageUpdateModel models) {
		
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				//׼���ϴ�ȫ������
				ImageUploadManager.getInstance().operImageUploadCenter(models, context);
				//��ȡȫ��Ҫ�ϴ����б�����
				ArrayList<ImageChoiceModel> uploadImageLists=models.getUploadImageLists();
				for(int i=0;i<uploadImageLists.size();i++) {
					HashMap<String, String> map=new HashMap<String, String>();
					//�ϴ�ͼƬ��id��Ϣ
					map.put("x:a", ""+uploadImageLists.get(i).getId());	
					//ִ���ϴ�����
					doUpload(context, new File(uploadImageLists.get(i).getPath()), map, models.getActivity_id());					
				}
			}
		};
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//��ȡȫ��������������
				ArrayList<ImageChoiceModel> imageList=models.getImageList();
				ArrayList<ImageChoiceModel> uploadImageList=new ArrayList<ImageChoiceModel>();
				for(int i=0;i<imageList.size();i++) {
					//�Դ��ϴ��ı���ͼƬ����ͼƬ����
					if(imageList.get(i).getPath().indexOf("http")==-1&&(imageList.get(i).getFlag()==ImageChoiceModel.ADD||imageList.get(i).getFlag()==ImageChoiceModel.UPDATE)) {
						String path=CommonUtils.getScalePicturePathName(imageList.get(i).getPath());
						imageList.get(i).setPath(path);
						//��ӽ�����ϴ��б���
						ImageChoiceModel uModel=imageList.get(i);
						uModel.setPath(path);
						uploadImageList.add(uModel);
					}	
					//ԭʼͼƬȥhttpͷ
					else if(imageList.get(i).getPath().indexOf("http")!=-1) {
						String path=imageList.get(i).getPath().substring(imageList.get(i).getPath().lastIndexOf("/")+1);
						imageList.get(i).setPath(path);
					}
				}
				models.setUploadImageLists(uploadImageList);
				handler.sendEmptyMessage(0);
			}}).start();
	}
	
	/**
	 * ͨ����ţִ���ļ��ϴ�
	 * @param uri
	 */
	public static synchronized void doUpload(final Context context, final File file, final HashMap<String, String> map, final int id) {
		if(!file.exists()) {
			return;
		}
		//�Զ�����key
		String key=IO.UNDEFINED_KEY; 
		//���ò���
		PutExtra extra=new PutExtra();
		extra.params=new HashMap<String, String>();
		Iterator<Entry<String, String>> it=map.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry=it.next();
			extra.params.put(entry.getKey(), entry.getValue());
		}
		IO.putFile(ParamsManager.uptoken, key, file, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				System.out.println(file.getName()+": "+current+"/"+total);
			}

			@Override
			public void onSuccess(JSONObject resp) {
				System.out.println("�ϴ��ɹ�! "+resp);
				//���´���������
				map.put("id", ""+id);
				String imageUrl="";
				try {
					imageUrl=resp.getString("hash");
					System.out.println(imageUrl);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("imageUrl", imageUrl);
				ImageUploadManager.getInstance().operImageUploadCenter(map, context);
			}

			@Override
			public void onFailure(Exception ex) {
				System.out.println("����: " + ex.getMessage());
				//���´���������
				map.put("id", ""+id);
				ImageUploadManager.getInstance().operImageUploadCenter(map, context);
				//�ϴ�ʧ�����ͳ��
				DB.getInstance(context).insertErrorImage(file.getPath(), map.get("x:a"));
			}
		});
	}
	
	/**
	 * ��ʼ����Ҫ���ļ���
	 */
	public static void loadDir() {
		String dirPath=Environment.getExternalStorageDirectory().getPath()+"/Alumni/Thumb";
		File file=new File(dirPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * ��ȡѹ��ͼƬ·��
	 * @param filename �ļ�·������
	 * @return
	 */
	public static String getScalePicturePathName(String filePath) {
		
	    try {
	    	BitmapFactory.Options opts=new BitmapFactory.Options();
	    	opts.inJustDecodeBounds=true;
	    	//��ʱ����bitmapΪ��
	    	Bitmap bitmap=BitmapFactory.decodeFile(filePath, opts);
	    	opts.inJustDecodeBounds=false;
            int srcWidth=opts.outWidth;
            int srcHeight=opts.outHeight;
            //���ԭʼͼƬ�Ŀ��
            if(srcWidth<1000||srcHeight<1000) {
            	return filePath;
            }
            // ���ű���
            double ratio=8;
            // ���������ȡ��߶�
            BitmapFactory.Options newOpts=new BitmapFactory.Options();
            newOpts.inSampleSize=(int) (ratio);
            newOpts.inJustDecodeBounds=false;
            newOpts.outWidth=(int) (srcWidth*ratio);
            newOpts.outHeight=(int) (srcHeight*ratio);
            Matrix matrix=new Matrix();;  
            matrix.postRotate(readPictureDegree(filePath));  
            // �����µ�ͼƬ   
            bitmap=BitmapFactory.decodeFile(filePath, newOpts);
            bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
            
            //������ͼƬ
            String dirPath=Environment.getExternalStorageDirectory().getPath()+"/Alumni/Thumb";
            File cameraFile=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
            FileOutputStream fos=null; 
            if (!cameraFile.exists()) { 
        		cameraFile.createNewFile();
        	} 
        	fos=new FileOutputStream(cameraFile); 
        	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); 
        	bitmap.recycle();
        	bitmap=null;
        	return cameraFile.getPath();
	    } catch (Exception e) {
            // TODO: handle exception
	    	e.printStackTrace();
	    	return filePath;
        }
    }
	
	/** 
	 * ��ȡͼƬ���ԣ���ת�ĽǶ� 
	 * @param path ͼƬ����·�� 
	 * @return degree��ת�ĽǶ� 
	 */  
	public static int readPictureDegree(String path) {  
		int degree  = 0;  
	    try {  
	    	ExifInterface exifInterface=new ExifInterface(path);  
	    	int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	    	switch (orientation) {  
	    		case ExifInterface.ORIENTATION_ROTATE_90:  
	    			degree=90;  
	    			break;  
	    		case ExifInterface.ORIENTATION_ROTATE_180:  
	    			degree=180;  
	                break;  
	            case ExifInterface.ORIENTATION_ROTATE_270:  
	            	degree = 270;  
	            	break;  
           }  
	    } catch (IOException e) {  
	    	e.printStackTrace();  
	    }  
	    return degree;  
	} 
	
	/**
	 * �Զ����б���AlertDialog
	 * @param context
	 * @param message �б�����
	 */
  	public static void showCustomAlertDialog(Context context, String[] message, int[] image, OnDialogItemClickListener lis) {
  		MyChoiceLodingDialog dialog=new MyChoiceLodingDialog(context, R.style.MyListDialog, message, image, lis);
  		//dialogλ���ڵײ�
  		dialog.getWindow().setGravity(Gravity.BOTTOM);  		
  		//���õ��Dialog�ⲿ��������ر�Dialog
  		dialog.setCanceledOnTouchOutside(true);
  		dialog.show();
  	}
  	
  	/**
  	 * �Զ���AlertDialog
  	 * @param context
  	 */
  	public static MyLoadingDialog showCustomAlertProgressDialog(Context context, String message) {
  		MyLoadingDialog dialog=new MyLoadingDialog(context, R.style.MyLoadingDialog, message);
  		//���õ��Dialog�ⲿ��������ر�Dialog
  		dialog.setCanceledOnTouchOutside(false);
  		dialog.show();  		
  		return dialog;
  	}
  	
  	/**
  	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)  
  	 * @param context
  	 * @param dpValue
  	 * @return
  	 */
    public static int dip2px(Context context, float dpValue) {  
        final float scale=context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue*scale+0.5f);  
    }  
  
    /**
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp   
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {  
        final float scale=context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue/scale+0.5f);  
    }  
    
    /**
     * ����ʱ�䷴��ʱ���
     * @param time
     * @return
     */
    public static long getTimeFormat(String time) {
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date date;
		try {
			date = format.parse(time);
	    	return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    }
    
    /**
     * ����ʱ�����ȡʱ���ַ���
     * @param time
     * @return
     */
    public static String getTimeFormat(long time) {
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	return format.format(new Date(time));
    }
    
    /**
     * ����ʱ�����ȡʱ��map
     * @param time
     * @return
     */
    public static HashMap<String, String> getTimeFormatMap(long time) {
    	HashMap<String, String> map=new HashMap<String, String>();
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    	String str=format.format(new Date(time));
    	map.put("year", str.split("-")[0]);
    	map.put("month", str.split("-")[1]);
    	map.put("day", str.split("-")[2]);
    	map.put("hour", str.split("-")[3]);
    	map.put("minute", str.split("-")[4]);
    	return map;
    }
    
    /**
     * ��ȡ����Ƶ��ʱ���ֵ
     * @param time
     * @return
     */
    public static String getPulicIndexTimeExtra(long time) {
    	long currentTime=System.currentTimeMillis();
    	int sec=(int) ((currentTime-time)/1000);
    	if(sec<=60) {
    		return sec+"��ǰ";
    	}
    	else if(sec>60&&sec<=60*60) {
    		return sec/60+"����ǰ";
    	}
    	else if(sec>60*60&&sec<=60*60*24) {
    		return sec/(60*60)+"Сʱǰ";
    	}
    	else if(sec>60*60*24) {
    		return sec/(60*60*24)+"��ǰ";
    	}
    	else {
    		return "";
    	}
    }
    
    /**
     * ˽����Ա�б�ʱ��
     * @param time
     * @return
     */
    public static String getPrivateMessageCenterTimeFormat(long time) {
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    	String str=format.format(new Date(time));
    	long currentTime=ParamsManager.extratime+System.currentTimeMillis();
    	String currentStr=format.format(new Date(currentTime));
    	int month=Integer.parseInt(str.split("-")[1]);
    	int currentMonth=Integer.parseInt(currentStr.split("-")[1]);
    	int day=Integer.parseInt(str.split("-")[2]);
    	int currentDay=Integer.parseInt(currentStr.split("-")[2]);
    	
    	if(month!=currentMonth) {
			return str.split("-")[1]+"-"+str.split("-")[2];
		}
		else {
			if(day!=currentDay) {
				if(currentDay-day==1) {
					return "����";
				}
				else if(currentDay-day==2) {
					return "ǰ��";
				}
				else {
					return str.split("-")[1]+"-"+str.split("-")[2];
				}
			}
			else {
				return str.split("-")[3]+":"+str.split("-")[4];
			}
		}
    }
    
    /**
     * ˽��ʱ��
     * @param time
     * @return
     */
    public static String getPrivateMessageTimeFormat(long time) {
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    	String str=format.format(new Date(time));
    	long currentTime=ParamsManager.extratime+System.currentTimeMillis();
    	String currentStr=format.format(new Date(currentTime));

    	int year=Integer.parseInt(str.split("-")[0]);
    	int currentYear=Integer.parseInt(currentStr.split("-")[0]);
    	int month=Integer.parseInt(str.split("-")[1]);
    	int currentMonth=Integer.parseInt(currentStr.split("-")[1]);
    	int day=Integer.parseInt(str.split("-")[2]);
    	int currentDay=Integer.parseInt(currentStr.split("-")[2]);
    	if(year!=currentYear) {
    		if(currentYear-year==1) {
    			return "ȥ�� "+str.split("-")[1]+"-"+str.split("-")[2]+" "+str.split("-")[3]+":"+str.split("-")[4];
    		}
    		else if(currentYear-year==2) {
    			return "ǰ�� "+str.split("-")[1]+"-"+str.split("-")[2]+" "+str.split("-")[3]+":"+str.split("-")[4];
    		}
    		else {
    			return getTimeFormat(time);
    		}
    	}
    	else {
    		if(month!=currentMonth) {
    			return str.split("-")[1]+"-"+str.split("-")[2]+" "+str.split("-")[3]+":"+str.split("-")[4];
    		}
    		else {
    			if(day!=currentDay) {
    				if(currentDay-day==1) {
    					return "���� "+str.split("-")[3]+":"+str.split("-")[4];
    				}
    				else if(currentDay-day==2) {
    					return "ǰ�� "+str.split("-")[3]+":"+str.split("-")[4];
    				}
    				else {
    					return str.split("-")[1]+"-"+str.split("-")[2]+" "+str.split("-")[3]+":"+str.split("-")[4];
    				}
    			}
    			else {
    				return str.split("-")[3]+":"+str.split("-")[4];
    			}
    		}
    	}
    }
    
    /**
  	 * ��ȡƴ������ĸ
  	 * @param chinese
  	 * @return
  	 */
  	private static String getFirstSpell(String chinese) {   
        StringBuffer pybf = new StringBuffer();   
        char[] arr = chinese.toCharArray();   
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
        for (int i = 0; i < arr.length; i++) {   
            if (arr[i] > 128) {   
                try {   
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
                    if (temp != null) {   
                            pybf.append(temp[0].charAt(0));   
                    }   
                } catch (BadHanyuPinyinOutputFormatCombination e) {   
                        e.printStackTrace();   
                }   
            } else {   
                    pybf.append(arr[i]);   
            }   
        }   
        return pybf.toString().replaceAll("\\W", "").trim();   
  	}
    
    /**
	 * ���������ݿ⸴��
	 * @param sqliteName
	 */
	public static void copyDbFile(Context context) {
		//�������ݿ�
		File file=new File("/data/data/"+context.getPackageName()+"/ChinaArea.db");
		if(!file.exists()) {
			CommonUtils.copyAssetsFile("ChinaArea.db", file.getPath(), context);
		}
		//�������ݿ�
		File file_international=new File("/data/data/"+context.getPackageName()+"/InternationArea.db");
		if(!file_international.exists()) {
			CommonUtils.copyAssetsFile("InternationArea.db", file_international.getPath(), context);
		}
	}
	
	/**
     * ͨ��assets�����ļ�
     * @param oldName
     * @param newPath
     * @param context
     */
    private static void copyAssetsFile(String oldName, String newPath, Context context) {
    	AssetManager manager=context.getAssets();
    	try {
    		int bytesum=0; 
    		int byteread=0; 
			InputStream inStream=manager.open(oldName);
			FileOutputStream fs=new FileOutputStream(newPath); 
			byte[] buffer=new byte[1444]; 
			while ((byteread = inStream.read(buffer))!=-1) { 
				bytesum+=byteread; 
				fs.write(buffer, 0, byteread); 
			} 
			inStream.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * ԭʼ���ݿ�����
     * @param context
     */
//    private static void runDb(Context context) {
//    	File file=new File("/data/data/"+context.getPackageName()+"/ChinaArea.db");
//		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
//		Cursor cs=db.query("city_config", null, null, null, null, null, null);
//		cs.moveToFirst();
//		for(int i=0;i<cs.getCount();i++) {
//			cs.moveToPosition(i);
//			String city_name=cs.getString(cs.getColumnIndex("city_name"));
//			String letter=getFirstSpell(city_name).substring(0, 1);
//			ContentValues cv=new ContentValues();
//			cv.put("letter", letter);
//			db.update("city_config", cv, "city_name=?", new String[]{city_name});
//		}
//		cs.close();
//		db.close();
//    }
    
    /**
     * ��ȡ���ݿ���ȫ�����ʳ�����Ϣ
     * @param context
     * @param mapModel ÿ��section��Ԫ��
     * @param sectionsList ����section
     * @param cityAlls ����section������Ԫ��
     */
    public static void getAllInternationCity(Context context, LinkedHashMap<String, ArrayList<CityModel>> mapModel, ArrayList<String> sectionsList, ArrayList<CityModel> cityAlls, ArrayList<String> cityAllsWithString) {
    	File file=new File("/data/data/"+context.getPackageName()+"/InternationArea.db");
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
		Cursor cs=db.query("city", null, null, null, null, null, "pyshot asc");
		cs.moveToFirst();
		for(int i=0;i<cs.getCount();i++) {
			cs.moveToPosition(i);
			CityModel model=new CityModel();
			model.setCityCode(cs.getString(cs.getColumnIndex("cityid")));
			model.setCityName(cs.getString(cs.getColumnIndex("cityname")));
			ArrayList<CityModel> models=null;
			if(!mapModel.containsKey(cs.getString(cs.getColumnIndex("pyshot")))) {				
				sectionsList.add(cs.getString(cs.getColumnIndex("pyshot")));
				
				models=new ArrayList<CityModel>();
				
				cityAlls.add(null);
				cityAllsWithString.add(cs.getString(cs.getColumnIndex("pyshot")));
			}
			else {
				models=mapModel.get(cs.getString(cs.getColumnIndex("pyshot")));
			}
			models.add(model);
			mapModel.put(cs.getString(cs.getColumnIndex("pyshot")), models);
			
			cityAlls.add(model);
			cityAllsWithString.add(cs.getString(cs.getColumnIndex("cityname")));
		}
		cs.close();
		db.close();
    }
    
    /**
     * ��ȡ���ݿ���ȫ������������Ϣ
     * @param context
     * @param mapModel ÿ��section��Ԫ��
     * @param sectionsList ����section
     * @param cityAlls ����section������Ԫ��
     */
    public static void getAllCity(Context context, LinkedHashMap<String, ArrayList<CityModel>> mapModel, ArrayList<String> sectionsList, ArrayList<CityModel> cityAlls, ArrayList<String> cityAllsWithString) {
    	File file=new File("/data/data/"+context.getPackageName()+"/ChinaArea.db");
		SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
		Cursor cs=db.query("city_config", null, null, null, null, null, "letter asc");
		cs.moveToFirst();
		for(int i=0;i<cs.getCount();i++) {
			cs.moveToPosition(i);
			CityModel model=new CityModel();
			model.setCityCode(cs.getString(cs.getColumnIndex("city_code")));
			model.setCityName(cs.getString(cs.getColumnIndex("city_name")));
			ArrayList<CityModel> models=null;
			if(!mapModel.containsKey(cs.getString(cs.getColumnIndex("letter")))) {				
				sectionsList.add(cs.getString(cs.getColumnIndex("letter")));
				
				models=new ArrayList<CityModel>();
				
				cityAlls.add(null);
				cityAllsWithString.add(cs.getString(cs.getColumnIndex("letter")));
			}
			else {
				models=mapModel.get(cs.getString(cs.getColumnIndex("letter")));
			}
			models.add(model);
			mapModel.put(cs.getString(cs.getColumnIndex("letter")), models);
			
			cityAlls.add(model);
			cityAllsWithString.add(cs.getString(cs.getColumnIndex("city_name")));
		}
		cs.close();
		db.close();
    }
    
    /**
     * �ж��ֻ���ʽ�Ƿ���ȷ
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	Matcher m = p.matcher(mobiles);
    	return m.matches();
    }
            
    /**
     * �ж�email��ʽ�Ƿ���ȷ
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
    	String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    	Pattern p = Pattern.compile(str);
    	Matcher m = p.matcher(email);
    	return m.matches();
    }
    
    /**
     * �ж��Ƿ�ȫ������
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
    	Pattern pattern = Pattern.compile("[0-9]*");
    	Matcher isNum = pattern.matcher(str);
    	if (!isNum.matches()) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * ��ʾ�Զ����toast
     * @param context
     * @param str
     */
    public static void showCustomToast(Context context, String str, boolean isOK) {
    	View view=LayoutInflater.from(context).inflate(R.layout.alertdialog_message_layout, null);
    	ImageView dialog_img=(ImageView) view.findViewById(R.id.dialog_img);
    	dialog_img.setImageResource(R.drawable.toast_ok_icon);
    	TextView dialog_message=(TextView) view.findViewById(R.id.dialog_message);
    	dialog_message.setText(str);
    	Toast toast=new Toast(context.getApplicationContext());
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.setDuration(3000);
    	toast.setView(view);
    	toast.show();
    }
    
    public static String convertNull(String returnValue) {
        try {
            returnValue = (returnValue==null||(returnValue!=null&&returnValue.equals("null")))?"":returnValue;
        } catch (Exception e) {
            returnValue = "";
        }
        return returnValue;
    }
    
    /**
     * qq����
     * @param context
     * @param text
     * @param imageUrl
     * @param title
     * @param url
     */
    public static void shareQQ(Context context, String text, String imageUrl, String title, String url) {
    	Intent intent=new Intent(context, QQActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("title", title);
		bundle.putString("url", url);
		bundle.putString("text", text);
		bundle.putString("send_imageUrl", imageUrl);
		bundle.putString("type", "qq");
		intent.putExtras(bundle);
		context.startActivity(intent);
    }
    
    /**
     * qq�ռ����
     * @param context
     * @param text
     * @param imageUrl
     * @param title
     * @param url
     */
    public static void shareQQKJ(Context context, String text, String imageUrl, String title, String url) {
    	Intent intent=new Intent(context, QQActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("title", title);
		bundle.putString("url", url);
		bundle.putString("text", text);
		bundle.putString("send_imageUrl", imageUrl);
		bundle.putString("type", "qqkj");
		intent.putExtras(bundle);
		context.startActivity(intent);
    }
    
    /**
     * ΢�ŷ���
     * @param context
     * @param text
     * @param imageUrl
     * @param title
     * @param url
     */
    public static void shareWeixin(Context context, String text, String title, String imagePath, String url) {
    	SendWeixin weixin=new SendWeixin();
		weixin.sendWeixin(context, text, url, title, false);
    }
    
    /**
     * ΢������Ȧ����
     * @param context
     * @param text
     * @param imageUrl
     * @param title
     * @param url
     */
    public static void shareWeixinPy(Context context, String text, String title, String imagePath, String url) {
    	SendWeixin weixin=new SendWeixin();
		weixin.sendWeixin(context, text, url, title, true);
    }
    
    /**
     * ����΢������
     * @param context
     * @param text
     * @param imageUrl
     * @param title
     * @param url
     */
    public static void shareWeibo(Context context, String text, String imageUrl, String title, String url) {
    	Intent intent=new Intent(context, WBMainActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("title", title);
		bundle.putString("url", url);
		bundle.putString("text", text);
		bundle.putString("defaultText", "�ƴ���");
		intent.putExtras(bundle);
		context.startActivity(intent);
    }
    
    /**
     * �õ���Ļ���
     * @return ��λ:px
     */
    public static int getScreenWidth(Context context) {
        int screenWidth;
        WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        return screenWidth;
    }
     
    /**
     * �õ���Ļ�߶�
     * @return ��λ:px
     */
    public static int getScreenHeight(Context context) {
        int screenHeight;
        WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight=dm.heightPixels;
        return screenHeight;
    }
    
    /**
     * �Ÿ�ע������
     * @param context
     * @param alias
     */
    public static void registXG(final Context context, final String alias) {
    	XGPushManager.registerPush(context.getApplicationContext(), alias, new XGIOperateCallback() {

			@Override
			public void onFail(Object arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				//������������ע����û�Ϊ��ǰ��¼���û�����ע��������ܳ���3��
				UserModel model=DB.getInstance(context).getUserModel();
				if(model!=null&&model.getToken().equals(alias)&&ParamsManager.XGLoadTimes<3) {
					ParamsManager.XGLoadTimes++;
					registXG(context, alias);
				}
			}

			@Override
			public void onSuccess(Object arg0, int arg1) {
				// TODO Auto-generated method stub
				System.out.println("ע��ɹ�");
			}});
    }
    
    /**
     * �жϵ�ǰ����״̬�Ƿ���ͨ
     * @param context
     * @return
     */
    public static boolean showNoNetworkInfo(Context context) {
    	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=connectivityManager.getActiveNetworkInfo();  
        if(info!=null&&info.isAvailable()) {
            return true;
        } 
        else {
            System.out.println("û����������");
            return false;
        }
    }
    
    /**
	 * �жϷ����Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isServiceWorked(Context context, String serviceName) {  
		ActivityManager myManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(100);  
		for(int i = 0 ; i<runningService.size();i++) {  
			System.out.println(runningService.get(i).service.getClassName().toString());
			if(runningService.get(i).service.getClassName().toString().equals(serviceName)) {  
				return true;  
			}  
		}  
		return false;  
	}
	
	/**
	 * �Ƿ�ǰ̨Ϊ�ƴ���Ӧ��
	 * @param context
	 * @return
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);  
		RunningTaskInfo cinfo = runningTasks.get(0);  
		ComponentName component = cinfo.topActivity;  
		if("com.renyu.alumni".equals(component.getPackageName())) {
			return true;  
		}
		return false; 
	}
	
	public static void sendComp(Context context, int shareType, int classId) {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		final UserModel model=DB.getInstance(context).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "classcallshare", model.getToken(), context);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/classcallshare?class_id="+classId+"&share_type="+shareType, headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * �ж��Ƿ�����
	 * @param context
	 * @return
	 */
	public static boolean checkIsScreenOn(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	/**
	 * ����Ԥ�����
	 * @param context
	 */
	public static void setShowIntroduce(Context context) {
		SharedPreferences sp=context.getSharedPreferences("alumni", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		editor.putBoolean("showintro", true);
		editor.commit();
	}
	
	/**
	 * �ж��Ƿ����Ԥ��
	 * @param context
	 * @return
	 */
	public static boolean getShowInterface(Context context) {
		SharedPreferences sp=context.getSharedPreferences("alumni", Activity.MODE_PRIVATE);
		return sp.getBoolean("showintro", false);
	}
	
	/**
	 * �ж�app�Ƿ�װ
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packageName) {  
        final PackageManager packageManager=context.getPackageManager();  
        List<PackageInfo> pinfo=packageManager.getInstalledPackages(0);  
        List<String> pName=new ArrayList<String>();  
        if (pinfo != null) {  
            for (int i=0;i<pinfo.size();i++) {  
                String pn=pinfo.get(i).packageName;  
                pName.add(pn);  
            }  
        }  
        return pName.contains(packageName);  
    }
	
	/**
     * ��ȡ״̬���߶�
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
    	Rect frame=new Rect();  
    	activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
    	return frame.top;  
    }
    
    /**
     * ��ȡ���ͼƬ·��
     * @param uri
     * @param context
     * @return
     */
    public static String getPath(final Uri uri, final Context context) {  
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
            if (isExternalStorageDocument(uri)) {  
                final String docId = DocumentsContract.getDocumentId(uri);  
                final String[] split = docId.split(":");  
                final String type = split[0];  
      
                if ("primary".equalsIgnoreCase(type)) {  
                    return Environment.getExternalStorageDirectory() + "/" + split[1];  
                }  
            }  
            else if (isDownloadsDocument(uri)) {  
                final String id = DocumentsContract.getDocumentId(uri);  
                final Uri contentUri = ContentUris.withAppendedId(  
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
                return getDataColumn(context, contentUri, null, null);  
            }  
            else if (isMediaDocument(uri)) {  
                final String docId = DocumentsContract.getDocumentId(uri);  
                final String[] split = docId.split(":");  
                final String type = split[0];  
                Uri contentUri = null;  
                if ("image".equals(type)) {  
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                } 
                else if ("video".equals(type)) {  
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                } 
                else if ("audio".equals(type)) {  
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                }  
                final String selection = "_id=?";  
                final String[] selectionArgs = new String[] {  
                        split[1]  
                };  
                return getDataColumn(context, contentUri, selection, selectionArgs);  
            }  
        }  
        else if ("content".equalsIgnoreCase(uri.getScheme())) {  
            if (isGooglePhotosUri(uri))  
                return uri.getLastPathSegment();  
            return getDataColumn(context, uri, null, null);  
        }  
        else if ("file".equalsIgnoreCase(uri.getScheme())) {  
            return uri.getPath();  
        }  
        return null;  
    }  
    
    private static String getDataColumn(Context context, Uri uri, String selection,  
            String[] selectionArgs) {  
        Cursor cursor = null;  
        final String column = "_data";  
        final String[] projection = {  
                column  
        };  
        try {  
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                    null);  
            if (cursor != null && cursor.moveToFirst()) {  
                final int index = cursor.getColumnIndexOrThrow(column);  
                return cursor.getString(index);  
            }  
        } finally {  
            if (cursor != null)  
                cursor.close();  
        }  
        return null;  
    }  
    
    private static boolean isExternalStorageDocument(Uri uri) {  
        return "com.android.externalstorage.documents".equals(uri.getAuthority());  
    }  

    private static boolean isDownloadsDocument(Uri uri) {  
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
    }  

    private static boolean isMediaDocument(Uri uri) {  
        return "com.android.providers.media.documents".equals(uri.getAuthority());  
    }  

    private static boolean isGooglePhotosUri(Uri uri) {  
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
    }
    
    public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
