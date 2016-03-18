package com.renyu.alumni.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.UpdateNotificationManager;

import org.apache.http.Header;

import java.io.File;

public class DownloadService extends Service {
	
	int persent=0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent!=null&&intent.getExtras()!=null) {
			UpdateNotificationManager.getNotificationCenter(DownloadService.this).createNewNotification(DownloadService.this, 0);
			String url=intent.getExtras().getString("url");
			
			String dirPath=Environment.getExternalStorageDirectory().getPath()+"/Alumni/alumni.apk";
			File file_=new File(dirPath);
			
			AsyncHttpClient client=new AsyncHttpClient();
			FileAsyncHttpResponseHandler handler=new FileAsyncHttpResponseHandler(file_) {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, File file) {
					// TODO Auto-generated method stub
					UpdateNotificationManager.getNotificationCenter(DownloadService.this).cancelNotification(DownloadService.this, 0);
					UpdateNotificationManager.getNotificationCenter(DownloadService.this).showEndNotification(DownloadService.this, 0);
					
					//��װ���
					Intent intent=new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(Intent.ACTION_VIEW);
					String type="application/vnd.android.package-archive";
					intent.setDataAndType(Uri.fromFile(file), type);
					startActivity(intent);
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, File file) {
					// TODO Auto-generated method stub
					CommonUtils.showCustomToast(DownloadService.this, "����ʧ��", false);
					UpdateNotificationManager.getNotificationCenter(DownloadService.this).cancelNotification(DownloadService.this, 0);
				}
				
				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					// TODO Auto-generated method stub
					super.onProgress(bytesWritten, totalSize);
					System.out.println(bytesWritten+"/"+totalSize);
					int currentPersent=bytesWritten*100/totalSize;
					if(currentPersent-persent>5) {
						UpdateNotificationManager.getNotificationCenter(DownloadService.this).updateNotification(DownloadService.this, 0, persent);
						persent=currentPersent;
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					stopSelf();
				}
			};
			client.get(url, handler);
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
