package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.service.UpdateService;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SettingActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	LinearLayout setting_update=null;
	LinearLayout setting_suggest=null;
	LinearLayout setting_protocol=null;
	LinearLayout setting_share=null;
	TextView setting_loginout=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		setting_update=(LinearLayout) findViewById(R.id.setting_update);
		setting_update.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, "���ڼ��汾��Ϣ", 2000).show();
				Intent intent=new Intent(SettingActivity.this, UpdateService.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("isNeedShowDialog", true);
				intent.putExtras(bundle);
				startService(intent);
			}});
		
		setting_suggest=(LinearLayout) findViewById(R.id.setting_suggest);
		setting_suggest.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this, SuggestActivity.class);
				startActivity(intent);
			}});
		
		setting_protocol=(LinearLayout) findViewById(R.id.setting_protocol);
		setting_protocol.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this, AgreementActivity.class);
				startActivity(intent);
			}});
		
		setting_share=(LinearLayout) findViewById(R.id.setting_share);
		setting_share.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> strArrayList=new ArrayList<String>();
				ArrayList<Integer> imageArrayList=new ArrayList<Integer>();
				
				if(CommonUtils.isAppInstalled(SettingActivity.this, "com.tencent.mobileqq")) {
					imageArrayList.add(R.drawable.qq_logo);
					strArrayList.add("QQ����");
					imageArrayList.add(R.drawable.qzone_logo);
					strArrayList.add("QQ�ռ�");
				}
				if(CommonUtils.isAppInstalled(SettingActivity.this, "com.tencent.mm")) {
					imageArrayList.add(R.drawable.weixin_logo);
					strArrayList.add("΢�ź���");
					imageArrayList.add(R.drawable.weixinpy_logo);
					strArrayList.add("΢������Ȧ");
				}
				if(CommonUtils.isAppInstalled(SettingActivity.this, "com.sina.weibo")) {
					imageArrayList.add(R.drawable.weibo_logo);
					strArrayList.add("����΢��");
				}
				Object[] strArrayTemp=(Object[]) strArrayList.toArray();
				final String[] strArray=new String[strArrayTemp.length];
				for(int i=0;i<strArrayTemp.length;i++) {
					strArray[i]=String.valueOf(strArrayTemp[i]);
				}
				Object[] imageArrayTemp=(Object[]) (imageArrayList.toArray());
				final int[] imageArray=new int[imageArrayTemp.length];
				for(int i=0;i<imageArrayTemp.length;i++) {
					imageArray[i]=Integer.valueOf(String.valueOf(imageArrayTemp[i]));
				}
				CommonUtils.showCustomAlertDialog(SettingActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						if(strArray[pos].equals("QQ����")) {
							CommonUtils.shareQQ(SettingActivity.this, "�пƴ�У��ר������APP����ҿ�������ɡ�", "http://morningtel.qiniudn.com/ic_launcher.png", "�ƴ��˺�����", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("QQ�ռ�")) {
							CommonUtils.shareQQKJ(SettingActivity.this, "�пƴ�У��ר������APP����ҿ�������ɡ�", "http://morningtel.qiniudn.com/ic_launcher.png", "�ƴ��˺�����", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢�ź���")) {
							CommonUtils.shareWeixin(SettingActivity.this, "�пƴ�У��ר������APP����ҿ�������ɡ�", "�ƴ��˺�����", "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢������Ȧ")) {
							CommonUtils.shareWeixinPy(SettingActivity.this, "�пƴ�У��ר������APP����ҿ�������ɡ�", "�ƴ��˺�����", "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("����΢��")) {
							CommonUtils.shareWeibo(SettingActivity.this, "�пƴ�У��ר������APP����ҿ�������ɡ�", "http://morningtel.qiniudn.com/ic_launcher.png", "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
					}});
			}});
		setting_loginout=(TextView) findViewById(R.id.setting_loginout);
		UserModel model=DB.getInstance(this).getUserModel();
		if(model==null) {
			setting_loginout.setVisibility(View.GONE);
		}
		
		setting_loginout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserModel model=DB.getInstance(SettingActivity.this).getUserModel();
				DB.getInstance(SettingActivity.this).offlineUser(model.getUser_id(), 0);
				//�Ÿ�ע��ȡ������
				XGPushManager.registerPush(SettingActivity.this.getApplicationContext(), "*");
				CommonUtils.showCustomToast(SettingActivity.this, "ע���ɹ�", false);
				finish();
			}
		});
	}

    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
