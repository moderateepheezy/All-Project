package com.renyu.alumni.common;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.io.PutExtra;
import com.renyu.alumni.R;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PostUploadModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class PostUploadManager {

	HashMap<String, PostUploadModel> uploadMaps=null;
	static Context context=null;

	private PostUploadManager() {
		uploadMaps=new HashMap<String, PostUploadModel>();
	}
	
	static PostUploadManager center=null;
	
	public static PostUploadManager getInstance(Context context_) {
		if(center==null) {
			center=new PostUploadManager();
			context=context_.getApplicationContext();
		}
		return center;
	}

	/**
	 * ����׼��
	 */
	public void start(PostUploadModel model) {
		if(model.getUploadList().size()>0) {
			String timeStamp=""+System.currentTimeMillis();
			uploadMaps.put(timeStamp, model);
			UploadNotificationManager.getNotificationCenter(context).createNewNotification(context, (int) Long.parseLong(timeStamp)/1000);
			for(int i=0;i<model.getUploadList().size();i++) {
				upload(new File(model.getUploadList().get(i)), timeStamp);
			}
		}
		else {
			uploadToServer("", model);
		}
	}
	
	/**
	 * �ϴ��ļ�
	 * @param file
	 * @param timeStamp
	 */
	private synchronized void upload(final File file, final String timeStamp) {
		if(!file.exists()) {
			return;
		}
		//�Զ�����key
		String key=IO.UNDEFINED_KEY; 
		//���ò���
		PutExtra extra=new PutExtra();
		extra.params=new HashMap<String, String>();
		extra.params.put("timeStamp", timeStamp);
		IO.putFile(ParamsManager.uptoken, key, file, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				System.out.println(file.getName()+": "+current+"/"+total);
			}

			@Override
			public void onSuccess(JSONObject resp) {
				System.out.println("�ϴ��ɹ�! "+resp);
				PostUploadModel model=uploadMaps.get(timeStamp);
				if(model==null) {
					return;
				}
				model.setFinish();
				String imageUrl="";
				try {
					imageUrl=resp.getString("hash");
					System.out.println(imageUrl);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.getUploadCompList().add(imageUrl);
				UploadNotificationManager.getNotificationCenter(context).updateNotification(context, (int) Long.parseLong(timeStamp)/1000, model.getFinishCount()*100/model.getUploadList().size());
				//������ɣ��ύ�������
				if(model.getFinishCount()==model.getUploadList().size()) {
					uploadToServer(timeStamp, null);
				}
			}

			@Override
			public void onFailure(Exception ex) {
				System.out.println("����: " + ex.getMessage());
				//���´���������
				PostUploadModel model=uploadMaps.get(timeStamp);
				if(model==null) {
					return;
				}
				model.setFinish();
				UploadNotificationManager.getNotificationCenter(context).updateNotification(context, (int) Long.parseLong(timeStamp)/1000, model.getFinishCount()*100/model.getUploadList().size());
				//������ɣ��ύ�������
				if(model.getFinishCount()==model.getUploadList().size()) {
					uploadToServer(timeStamp, null);
				}
			}
		});
	}
	
	/**
	 * �ύ�������
	 * @param timeStamp
	 * @param modelTemp ��ͼƬ����
	 */
	private void uploadToServer(final String timeStamp, final PostUploadModel modelTemp) {
		System.out.println("�ϴ�ȫ�����ݵ������");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel umodel=DB.getInstance(context).getUserModel();
		Security se=new Security();
		PostUploadModel model=null;
		if(modelTemp==null) {
			model=uploadMaps.get(timeStamp);
		}
		else {
			model=modelTemp;
		}
		if(model==null) {
			return;
		}
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "publish", umodel.getToken(), context);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
		JSONArray array_image=new JSONArray();
		for(int i=0;i<model.getUploadCompList().size();i++) {
			array_image.put(model.getUploadCompList().get(i));
		}
		JSONArray array_tag=new JSONArray();
		for(int i=0;i<model.getResource_tags().split("&").length;i++) {
			array_tag.put(model.getResource_tags().split("&")[i]);
		}
		JSONObject obj=new JSONObject();
		try {
			obj.put("publish_type", model.getPublish_type());
			obj.put("publish_author", model.getPublish_author());
			obj.put("resource_category", model.getResource_category());
			obj.put("resource_type", model.getResource_type());
			obj.put("resource_tags", array_tag);
			obj.put("images", array_image);
			obj.put("resource_title", model.getTitle());
			obj.put("resource_content", model.getContent());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/publish", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(context, new String(responseBody));
					
					Intent intent=new Intent("refreshContent");
					context.sendBroadcast(intent);
					
					EventBus.getDefault().post("refreshContent");
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(context, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(context, context.getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					//��ͼƬ��������Ҫ֪ͨ����ʾ
					if(modelTemp==null) {
						UploadNotificationManager.getNotificationCenter(context).showEndNotification(context, (int) Long.parseLong(timeStamp)/1000);
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj.toString());

	}
}
