package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegistFailActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	TextView nav_left_item_text=null;

	TextView regist_fail_desp=null;
	TextView regist_fail_rewrite=null;
	TextView regist_fail_findpassword=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_fail);
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ע��ʧ��");
		nav_left_item_text=(TextView) findViewById(R.id.nav_left_item_text);
		nav_left_item_text.setText("ȡ��");
		nav_left_item_text.setVisibility(View.VISIBLE);
		nav_left_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		regist_fail_desp=(TextView) findViewById(R.id.regist_fail_desp);
		regist_fail_desp.setText(getIntent().getExtras().getString("errorMessage"));
		
		regist_fail_rewrite=(TextView) findViewById(R.id.regist_fail_rewrite);
		regist_fail_rewrite.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=getIntent();
				setResult(RESULT_OK, intent);
				finish();
			}});
		regist_fail_findpassword=(TextView) findViewById(R.id.regist_fail_findpassword);
		regist_fail_findpassword.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistFailActivity.this, ForgetPassActivity.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("needClear", true);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
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

}
