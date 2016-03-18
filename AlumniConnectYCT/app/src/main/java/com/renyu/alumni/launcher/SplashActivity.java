package com.renyu.alumni.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.AlumniApplication;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.main2.MainActivity2;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.service.NetworkService;
import com.renyu.alumni.service.UpdateService;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
	
	ViewPager splash_viewpager=null;
	PagerAdapter adapter=null;
	List<View> views=null;
	LinearLayout splash_viewpager_indicator=null;
	
	ImageView splash1_left=null;
	ImageView splash1_right=null;
	ImageView splash2_left=null;
	ImageView splash2_right=null;
	ImageView splash2_right_final=null;
	ImageView splash3_top=null;
	ImageView splash3_center=null;
	ImageView splash3_bottom=null;
	ImageView splash4=null;
	ImageView splash_enter=null;
	ImageView splash_pic_1=null;
	ImageView splash_pic_2=null;
	ImageView splash_pic_3=null;
	ImageView splash_pic_4=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		views=new ArrayList<View>();
		
		if(((AlumniApplication) getApplication()).isStart) {
			finish();
			return;
		}
		else {
			((AlumniApplication) getApplication()).isStart=true;

			//�����粻�õ�����£��ж�����������Ƿ�����û�����Ļ��Ϳ���
			if(!CommonUtils.showNoNetworkInfo(SplashActivity.this)) {
				if(!CommonUtils.isServiceWorked(SplashActivity.this, "com.renyu.alumni.service.NetworkService")) {
					Intent service=new Intent(SplashActivity.this, NetworkService.class);
					startService(service);
				}
			}
			else {
				Intent service=new Intent(SplashActivity.this, UpdateService.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("isNeedShowDialog", false);
				service.putExtras(bundle);
				startService(service);
			}

			if(CommonUtils.getShowInterface(SplashActivity.this)) {
				openIndex();
			}
			else {
				init();
			}
			
		}
	}
	
	private void init() {
		splash_viewpager_indicator=(LinearLayout) findViewById(R.id.splash_viewpager_indicator);
		splash_viewpager_indicator.setVisibility(View.VISIBLE);
		splash_pic_1=(ImageView) findViewById(R.id.splash_pic_1);
		splash_pic_2=(ImageView) findViewById(R.id.splash_pic_2);
		splash_pic_3=(ImageView) findViewById(R.id.splash_pic_3);
		splash_pic_4=(ImageView) findViewById(R.id.splash_pic_4);
		splash_viewpager=(ViewPager) findViewById(R.id.splash_viewpager);
		splash_viewpager.setVisibility(View.VISIBLE);
		View view1=LayoutInflater.from(SplashActivity.this).inflate(R.layout.view_splash_1, null);
		splash1_left=(ImageView) view1.findViewById(R.id.splash1_left);
		splash1_right=(ImageView) view1.findViewById(R.id.splash1_right);
		View view2=LayoutInflater.from(SplashActivity.this).inflate(R.layout.view_splash_2, null);
		splash2_left=(ImageView) view2.findViewById(R.id.splash2_left);
		splash2_right=(ImageView) view2.findViewById(R.id.splash2_right);
		splash2_right_final=(ImageView) view2.findViewById(R.id.splash2_right_final);
		View view3=LayoutInflater.from(SplashActivity.this).inflate(R.layout.view_splash_3, null);
		splash3_top=(ImageView) view3.findViewById(R.id.splash3_top);
		splash3_center=(ImageView) view3.findViewById(R.id.splash3_center);
		splash3_bottom=(ImageView) view3.findViewById(R.id.splash3_bottom);
		View view4=LayoutInflater.from(SplashActivity.this).inflate(R.layout.view_splash_4, null);
		splash4=(ImageView) view4.findViewById(R.id.splash4);
		splash_enter=(ImageView) view4.findViewById(R.id.splash_enter);
		splash_enter.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				splash_viewpager.setVisibility(View.GONE);
				splash_viewpager_indicator.setVisibility(View.GONE);
				CommonUtils.setShowIntroduce(SplashActivity.this);
				openIndex();
			}});
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		adapter=new SplashPagerAdapter();
		splash_viewpager.setAdapter(adapter);
		splash_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int arg0) {
				// TODO Auto-generated method stub
				if(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH<android.os.Build.VERSION.SDK_INT) {
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							animate(arg0);
						}
					}, 300);
				}
				else {
					animate(arg0);
				}
				
				splash_pic_1.setImageResource(R.drawable.ic_point2);
				splash_pic_2.setImageResource(R.drawable.ic_point2);
				splash_pic_3.setImageResource(R.drawable.ic_point2);
				splash_pic_4.setImageResource(R.drawable.ic_point2);
				if(arg0==0) {
					splash_pic_1.setImageResource(R.drawable.ic_point1);
				}
				else if(arg0==1) {
					splash_pic_2.setImageResource(R.drawable.ic_point1);
				}
				else if(arg0==2) {
					splash_pic_3.setImageResource(R.drawable.ic_point1);
				}
				else if(arg0==3) {
					splash_pic_4.setImageResource(R.drawable.ic_point1);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		if(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH<android.os.Build.VERSION.SDK_INT) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					animate(0);
				}
			}, 300);
		}
		else {
			animate(0);
		}
	}
	
	private void animate(int position) {
		
		splash1_left.clearAnimation();
		splash1_left.setVisibility(View.INVISIBLE);		
		splash1_right.clearAnimation();
		splash1_right.setVisibility(View.INVISIBLE);
		
		splash2_left.clearAnimation();
		splash2_left.setVisibility(View.INVISIBLE);	
		splash2_right.clearAnimation();
		splash2_right.setVisibility(View.INVISIBLE);
		splash2_right_final.clearAnimation();
		splash2_right_final.setVisibility(View.INVISIBLE);
		
		splash3_top.clearAnimation();
		splash3_top.setVisibility(View.INVISIBLE);
		splash3_center.clearAnimation();
		splash3_center.setVisibility(View.INVISIBLE);	
		splash3_bottom.clearAnimation();
		splash3_bottom.setVisibility(View.INVISIBLE);	
		
		splash4.clearAnimation();
		splash4.setVisibility(View.INVISIBLE);
		
		if(position==0) {	
			Animation animation_left=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_1_left);
			splash1_left.startAnimation(animation_left);
			
			Animation animation_right=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_1_right);
			splash1_right.startAnimation(animation_right);
		}
		else if(position==1) {
			Animation animation_left=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_2_left);
			splash2_left.startAnimation(animation_left);
			
			Animation animation_right=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_2_right);
			animation_right.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					splash2_right.clearAnimation();
					splash2_right.setVisibility(View.INVISIBLE);
					splash2_right_final.setVisibility(View.VISIBLE);
					
					RotateAnimation ani=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					ani.setInterpolator(new LinearInterpolator());
					ani.setRepeatCount(-1);
					ani.setDuration(800);
					ani.setFillAfter(true);	
					splash2_right_final.startAnimation(ani);
				}
			});
			splash2_right.startAnimation(animation_right);
		}
		else if(position==2) {
			Animation animation_top=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_3_top);
			splash3_top.startAnimation(animation_top);
			
			Animation animation_center=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_3_center);
			splash3_center.startAnimation(animation_center);
			
			Animation animation_right=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_3_bottom);
			splash3_bottom.startAnimation(animation_right);
		}
		else if(position==3) {
			Animation animation=AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_4);
			splash4.startAnimation(animation);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getExtraTime() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("client_time", ""+System.currentTimeMillis());
		client.get(SplashActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/getsystemtime", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				ParamsManager.extratime=JsonParse.getExtraTime(new String(responseBody));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}});
	}
	
	public class SplashPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(views.get(position));
			return views.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	};
	
	private void openIndex() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getExtraTime();
				Intent intent=new Intent(SplashActivity.this, MainActivity2.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
}
