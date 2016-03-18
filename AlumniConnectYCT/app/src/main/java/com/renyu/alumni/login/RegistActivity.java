package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegistActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	TextView nav_left_item_text=null;

	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView regist_commit=null;
	EditText regist_name=null;
	EditText regist_email=null;
	EditText regist_pass=null;
	TextView regist_year=null;
	EditText regist_speciality=null;
	EditText regist_schoolnum=null;

	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ע��");
		nav_left_item_text=(TextView) findViewById(R.id.nav_left_item_text);
		nav_left_item_text.setText("ȡ��");
		nav_left_item_text.setVisibility(View.VISIBLE);
		nav_left_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		regist_commit=(TextView) findViewById(R.id.regist_commit);
		regist_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(regist_name.getText().toString().equals("")||regist_email.getText().toString().equals("")
						||regist_pass.getText().toString().equals("")||regist_year.getText().toString().equals("")
						||regist_speciality.getText().toString().equals("")) {
					CommonUtils.showCustomToast(RegistActivity.this, "ע����Ϣ����Ϊ��", false);
				}
				else {
					if(!CommonUtils.isEmail(regist_email.getText().toString())) {
						CommonUtils.showCustomToast(RegistActivity.this, "�����ʽ����ȷ", false);
					}
					else {
						check();
					}
				}
				
			}});
		
		regist_name=(EditText) findViewById(R.id.regist_name);
		regist_email=(EditText) findViewById(R.id.regist_email);
		regist_pass=(EditText) findViewById(R.id.regist_pass);
		regist_year=(TextView) findViewById(R.id.regist_year);
		regist_year.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistActivity.this, ChoiceSchoolInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("type", "year");
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.LOGIN_CHOICE_YEAR);
			}});
		regist_speciality=(EditText) findViewById(R.id.regist_speciality);
		regist_schoolnum=(EditText) findViewById(R.id.regist_schoolnum);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				regist_name.requestFocus();
				InputMethodManager imm=(InputMethodManager)getSystemService(RegistActivity.this.INPUT_METHOD_SERVICE);
				imm.showSoftInput(regist_name, 0);
			}
		}, 500);
		
	}
	
	private void check() {
		dialog=CommonUtils.showCustomAlertProgressDialog(RegistActivity.this, "����ע��");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("mail", regist_email.getText().toString());
		params.add("user_name", regist_name.getText().toString());
		params.add("xuehao", regist_schoolnum.getText().toString());
		params.add("profession", regist_speciality.getText().toString());
		params.add("year", regist_year.getText().toString());
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "check", "null", RegistActivity.this);
		Header[] headers={new BasicHeader("Validation", serToken)};
		client.get(RegistActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/check", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				int result=JsonParse.getLoginResult(RegistActivity.this, new String(responseBody));
				switch(result) {
				//1 �������Ѿ���ע��ʹ��
				case 1:
					Intent intent1=new Intent(RegistActivity.this, RegistFailActivity.class);
					Bundle bundle1=new Bundle();
					bundle1.putString("errorMessage", JsonParse.getErrorMessage(RegistActivity.this, new String(responseBody)));
					intent1.putExtras(bundle1);
					startActivityForResult(intent1, ParamsManager.LOGIN_CHOICE_EMAIL);
					break;
				//2 û��ƥ�䵽�û���Ϣ
				case 2:
					Intent intent2=new Intent(RegistActivity.this, RegistOKActivity.class);
					Bundle bundle2=new Bundle();
					bundle2.putString("user_name", regist_name.getText().toString());
					bundle2.putString("mail", regist_email.getText().toString());
					bundle2.putString("password", regist_pass.getText().toString());
					bundle2.putString("year", regist_year.getText().toString());
					bundle2.putString("xuehao", regist_schoolnum.getText().toString());
					bundle2.putString("profession", regist_speciality.getText().toString());
					bundle2.putBoolean("find", false);
					intent2.putExtras(bundle2);
					startActivity(intent2);
					break;
				//3 ƥ�䵽�û���Ϣ	
				case 3:
					Intent intent3=new Intent(RegistActivity.this, RegistOKActivity.class);
					Bundle bundle3=new Bundle();
					bundle3.putString("user_name", regist_name.getText().toString());
					bundle3.putString("mail", regist_email.getText().toString());
					bundle3.putString("password", regist_pass.getText().toString());
					bundle3.putString("year", regist_year.getText().toString());
					bundle3.putString("xuehao", regist_schoolnum.getText().toString());
					bundle3.putString("profession", regist_speciality.getText().toString());
					bundle3.putBoolean("find", true);
					bundle3.putStringArrayList("verificationUserList", JsonParse.getVerificationUserList(new String(responseBody)));
					intent3.putExtras(bundle3);
					startActivity(intent3);
					break;
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(RegistActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(RegistActivity.this, getResources().getString(R.string.network_error), false);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) {
			if(requestCode==ParamsManager.LOGIN_CHOICE_YEAR) {
				regist_year.setText(data.getExtras().getString("year"));
				regist_speciality.requestFocus();
			}
			else if(requestCode==ParamsManager.LOGIN_CHOICE_EMAIL) {
				regist_email.requestFocus();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						InputMethodManager imm=(InputMethodManager)getSystemService(RegistActivity.this.INPUT_METHOD_SERVICE);
						imm.showSoftInput(regist_email, 0);
					}
				}, 500);
			}
		}
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
