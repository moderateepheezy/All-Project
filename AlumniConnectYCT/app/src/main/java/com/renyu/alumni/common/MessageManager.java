package com.renyu.alumni.common;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.PrivateLetterModel;
import com.renyu.alumni.model.ReceiverPrivateLetterModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.receiver.CustomPushReceiver;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MessageManager {
	
	//��ǰȫ����Ϣmap
	private static LinkedHashMap<Integer, ReceiverPrivateLetterModel> messageMap=null;
	
	private static MessageManager instance=null;
	private static Context context=null; 
	
	private MessageManager() {
		
	}
	
	public static synchronized MessageManager getInstance(Context context_) {
		if(instance==null) {
			instance=new MessageManager();
			messageMap=new LinkedHashMap<Integer, ReceiverPrivateLetterModel>();
			context=context_.getApplicationContext();
		}
		return instance;
	}
	
	/**
	 * ������Ϣ����
	 */
	public void startMessageUser() {
		
		messageMap.clear();
		ArrayList<ReceiverPrivateLetterModel> all=DB.getInstance(context).getProvateMessageList(context);
		if(all==null) {
			return;
		}
		for(int i=0;i<all.size();i++) {
			messageMap.put(all.get(i).getUser_id(), all.get(i));
		}
	}
	
	/**
	 * ���������Ϣ������Ϣ
	 * @param obj
	 * @param isAdd
	 */
	public void operMessage(JSONObject obj, boolean isAdd) {
		UserModel umodel=DB.getInstance(context).getUserModel();
		if(umodel==null) {
			return;
		}
		try {
			ReceiverPrivateLetterModel model=new ReceiverPrivateLetterModel();
			model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
			model.setContent(obj.getString("content"));
			model.setTime(obj.getLong("time"));
			model.setUser_id(obj.getInt("user_id"));
			model.setUsername(obj.getString("user_name"));
			model.setType(obj.getInt("type"));
			if(messageMap.containsKey(obj.getInt("user_id"))&&isAdd) {
				model.setNoReadCount((messageMap.get(obj.getInt("user_id")).getNoReadCount()+1));
			}
			else if(!messageMap.containsKey(obj.getInt("user_id"))&&isAdd) {
				model.setNoReadCount(1);
			}
			else {
				model.setNoReadCount(0);
			}
			messageMap.put(obj.getInt("user_id"), model);
			
			DB.getInstance(context).insertPrivateMessageList(context, model);
			
			//����ʱ���͹㲥
			if(!CommonUtils.checkIsScreenOn(context)) {
				Intent intent_screen=new Intent();
				intent_screen.setAction("com.renyu.alumni.receiver.ScreenOnReceiver");
				context.sendBroadcast(intent_screen);
			}
			//�������ҳ����ڣ����͹㲥ˢ��
			Intent intent_screen_refresh=new Intent();
			intent_screen_refresh.setAction("com.renyu.alumni.receiver.ScreenOnReceiver2");
			context.sendBroadcast(intent_screen_refresh);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ϵ���б�δ����Ϣ����Ϊ0
	 * @param context
	 * @param user_id
	 */
	public void clearOneMessage(Context context, int user_id) {
		Integer in=Integer.valueOf(user_id);
		if(messageMap.containsKey(in)) {
			ReceiverPrivateLetterModel model=messageMap.get(in);
			model.setNoReadCount(0);
			messageMap.put(in, model);
			DB.getInstance(context).clearMessage(context, user_id);
		}
	}
	
	/**
	 * �ֶ����ӵ�ģ������
	 * MessageManager.getInstance(context).addModualData("asdasd&asasd", cmodel.getUser_id(), cmodel.getUser_name(), cmodel.getAvatar_large());				
	 */
	public PrivateLetterModel addModualData(String message, int user_id, String user_name, String avatar_large, int message_type) {
		PrivateLetterModel model=new PrivateLetterModel();
		model.setPrivate_letter_contenttime(System.currentTimeMillis()+ParamsManager.extratime);
		if(message_type==CustomPushReceiver.MODUAL_MODEL) {
			model.setPrivate_letter_to(1);
		}
		else {
			model.setPrivate_letter_to(0);
		}
		model.setPrivate_letter_usercontent(message);
		model.setPrivate_letter_userid(user_id);
		model.setPrivate_letter_type(message_type);
		if(message_type==CustomPushReceiver.IMAGE_MODEL) {
			model.setPrivate_letter_success(0);
		}
		else {
			model.setPrivate_letter_success(1);
		}
		//���
		ArrayList<PrivateLetterModel> models_=new ArrayList<PrivateLetterModel>();
		models_.add(model);
		DB.getInstance(context).insertPrivateLetter(models_, context);
		
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_id", user_id);
			obj.put("user_name", user_name);
			obj.put("avatar_large", avatar_large);
			if(message_type==CustomPushReceiver.MODUAL_MODEL) {
				obj.put("content", "��������Ϣ��");
			}
			else if(message_type==CustomPushReceiver.IMAGE_MODEL) {
				obj.put("content", "��ͼƬ��");
			}
			else if(message_type==CustomPushReceiver.LOCATION_MODEL) {
				obj.put("content", "��λ�á�");
			}
			else if(message_type==CustomPushReceiver.BUSSINESS_MODEL) {
				obj.put("content", "����Ƭ��");
			}
			obj.put("type", message_type);
			obj.put("time", ParamsManager.extratime+System.currentTimeMillis());
			MessageManager.getInstance(context).operMessage(obj, false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(message_type==CustomPushReceiver.IMAGE_MODEL||message_type==CustomPushReceiver.MODUAL_MODEL) {
			return model;
		}
		uploadToServer(message, user_id, message_type);
		return model;
	}
	
	/**
	 * �ϴ��ɹ���ɱ�־λ����
	 * @param userId
	 * @param imageUrl
	 */
	public void uploadComp(int userId, String imageUrl) {
		DB.getInstance(context).updateSuccess(userId, imageUrl, context);
	}
	
	public void uploadToServer(String message, int user_id, int type) {
		try {
			UserModel umodel=DB.getInstance(context).getUserModel();
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "sendmessage", umodel.getToken(), context);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			AsyncHttpClient client=new AsyncHttpClient();
			JSONObject obj_upload=new JSONObject();
			obj_upload.put("from_user_id", umodel.getUser_id());
			obj_upload.put("to_user_id", user_id);
			obj_upload.put("content", message);
			obj_upload.put("type", type);
			ByteArrayEntity entity=new ByteArrayEntity(obj_upload.toString().getBytes("UTF-8"));
			client.post(context, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/sendmessage", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					
				}});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
