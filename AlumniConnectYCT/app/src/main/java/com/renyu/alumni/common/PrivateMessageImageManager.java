package com.renyu.alumni.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class PrivateMessageImageManager {
	
	private static PrivateMessageImageManager manager=null;
	static Context context=null;
	
	private PrivateMessageImageManager() {
		
	}
	
	public synchronized static PrivateMessageImageManager getInstance(Context context) {
		if(manager==null) {
			manager=new PrivateMessageImageManager();
			PrivateMessageImageManager.context=context.getApplicationContext();
		}
		return manager;
	}

	public void start(final String imagePath) {
		final File file=new File(CommonUtils.getScalePicturePathName(imagePath));
		if(!file.exists()) {
			sendBroadCast(false, null, imagePath);
			return;
		}
		//�Զ�����key
		String key=IO.UNDEFINED_KEY; 
		//���ò���
		PutExtra extra=new PutExtra();
		extra.params=new HashMap<String, String>();
		IO.putFile(ParamsManager.uptoken, key, file, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				System.out.println(file.getName()+": "+current+"/"+total);
			}

			@Override
			public void onSuccess(JSONObject resp) {
				System.out.println("�ϴ��ɹ�! "+resp);
				String imageUrl="";
				try {
					imageUrl=resp.getString("hash");
					sendBroadCast(true, imageUrl, imagePath);
					System.out.println(imageUrl);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendBroadCast(false, null, imagePath);
				}
				
			}

			@Override
			public void onFailure(Exception ex) {
				System.out.println("����: " + ex.getMessage());
				sendBroadCast(false, null, imagePath);
			}
		});
	}
	
	/**
	 * �ϴ��ļ����������͹㲥
	 * @param success
	 * @param imageUrl
	 */
	private void sendBroadCast(boolean success, String imageUrl, String tag) {
		Intent intent=new Intent("PRIVATELETTERUPLOAD");
		Bundle bundle=new Bundle();
		bundle.putString("imageUrl", CommonUtils.convertNull(imageUrl));
		bundle.putBoolean("success", success);
		bundle.putString("tag", tag);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}
}
