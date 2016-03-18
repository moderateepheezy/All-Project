package com.renyu.alumni.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.service.NetworkService;

public class NetWorkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			//ֻ���ڵ�ǰӦ�ô򿪲�����������ʧ�ܲ�����ʾ����񲻴��ڵ�����£�����ʾ��
			if(CommonUtils.isRunningForeground(context)&&!CommonUtils.showNoNetworkInfo(context)&&!CommonUtils.isServiceWorked(context, "com.renyu.alumni.service.NetworkService")) {
				Intent service=new Intent(context, NetworkService.class);
				context.startService(service);
			}
			
        }   
	}

}
