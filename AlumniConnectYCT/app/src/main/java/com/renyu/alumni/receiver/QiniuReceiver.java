package com.renyu.alumni.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.renyu.alumni.service.QiniuService;

public class QiniuReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("qiniurelodingservice")) {
			//������ţtoken
			Intent intent_=new Intent(context, QiniuService.class);
			context.startService(intent_);
		}
	}

}
