package com.renyu.alumni.common;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.renyu.alumni.R;

public class UpdateTipNotificationManager {
	
	static UpdateTipNotificationManager center=null;
	
	static NotificationManager manager=null;
	static Notification no=null;
	
	/**
	 * ֪ͨ�����ĵ���
	 * @param context
	 * @return
	 */
	public static UpdateTipNotificationManager getNotificationCenter(Context context) {
		if(center==null) {
			center=new UpdateTipNotificationManager();
			
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
	public void createNewNotification(Context context, String url) {
		Intent intent=new Intent(context, UpdateTipActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("url", url);
		intent.putExtras(bundle);
		Resources res = context.getResources();
		Builder builder = new Notification.Builder(context);
		builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
		builder.setSmallIcon(R.drawable.ic_launcher);//����״̬�������ͼ�꣨Сͼ�꣩
		builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher));//���������б������ͼ�꣨��ͼ�꣩ ��������������.setTicker("this is bitch!") //����״̬������ʾ����Ϣ
		builder.setWhen(System.currentTimeMillis());//����ʱ�䷢��ʱ��
		builder.setAutoCancel(true);//���ÿ������
		builder.setContentTitle("������");//���������б���ı���
        builder.setContentText(context.getResources().getString(R.string.app_name)+"�������������������");//��������������
        no=builder.getNotification();//��ȡһ��Notification
        no.defaults =Notification.DEFAULT_SOUND;//����ΪĬ�ϵ�����
		no.flags=Notification.FLAG_AUTO_CANCEL;
		no.icon=R.drawable.ic_launcher;
		no.when=System.currentTimeMillis();
		no.tickerText=context.getResources().getString(R.string.app_name)+"�°汾����";
        manager.notify(10000, no);
	}
}
