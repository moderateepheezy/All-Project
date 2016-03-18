package com.renyu.alumni.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.UpdateTipNotificationManager;
import com.renyu.alumni.model.JsonParse;

import org.apache.http.Header;

import java.util.HashMap;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent!=null&&intent.getExtras()!=null) {
			//�Ƿ���ʾ��ʾ�Ի���
			final boolean isNeedShowDialog=intent.getExtras().getBoolean("isNeedShowDialog");
			int version_code=0;
			try {
				PackageInfo info=getPackageManager().getPackageInfo(getPackageName(), 0);
				version_code=info.versionCode;
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			AsyncHttpClient client=new AsyncHttpClient();
			client.get(ParamsManager.HttpUrl+"StudentsContacts/contactsapi/person/checkversion?platform=1&version_id="+version_code, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					HashMap<String, String> map=JsonParse.getVersionInfo(new String(responseBody));
					if(map!=null) {
						String version_desc=map.get("version_desc");
						String version_id=map.get("version_id");
						String force_update=map.get("force_update");
						String url=map.get("url");
						PackageManager manager=UpdateService.this.getPackageManager();
						try {
							PackageInfo info=manager.getPackageInfo(UpdateService.this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
							if(info.versionCode<Integer.parseInt(version_id)) {
								if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.KITKAT) {
									showUpdateDialog(version_desc, url, force_update);
								}
								else {
									showUpdateDialog2(version_desc, url, force_update);
								}
							}
							else {
								if(isNeedShowDialog) {
									Toast.makeText(UpdateService.this, "�ƴ����Ѿ������°汾", 3000).show();
								}
							}
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(isNeedShowDialog) {
								Toast.makeText(UpdateService.this, "�ƴ����Ѿ������°汾", 3000).show();
							}
						}
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(isNeedShowDialog) {
						Toast.makeText(UpdateService.this, "�ƴ����Ѿ������°汾", 3000).show();
					}
				}
			});
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void showUpdateDialog(String content, final String url, final String force_update) {
		final AlertDialog dialog=new AlertDialog.Builder(UpdateService.this).create();
		dialog.setCancelable(false);
		Window window=dialog.getWindow();
		window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		window.setContentView(R.layout.alertdialog_update);
		TextView update_content=(TextView) window.findViewById(R.id.update_content);
		update_content.setText(content);
		TextView update_later=(TextView) window.findViewById(R.id.update_later);
		update_later.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				stopSelf();
				//ǿ�Ƹ���
				if(force_update.equals("1")) {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}});
		TextView update_now=(TextView) window.findViewById(R.id.update_now);
		update_now.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UpdateService.this, DownloadService.class);
				Bundle bundle=new Bundle();
				bundle.putString("url", url);
				intent.putExtras(bundle);
				startService(intent);
				dialog.cancel();
				stopSelf();
			}});
	}
	
	private void showUpdateDialog2(String content, final String url, final String force_update) {
		UpdateTipNotificationManager.getNotificationCenter(UpdateService.this).createNewNotification(UpdateService.this, url);
		stopSelf();
	}

}
