package com.renyu.alumni.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.renyu.alumni.R;
import com.renyu.alumni.launcher.SplashActivity;

public class UpdateNotificationManager {
	
	static UpdateNotificationManager center=null;
	
	static NotificationManager manager=null;
	static Notification no=null;
	
	/**
	 * ֪ͨ�����ĵ���
	 * @param context
	 * @return
	 */
	public static UpdateNotificationManager getNotificationCenter(Context context) {
		if(center==null) {
			center=new UpdateNotificationManager();
			
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return center;
	}
	
	/**
	 * ������֪ͨ
	 * @param context
	 * @param title
	 * @param id
	 */
	public void createNewNotification(Context context, int id) {

		no=new Notification();
		no.flags=Notification.FLAG_AUTO_CANCEL;
		no.icon=R.drawable.ic_launcher;
		no.when=System.currentTimeMillis();
		no.tickerText=context.getResources().getString(R.string.app_name)+"���ڸ�����...";
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.view_notification);
		no.contentView=views;
		no.contentView.setProgressBar(R.id.no_pb, 100, 0, false);
		no.contentView.setTextViewText(R.id.no_title, context.getResources().getString(R.string.app_name)+"���ڸ�����...");
		no.contentView.setTextViewText(R.id.no_pb_num, "0%");
		no.contentIntent=PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class), 0);
		manager.notify(id, no);
	}
	
	/**
	 * ������Ӧid��֪ͨ��
	 * @param context
	 * @param id
	 */
	public void updateNotification(Context context, int id, int persent) {
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.view_notification);
		views.setProgressBar(R.id.no_pb, 100, persent, false);
		views.setTextViewText(R.id.no_title, context.getResources().getString(R.string.app_name)+"���ڸ�����...");
		views.setTextViewText(R.id.no_pb_num, persent+"%");
		no.contentView=views;
		manager.notify(id, no);
	}
	
	/**
	 * �ر�֪ͨ
	 * @param context
	 * @param id
	 */
	public void cancelNotification(Context context, int id) {
		manager.cancel(id);
	}
	
	/**
	 * �����ʾ
	 * @param context
	 * @param id
	 */
	public void showEndNotification(Context context, int id) {
		Notification no=new Notification();
		no.flags=Notification.FLAG_AUTO_CANCEL;
		no.icon=R.drawable.ic_launcher;
		no.when=System.currentTimeMillis();
		no.tickerText=context.getResources().getString(R.string.app_name)+"�������";
		Intent i=new Intent(context, SplashActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);		                 
		no.setLatestEventInfo(context, context.getResources().getString(R.string.app_name), "�������", contentIntent);
		manager.notify(id, no);
		manager.cancel(id);
	}
}
