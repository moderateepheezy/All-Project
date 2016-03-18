package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class VerificationResultActivity extends SwipeBackActivity {
	
	String name="";
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	TextView verificationresult_name=null;
	LinearLayout verificationresult_list=null;
	TextView verificationresult_no_layout=null;
	LinearLayout verificationresult_layout=null; 
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verificationresult);
		
		name=getIntent().getExtras().getString("name");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("��֤");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		verificationresult_name=(TextView) findViewById(R.id.verificationresult_name);
		verificationresult_name.setText(name);
		verificationresult_list=(LinearLayout) findViewById(R.id.verificationresult_list);
		
		verificationresult_no_layout=(TextView) findViewById(R.id.verificationresult_no_layout);
		verificationresult_layout=(LinearLayout) findViewById(R.id.verificationresult_layout);
		
		getcertifier();
	}
	
	private void getcertifier() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("certifier_name", name);
		UserModel model=DB.getInstance(VerificationResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getcertifier", model.getToken(), VerificationResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(VerificationResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/getcertifier", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				ArrayList<String> verificationUserList=JsonParse.getVerificationUserList(new String(responseBody));
				if(verificationUserList.size()==0) {
					verificationresult_no_layout.setVisibility(View.VISIBLE);
				}
				else {
					verificationresult_layout.setVisibility(View.VISIBLE);
				}
				for(int i=0;i<verificationUserList.size();i++) {
					final String str=verificationUserList.get(i);
					View view=LayoutInflater.from(VerificationResultActivity.this).inflate(R.layout.adapter_verificationresult, null);
					view.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							sendEmail(str.split("&")[1]);
						}
					});
					TextView verificationresult_name=(TextView) view.findViewById(R.id.verificationresult_name);
					verificationresult_name.setText(str.split("&")[0]);
					TextView verificationresult_department=(TextView) view.findViewById(R.id.verificationresult_department);
					verificationresult_department.setText(str.split("&")[2]);
					TextView verificationresult_num=(TextView) view.findViewById(R.id.verificationresult_num);
					verificationresult_num.setText(str.split("&")[1]);
					verificationresult_list.addView(view);
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(VerificationResultActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(VerificationResultActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		});
	}
	
	private void sendEmail(String xuehao) {
		dialog=CommonUtils.showCustomAlertProgressDialog(VerificationResultActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("xuehao", xuehao);
		UserModel model=DB.getInstance(VerificationResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "sendmail", model.getToken(), VerificationResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(VerificationResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/sendmail", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(VerificationResultActivity.this, new String(responseBody));
				if(JsonParse.getLoginResult(VerificationResultActivity.this, new String(responseBody))==1) {
					Intent intent=new Intent(VerificationResultActivity.this, VerificationResultWaitActivity.class);
					startActivity(intent);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(VerificationResultActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(VerificationResultActivity.this, getResources().getString(R.string.network_error), false);
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
