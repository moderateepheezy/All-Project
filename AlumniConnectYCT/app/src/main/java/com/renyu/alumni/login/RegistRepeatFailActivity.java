package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.myview.MyLoadingDialog;

import org.apache.http.Header;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegistRepeatFailActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;

	TextView regist_fail_desp=null;
	TextView regist_fail_rewrite=null;
	TextView regist_fail_findpassword=null;	
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_fail);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ע��ʧ��");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		regist_fail_desp=(TextView) findViewById(R.id.regist_fail_desp);
		regist_fail_desp.setText(getIntent().getExtras().getString("errorMessage"));
		
		regist_fail_rewrite=(TextView) findViewById(R.id.regist_fail_rewrite);
		regist_fail_rewrite.setText("��Ҫ����");
		regist_fail_rewrite.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				representations();
			}});
		regist_fail_findpassword=(TextView) findViewById(R.id.regist_fail_findpassword);
		regist_fail_findpassword.setText("����ע��");
		regist_fail_findpassword.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistRepeatFailActivity.this, LoginActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("from", "RegistRepeatFailActivity");
				intent.putExtras(bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}});
	}
	
	private void representations() {
		dialog=CommonUtils.showCustomAlertProgressDialog(RegistRepeatFailActivity.this, "����ע��");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("university_id", "0");
		params.add("email", getIntent().getExtras().getString("mail"));
		params.add("user_id", getIntent().getExtras().getString("choosexuehao"));
		client.get(RegistRepeatFailActivity.this, "http://112.126.70.71:7050/shensu", params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(RegistRepeatFailActivity.this, new String(responseBody));
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(RegistRepeatFailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(RegistRepeatFailActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
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
}
