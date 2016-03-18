package com.renyu.alumni;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.service.QiniuService;

public class AlumniApplication extends Application {

	public boolean isStart=false;
			
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		SDKInitializer.initialize(this);

		CommonUtils.loadDir();

		CommonUtils.copyDbFile(getApplicationContext());

		Intent intent=new Intent(getApplicationContext(), QiniuService.class);
		startService(intent);

		MessageManager.getInstance(getApplicationContext()).startMessageUser();
	}
	
}
