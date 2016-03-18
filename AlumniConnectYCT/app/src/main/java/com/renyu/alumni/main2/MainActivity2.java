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
		// 设置通知的自定义样式，即build_id
		initNotificationBuilder(this);
		
		// 注册应用（必须调用本接口，否则APP将无法接收到通知和消息）
		// 使用绑定账号的注册接口（可针对账号下发通知和消息）
		// 可以重复注册，以最后一次注册为准
		UserModel model=DB.getInstance(MainActivity2.this).getUserModel();
		if(DB.getInstance(MainActivity2.this).getUserModel()!=null) {
			ParamsManager.XGLoadTimes=0;
			CommonUtils.registXG(getApplicationContext(), model.getToken());
		}
		else {
			XGPushManager.registerPush(getApplicationContext());
		}
		
		//设置菜单
		if (findViewById(R.id.menu_frame)==null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
		else {
			//错误情况下就不再展示菜单
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
		
		//设置主页面
		if(savedInstanceState!=null) {
            mContent=getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
		if (mContent==null) {
            mContent=new ContentFragment();
        }
		menuFragment=new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.main2_content_frame, mContent).commitAllowingStateLoss();
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment).commitAllowingStateLoss();
		
		//slidingmenu相关属性
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
	 * 设置自定义样式，这样在下发通知时可以指定build_id。编号由开发者自己维护
	 * 
	 * @param context
	 */
	private void initNotificationBuilder(Context context) {
		// 新建自定义样式
		XGBasicPushNotificationBuilder build=new XGBasicPushNotificationBuilder();
		// 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
		build.setIcon(R.drawable.ic_launcher).setSound(RingtoneManager
				// 设置声音
				.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM)) 
				// 振动
				.setDefaults(Notification.DEFAULT_VIBRATE) 
				// 是否可清除
				.setFlags(Notification.FLAG_NO_CLEAR); 
		// 设置通知样式，样式编号为2，即build_id为2，可通过后台脚本指定
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
				new AlertDialog.Builder(MainActivity2.this).setTitle("提示").setMessage("真的要退出科大人？").setPositiveButton("是的", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((AlumniApplication) getApplication()).isStart=false;
						finish();
					}
				}).setNegativeButton("稍等，我再看看", new DialogInterface.OnClickListener() {
					
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
