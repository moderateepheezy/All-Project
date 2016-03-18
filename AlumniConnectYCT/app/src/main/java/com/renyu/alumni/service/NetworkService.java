package com.renyu.alumni.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;

import java.lang.reflect.Method;

public class NetworkService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		showNetworkDialog();
	}

    /**
     * ��������ʧ�ܵ�����
     * @param context
     */
    private void showNetworkDialog() {
		final AlertDialog dialog=new AlertDialog.Builder(NetworkService.this).create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		Window window=dialog.getWindow();
		window.setContentView(R.layout.alertdialog_networkchoice);
		LinearLayout networkchoice_wifi=(LinearLayout) window.findViewById(R.id.networkchoice_wifi);
		networkchoice_wifi.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WifiManager wifiManager=(WifiManager) NetworkService.this.getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);
				CommonUtils.showCustomToast(NetworkService.this, "���ڴ�WiFi����", true);
				dialog.cancel();
				stopSelf();
			}});
		LinearLayout networkchoice_cellular_network=(LinearLayout) window.findViewById(R.id.networkchoice_cellular_network);
		networkchoice_cellular_network.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Object[] arg = null;
				try {
					boolean isMobileDataEnable=invokeMethod("getMobileDataEnabled", arg);
					if (!isMobileDataEnable) {
						invokeBooleanArgMethod("setMobileDataEnabled", true);
					}
					CommonUtils.showCustomToast(NetworkService.this, "���ڴ���������", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.cancel();
				stopSelf();
			}});
	}
    
    public boolean invokeMethod(String methodName, Object[] arg) throws Exception {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
		return isOpen;
	}

	public Object invokeBooleanArgMethod(String methodName, boolean value) throws Exception {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(mConnectivityManager, value);
	}
}
