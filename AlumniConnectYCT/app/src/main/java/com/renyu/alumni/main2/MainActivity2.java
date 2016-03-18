package com.renyu.alumni.main2;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;

import com.baidu.mobstat.StatService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.renyu.alumni.AlumniApplication;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.UserModel;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGPushManager;

public class MainActivity2 extends SlidingFragmentActivity {
	
	Fragment mContent;
	SlidingMenu sm=null;
	
	MenuFragment menuFragment=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);
		
		//XGPushConfig.enableDebug(this, true);
		// ����֪ͨ���Զ�����ʽ����build_id
		initNotificationBuilder(this);
		
		// ע��Ӧ�ã�������ñ��ӿڣ�����APP���޷����յ�֪ͨ����Ϣ��
		// ʹ�ð��˺ŵ�ע��ӿڣ�������˺��·�֪ͨ����Ϣ��
		// �����ظ�ע�ᣬ�����һ��ע��Ϊ׼
		UserModel model=DB.getInstance(MainActivity2.this).getUserModel();
		if(DB.getInstance(MainActivity2.this).getUserModel()!=null) {
			ParamsManager.XGLoadTimes=0;
			CommonUtils.registXG(getApplicationContext(), model.getToken());
		}
		else {
			XGPushManager.registerPush(getApplicationContext());
		}
		
		//���ò˵�
		if (findViewById(R.id.menu_frame)==null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
		else {
			//��������¾Ͳ���չʾ�˵�
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
		
		//������ҳ��
		if(savedInstanceState!=null) {
            mContent=getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
		if (mContent==null) {
            mContent=new ContentFragment();
        }
		menuFragment=new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.main2_content_frame, mContent).commitAllowingStateLoss();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment).commitAllowingStateLoss();
		
		//slidingmenu�������
		sm=getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        sm.setBackgroundImage(R.drawable.main_bg);
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.2 + 0.8);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });
        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.2);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
        
	}
	
	/**
	 * �����Զ�����ʽ���������·�֪ͨʱ����ָ��build_id������ɿ������Լ�ά��
	 * 
	 * @param context
	 */
	private void initNotificationBuilder(Context context) {
		// �½��Զ�����ʽ
		XGBasicPushNotificationBuilder build=new XGBasicPushNotificationBuilder();
		// �����Զ�����ʽ���ԣ������ԶԶ�Ӧ�ı����Ч��ָ�������޸ġ�
		build.setIcon(R.drawable.ic_launcher).setSound(RingtoneManager
				// ��������
				.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM)) 
				// ��
				.setDefaults(Notification.DEFAULT_VIBRATE) 
				// �Ƿ�����
				.setFlags(Notification.FLAG_NO_CLEAR); 
		// ����֪ͨ��ʽ����ʽ���Ϊ2����build_idΪ2����ͨ����̨�ű�ָ��
		XGPushManager.setPushNotificationBuilder(this, 2, build);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		menuFragment.refreshLoginInfo();
		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mContent!=null) {
			if(((ContentFragment) mContent).isSendLayoutOpen()) {
				((ContentFragment) mContent).closeSendLayout();
			}
			else {
				new AlertDialog.Builder(MainActivity2.this).setTitle("��ʾ").setMessage("���Ҫ�˳��ƴ��ˣ�").setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((AlumniApplication) getApplication()).isStart=false;
						finish();
					}
				}).setNegativeButton("�Եȣ����ٿ���", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
			}
			return;
		}
	}
}
