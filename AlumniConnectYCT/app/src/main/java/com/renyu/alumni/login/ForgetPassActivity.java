package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.myview.MyLoadingDialog;

import org.apache.http.Header;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ForgetPassActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	EditText forget_pass=null;
	TextView forget_commit=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpass);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�һ�����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		forget_pass=(EditText) findViewById(R.id.forget_pass);
		forget_pass.setText(getIntent().getExtras().getString("login_username"));
		forget_pass.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_SEND) {
					if(!forget_pass.getText().toString().equals("")) {
						if(!CommonUtils.isEmail(forget_pass.getText().toString())&&!CommonUtils.isMobileNO(forget_pass.getText().toString())) {
							CommonUtils.showCustomToast(ForgetPassActivity.this, "��������ȷ����������ֻ������ʽ", false);
							return true;
						}
						sendEmail();
					}
					else {
						CommonUtils.showCustomToast(ForgetPassActivity.this, "��������������֤����", false);
					}
				}
				return true;
			}
		});
		forget_commit=(TextView) findViewById(R.id.forget_commit);
		forget_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!forget_pass.getText().toString().equals("")) {
					if(!CommonUtils.isEmail(forget_pass.getText().toString())&&!CommonUtils.isMobileNO(forget_pass.getText().toString())) {
						CommonUtils.showCustomToast(ForgetPassActivity.this, "��������ȷ����������ֻ������ʽ", false);
						return;
					}
					sendEmail();
				}
				else {
					CommonUtils.showCustomToast(ForgetPassActivity.this, "��������������֤����", false);
				}
			}});
	}
	
	private void sendEmail() {
		dialog=CommonUtils.showCustomAlertProgressDialog(ForgetPassActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("university_id", ParamsManager.university_id);
		params.add("email", forget_pass.getText().toString());
		client.get("http://112.126.70.71:7050/getpasswd", params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(ForgetPassActivity.this, new String(responseBody));
				if(getIntent().getExtras().getBoolean("needClear")) {
					Intent intent=new Intent(ForgetPassActivity.this, LoginActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("from", "ForgetPassActivity");
					intent.putExtras(bundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				else {
					finish();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(ForgetPassActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(ForgetPassActivity.this, getResources().getString(R.string.network_error), false);
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
