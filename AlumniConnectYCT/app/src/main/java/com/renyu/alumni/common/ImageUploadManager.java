package com.renyu.alumni.common;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.ImageChoiceModel;
import com.renyu.alumni.model.ImageUpdateModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ImageUploadManager {
	
	private HashMap<Integer, ImageUpdateModel> uploadMaps=null;

	private ImageUploadManager() {
		uploadMaps=new HashMap<Integer, ImageUpdateModel>();
	}
	
	static ImageUploadManager center=null;
	
	public static ImageUploadManager getInstance() {
		if(center==null) {
			center=new ImageUploadManager();
		}
		return center;
	}
	
	/**
	 * �ж��Ƿ��Ѿ��ڷ���������
	 * @param id
	 * @return
	 */
	public boolean containsActivityId(int id) {
		if(uploadMaps.containsKey(id)) {
			return true;
		}
		return false;
	}
	
	public void operImageUploadCenter(Object obj, Context context) {
		synchronized (uploadMaps) {
			//������+1
			if(obj instanceof HashMap) {
				HashMap<String, String> map=(HashMap<String, String>) obj;
				ImageUpdateModel model=uploadMaps.get(Integer.parseInt(map.get("id")));
				int count=model.getFinishCount();
				model.setFinishCount(count+1);
				//��ȡ����ͼƬ��id
				int id=Integer.parseInt(map.get("x:a"));
				String path="";
				//ͼƬ�ϴ��ɹ�����ԭͼƬ��ַ������ţ��url��ʧ����Ϊ��
				if(map.containsKey("imageUrl")) {
					path=map.get("imageUrl");
				}
				for(int i=0;i<model.getImageList().size();i++) {
					if(model.getImageList().get(i).getId()==id) {
						model.getImageList().get(i).setPath(path);
					}
				}
				UploadNotificationManager.getNotificationCenter(context).updateNotification(context, model.getActivity_id(), model.getFinishCount()*100/model.getUploadImageLists().size());
				System.out.println("��������");
				if(model.getFinishCount()==model.getUploadImageLists().size()) {
					uploadData(uploadMaps.remove(Integer.parseInt(map.get("id"))), context);
				}
			}
			//������ϴ��ı���ͼƬ����
			else if(obj instanceof ImageUpdateModel) {
				ImageUpdateModel model=(ImageUpdateModel) obj;
				UploadNotificationManager.getNotificationCenter(context).createNewNotification(context, model.getActivity_id());
				//�������ڴ��ϴ�����ʱ��ֱ���ύ
				if(model.getUploadImageLists().size()==0) {
					uploadData(model, context);
					return;
				}
				if(!uploadMaps.containsKey(model.getActivity_id())) {
					uploadMaps.put(model.getActivity_id(), model);
					System.out.println("�������");
				}
				else {
					return ;
				}
			}
		}		
	}
	
	/**
	 * �ϴ�ȫ�����ݵ������
	 * @param model
	 */
	private void uploadData(final ImageUpdateModel model, final Context context) {
		System.out.println("�ϴ�ȫ�����ݵ������");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel umodel=DB.getInstance(context).getUserModel();
		Security se=new Security();
		RequestParams params=new RequestParams();
		UploadNotificationManager.getNotificationCenter(context).cancelNotification(context, model.getActivity_id());
		//�������༭�
		if(model.getActivity_state()==0||model.getActivity_state()==1) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "add", umodel.getToken(), context);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			try {
				JSONArray array=new JSONArray();
				for(int i=0;i<model.getImageList().size();i++) {
					ImageChoiceModel model_=model.getImageList().get(i);
					JSONObject obj=new JSONObject();
					obj.put("pic_id", model_.getId());
					obj.put("pic_url", model_.getPath());
					obj.put("pic_state", model_.getFlag());
					obj.put("pic_desc", model_.getContent());
					array.put(obj);
				}
				JSONObject obj=new JSONObject();
				obj.put("images", array);
				obj.put("activity_id", model.getActivity_id());
				obj.put("activity_name", model.getActivity_name());
				obj.put("begin_time", model.getBegin_time());
				obj.put("end_time", model.getEnd_time());
				obj.put("address", model.getAddress());
				obj.put("apply_time", model.getApply_time());
				obj.put("create_org", model.getCreate_org());
				obj.put("activity_desc", model.getActivity_desc());
				System.out.println(obj.toString());

				ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
				client.post(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/activity/add", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						UploadNotificationManager.getNotificationCenter(context).showEndNotification(context, model.getActivity_id());
						JsonParse.showMessage(context, new String(responseBody));
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
					}});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//�������༭����
		else if(model.getActivity_state()==5||model.getActivity_state()==7) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "addhis", umodel.getToken(), context);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			try {
				JSONArray array=new JSONArray();
				for(int i=0;i<model.getImageList().size();i++) {
					ImageChoiceModel model_=model.getImageList().get(i);
					JSONObject obj=new JSONObject();
					obj.put("pic_id", model_.getId());
					obj.put("pic_url", model_.getPath());
					obj.put("pic_state", model_.getFlag());
					obj.put("pic_desc", model_.getContent());
					array.put(obj);
				}
				JSONObject obj=new JSONObject();
				obj.put("images", array);
				obj.put("activity_id", model.getActivity_id());
				obj.put("his_desc", model.getHis_desc());				
				System.out.println(obj.toString());

				ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
				client.post(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/activity/addhis", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						JsonParse.showMessage(context, new String(responseBody));
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
					}});
			} catch(Exception e) {
				
			}
		}
		//�ϴ�ͷ��
		else if(model.getActivity_state()==context.getResources().getInteger(R.integer.avatar_activity_state)) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "upload", umodel.getToken(), context);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			String imageUrl="";
			if(model.getImageList().size()>0) {
				imageUrl=model.getImageList().get(0).getPath();
			}
			if(imageUrl.equals("")) {
				return ;
			}
			params.add("avatar_large", imageUrl);
			final String imageUrl_=imageUrl;
			client.get(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/upload", headers, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(context, new String(responseBody));
					UserModel umodel=DB.getInstance(context).getUserModel();
					umodel.setAvatar_large(imageUrl_);
					DB.getInstance(context).updateUserAvatar(umodel);
					
					Intent intent=new Intent();
					intent.setAction("IMAGEUPDATE");
					context.sendBroadcast(intent);
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
			});
		}
		//����˽���ϴ�ͼƬ
		else if(model.getActivity_state()==context.getResources().getInteger(R.integer.topic_message_image)) {
			
		}
	}
	
	public HashMap<Integer, ImageUpdateModel> getUploadMaps() {
		HashMap<Integer, ImageUpdateModel> uploadMaps_=new HashMap<Integer, ImageUpdateModel>(uploadMaps);
		return uploadMaps_;
	}
}
