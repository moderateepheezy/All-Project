package com.renyu.alumni.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;
import com.renyu.alumni.model.CityModel;

public class AreaChoiceActivity extends FragmentActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	TextView domestic_choice=null;
	TextView international_choice=null;
	FrameLayout area_domestic=null;
	FrameLayout area_international=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_areachoice_new);
		
		init();
	}

	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("����ѡ��");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		domestic_choice=(TextView) findViewById(R.id.domestic_choice);
		domestic_choice.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				area_domestic.setVisibility(View.VISIBLE);
				area_international.setVisibility(View.GONE);
			}});
		international_choice=(TextView) findViewById(R.id.international_choice);	
		international_choice.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				area_domestic.setVisibility(View.GONE);
				area_international.setVisibility(View.VISIBLE);
			}});
		area_domestic=(FrameLayout) findViewById(R.id.area_domestic);
		area_international=(FrameLayout) findViewById(R.id.area_international);
		area_international.setVisibility(View.GONE);
		
		AreaChoiceFragment domesticFragment=AreaChoiceFragment.newInstance(true);
		getSupportFragmentManager().beginTransaction().replace(R.id.area_domestic, domesticFragment).commitAllowingStateLoss();
		AreaChoiceFragment internationalFragment=AreaChoiceFragment.newInstance(false);
		getSupportFragmentManager().beginTransaction().replace(R.id.area_international, internationalFragment).commitAllowingStateLoss();
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
	
	/**
	 * �ص�ѡ�����
	 * @param model
	 */
	public void back(CityModel model) {
		Intent intent=getIntent();
		Bundle bundle=new Bundle();
		bundle.putSerializable("CityModel", model);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}
