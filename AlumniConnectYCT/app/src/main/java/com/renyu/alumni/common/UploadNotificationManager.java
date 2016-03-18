package com.renyu.alumni.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.renyu.alumni.R;
import com.renyu.alumni.launcher.SplashActivity;

import java.util.HashMap;

public class UploadNotificationManager {

	static UploadNotificationManager center=null;
	
	static NotificationManager manager=null;
	
	//�����Ӧid��֪ͨ��������Ϣ
	static HashMap<Integer, Notification> maps=null;
	//���������Ϣ����ͳһ�ϴ�������ͼƬURL�ӿ�
	static HashMap<Integer, String> strs=new HashMap<Integer, String>();
	
	/**
	 * ֪ͨ�����ĵ���
	 * @param context
	 * @return
	 */
	public static UploadNotificationManager getNotificationCenter(Context context) {
		if(center==null) {
			center=new UploadNotificationManager();
			
			manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			maps=new HashMap<Integer, Notification>();
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

		Notification no=new Notification();
		no.flags=Notification.FLAG_AUTO_CANCEL;
		no.icon=R.drawable.ic_launcher;
		no.when=System.currentTimeMillis();
		no.tickerText=context.getResources().getString(R.string.app_name)+"���ڷ�����...";
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.view_notification);
		no.contentView=views;
		no.contentView.setProgressBar(R.id.no_pb, 100, 0, false);
		no.contentView.setTextViewText(R.id.no_title, context.getResources().getString(R.string.app_name)+"���ڷ�����...");
		no.contentView.setTextViewText(R.id.no_pb_num, "0%");
		no.contentIntent=PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class), 0);
		manager.notify(id, no);
		maps.put(id, no);
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
	
	/**
	 * ������Ӧid��֪ͨ��
	 * @param context
	 * @param id
	 */
	public void updateNotification(Context context, int id, int persent) {
		Notification no=maps.get(id);
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.view_notification);
		views.setProgressBar(R.id.no_pb, 100, persent, false);
		views.setTextViewText(R.id.no_title, context.getResources().getString(R.string.app_name));
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
		maps.remove(id);
	}
	
}
