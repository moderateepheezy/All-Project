package com.renyu.alumni.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.renyu.alumni.R;
import com.renyu.alumni.launcher.SplashActivity;
import com.renyu.alumni.receiver.AlarmReceiver;

import java.util.Calendar;

public class QiniuService extends Service {
	
	PendingIntent sender=null;
	AlarmManager alarm=null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Intent intent=new Intent(QiniuService.this, AlarmReceiver.class);
	    intent.setAction("alarm");
	    sender=PendingIntent.getBroadcast(QiniuService.this, 0, intent, 0);	    
	    Calendar calendar=Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.add(Calendar.SECOND, 5);	    
	    alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
	    long now=System.currentTimeMillis();
	    alarm.setInexactRepeating(AlarmManager.RTC, now, 5000, sender);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent!=null) {
			setUpAsForeground();
		}
		return START_STICKY;
	}
	
	private void setUpAsForeground() {
		PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification mNotification=new Notification();
		mNotification.tickerText="У�ѻ�";
		mNotification.icon=R.drawable.ic_launcher;// TODO ��������ǰ�õ�ͼ��
		mNotification.flags=Notification.FLAG_ONGOING_EVENT;
		mNotification.setLatestEventInfo(getApplicationContext(), "У�ѻ�", "У�ѻ�", pendingIntent);
		startForeground(0, mNotification);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		alarm.cancel(sender);
		stopForeground(true);
		
		Intent intent=new Intent("qiniurelodingservice");
		sendBroadcast(intent);
	}

}
