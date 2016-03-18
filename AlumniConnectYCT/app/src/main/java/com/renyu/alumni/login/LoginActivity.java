package com.renyu.alumni.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AubDigestUtils;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.common.encrypt.EncodingUtils;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LoginActivity extends SwipeBackActivity {
	
	RelativeLayout nav_layout=null;
	TextView nav_title=null;
	TextView nav_left_item_text=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView login_commit=null;
	TextView login_regist=null;
	TextView login_forget=null;
	EditText login_username=null;
	EditText login_password=null;
	
	MyLoadingDialog dialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("");
		nav_left_item_text=(TextView) findViewById(R.id.nav_left_item_text);
		nav_left_item_text.setText("ȡ��");
		nav_left_item_text.setVisibility(View.VISIBLE);
		nav_left_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_layout=(RelativeLayout) findViewById(R.id.nav_layout);
		nav_layout.setBackgroundColor(Color.TRANSPARENT);
		
		login_commit=(TextView) findViewById(R.id.login_commit);
		login_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(login_username.getText().toString().equals("")||login_password.getText().toString().equals("")) {
					CommonUtils.showCustomToast(LoginActivity.this, "�û��������벻��Ϊ��", false);
				}
				else {
					if(!CommonUtils.isEmail(login_username.getText().toString())&&!CommonUtils.isMobileNO(login_username.getText().toString())) {
						CommonUtils.showCustomToast(LoginActivity.this, "��������ȷ����������ֻ������ʽ���е�¼", false);
						return;
					}
					login();
				}				
			}});
		login_regist=(TextView) findViewById(R.id.login_regist);
		login_regist.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
			}});
		login_forget=(TextView) findViewById(R.id.login_forget);
		login_forget.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this, ForgetPassActivity.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("needClear", false);
				bundle.putString("login_username", login_username.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		login_username=(EditText) findViewById(R.id.login_username);
		login_password=(EditText) findViewById(R.id.login_password);
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}
	
	private void login() {
		dialog=CommonUtils.showCustomAlertProgressDialog(LoginActivity.this, "���ڵ�½");
		String credentials=AuthorizationConfig.BASICPRE+EncodingUtils.toBase64(login_username.getText().toString()+':'+AubDigestUtils.encodeSHA256Hex(AuthorizationConfig.PSWPRE+login_password.getText().toString()));
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "login", "null", LoginActivity.this);
		Header[] headers={new BasicHeader("Authorization", credentials), new BasicHeader("Validation", serToken)};
		client.get(LoginActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/login", headers, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				if(arg0==401) {
					JsonParse.showMessage(LoginActivity.this, new String(arg2));
				}
				else {
					CommonUtils.showCustomToast(LoginActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(LoginActivity.this, new String(arg2));
				UserModel model=JsonParse.getUserModel(new String(arg2));
				//�����Ÿ�����
				ParamsManager.XGLoadTimes=0;
				CommonUtils.registXG(getApplicationContext(), model.getToken());
				//���û���Ϣ��¼�����ݿ����
				DB.getInstance(LoginActivity.this).updateUserModel(model);
				//������Ϣ��¼
				MessageManager.getInstance(getApplicationContext()).startMessageUser();
				
				Intent intent=getIntent();
				setResult(RESULT_OK, intent);
				finish();
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
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Bundle bundle=intent.getExtras();
		if(bundle.getString("from").equals("RegistMoreActivity")) {
			setResult(RESULT_OK, intent);
			finish();
		}
		else if(bundle.getString("from").equals("RegistRepeatFailActivity")) {
			Intent intent_=new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent_);
		}
		else if(bundle.getString("from").equals("ForgetPassActivity")) {
			
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
