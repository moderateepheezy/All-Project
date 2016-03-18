package com.renyu.alumni.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class CustomPushReceiver extends XGPushBaseReceiver {

	//��ͨģʽ
	public static final int MORMAL_MODEL=0;
	//˽��ģʽ
	public static final int PRIVATELETTER_MODEL=1;
	//ϵͳ��Ϣģʽ
	public static final int System_MODEL=2;
	//ģ����Ϣģʽ
	public static final int MODUAL_MODEL=3;
	//ͼƬ��Ϣģʽ
	public static final int IMAGE_MODEL=4;
	//λ����Ϣģʽ
	public static final int LOCATION_MODEL=5;
	//��Ƭ��Ϣģʽ
	public static final int BUSSINESS_MODEL=6;
	//���ĺ���Ϣģʽ
	public static final int Subscribe_MODEL=7;

	//10000000Ϊ�����˺���Ϣ
	
	private void show(Context context, String text) {
		System.out.println(text);
	}

	/**
	 * ע����
	 * 
	 * @param context
	 *            APP�����Ķ���
	 * @param errorCode
	 *            �����룬{@link XGPushBaseReceiver#SUCCESS}��ʾ�ɹ���������ʾʧ��
	 * @param registerMessage
	 *            ע��������
	 */
	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult registerMessage) {
		if (context == null || registerMessage == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = registerMessage + "ע��ɹ�";
			// ��������token
			String token = registerMessage.getToken();
			show(context, text+" token="+token);
		} else {
			text = registerMessage + "ע��ʧ�ܣ������룺" + errorCode;
			show(context, text);
		}
	}

	/**
	 * ��ע����
	 * 
	 * @param context
	 *            APP�����Ķ���
	 * @param errorCode
	 *            �����룬{@link XGPushBaseReceiver#SUCCESS}��ʾ�ɹ���������ʾʧ��
	 */
	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "��ע��ɹ�";
		} else {
			text = "��ע��ʧ��" + errorCode;
		}
		show(context, text);
	}

	/**
	 * ���ñ�ǩ�������
	 * 
	 * @param context
	 *            APP�����Ķ���
	 * @param errorCode
	 *            �����룬{@link XGPushBaseReceiver#SUCCESS}��ʾ�ɹ���������ʾʧ��
	 * @tagName ��ǩ����
	 */
	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"���óɹ�";
		} else {
			text = "\"" + tagName + "\"����ʧ��,�����룺" + errorCode;
		}
		show(context, text);
	}

	/**
	 * ɾ����ǩ�������
	 * 
	 * @param context
	 *            APP�����Ķ���
	 * @param errorCode
	 *            �����룬{@link XGPushBaseReceiver#SUCCESS}��ʾ�ɹ���������ʾʧ��
	 * @tagName ��ǩ����
	 */
	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"ɾ���ɹ�";
		} else {
			text = "\"" + tagName + "\"ɾ��ʧ��,�����룺" + errorCode;
		}
		show(context, text);
	}

	/**
	 * �յ���Ϣ<br>
	 * 
	 * @param context
	 *            APP�����Ķ���
	 * @param message
	 *            �յ�����Ϣ
	 */
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		if (context == null || message == null) {
			return;
		}
		String text = "�յ���Ϣ:" + message.toString();
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					show(context, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP����������Ϣ�Ĺ��̡�����
		show(context, text);
	}

	// ֪ͨ����ص� actionType=1Ϊ����Ϣ�������actionType=0Ϊ����Ϣ�����
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = null;
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// ֪ͨ��֪ͨ�������������������
			// APP�Լ�����������ض���
			// �������������activity��onResumeҲ�ܼ������뿴��3���������
			text = "֪ͨ���� :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// ֪ͨ���������������
			// APP�Լ�����֪ͨ����������ض���
			text = "֪ͨ����� :" + message;
		}
		System.out.println("�㲥���յ�֪ͨ�����:" + message.toString());
		// ��ȡ�Զ���key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1Ϊǰ̨���õ�key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					System.out.println("get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP��������Ĺ��̡�����
		show(context, text);
	}

	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
		if (context==null||notifiShowedRlt==null) {
			return;
		}
		String text="֪ͨ��չʾ ��title:"+notifiShowedRlt.getTitle()+",content:"+notifiShowedRlt.getContent()+",custom_content:"+notifiShowedRlt.getCustomContent();
		show(context, text);
		operateMessage(notifiShowedRlt.getCustomContent(), context);
	}
	
	/**
	 * ��Ϣ�б���
	 * @param message
	 * @param context
	 */
	private synchronized void operateMessage(String message, Context context) {
		try {
			JSONObject obj=new JSONObject(message);
			//1������Ϣ���� 
			if(obj.getInt("type")==PRIVATELETTER_MODEL||obj.getInt("type")==MODUAL_MODEL||obj.getInt("type")==IMAGE_MODEL||obj.getInt("type")==LOCATION_MODEL||obj.getInt("type")==BUSSINESS_MODEL||obj.getInt("type")==Subscribe_MODEL) {
				ParamsManager.isPrivateShow=true;
				MessageManager.getInstance(context).operMessage(obj, true);				
				Intent intent=new Intent();
				intent.setAction("PRIVATELETTER");
				Bundle bundle=new Bundle();
				bundle.putString("user_id", obj.getString("user_id"));
				intent.putExtras(bundle);
				context.sendBroadcast(intent);
				
				EventBus.getDefault().post("PRIVATELETTER");
			}
			else if(obj.getInt("type")==System_MODEL) {
				ParamsManager.isSystemShow=true;
				Intent intent=new Intent();
				intent.setAction("SYSTEMNOTIFICATION");
				context.sendBroadcast(intent);
				
				EventBus.getDefault().post("SYSTEMNOTIFICATION");
			}
			else if(obj.getInt("type")==MORMAL_MODEL) {
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}