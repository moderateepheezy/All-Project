package com.renyu.alumni.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.AlumniApplication;
import com.renyu.alumni.common.ImageUploadManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class AlarmReceiver extends BroadcastReceiver {
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("alarm")) {
			UserModel model=DB.getInstance(context).getUserModel();
			System.out.println("����������");
			//�û�û�е�¼����ֱ������
			if(model==null) {
				return;
			}
			//�û���¼����ʼ��ȡ
			else {
				//������ڼ����򲻽��л�ȡ
				if(ParamsManager.isGettingToken) {
					return;
				}
				//��������ں�̨����û���ϴ���ͼƬ���򲻻�ȡ
				if(!((AlumniApplication) context.getApplicationContext()).isStart&&ImageUploadManager.getInstance().getUploadMaps().size()==0) {
					return;
				}
				final long currentTime=System.currentTimeMillis();
				//1�����ڲ����ظ���ȡ
				if(ParamsManager.requestTime+1000*60>currentTime) {
					return;
				}
				ParamsManager.isGettingToken=true;
				AsyncHttpClient client=new AsyncHttpClient();
				RequestParams params=new RequestParams();
				Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken())};
				client.get(context, "http://112.126.70.71:7050/gettoken", headers, params, new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
						// TODO Auto-generated method stub
						ParamsManager.requestTime=currentTime;
						System.out.println(new String(responseBody));
						ParamsManager.uptoken=JsonParse.getQiNiuToke(new String(responseBody));
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						ParamsManager.isGettingToken=false;
					}
				});
			}
		}
	}

}
