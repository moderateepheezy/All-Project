package com.renyu.alumni.ucenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ModifyPassActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	EditText modify_oldpass=null;
	EditText modify_newpass=null;
	EditText modify_newpass_again=null;
	TextView modify_commit=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�޸�����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		modify_oldpass=(EditText) findViewById(R.id.modify_oldpass);
		modify_newpass=(EditText) findViewById(R.id.modify_newpass);
		modify_newpass_again=(EditText) findViewById(R.id.modify_newpass_again);
		modify_commit=(TextView) findViewById(R.id.modify_commit);
		modify_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(modify_oldpass.getText().toString().equals("")
						||modify_newpass.getText().toString().equals("")
						||modify_newpass_again.getText().toString().equals("")) {
					CommonUtils.showCustomToast(ModifyPassActivity.this, "��������д�������", false);
					return;
				}
				if(modify_oldpass.getText().toString().equals(modify_newpass.getText().toString())) {
					CommonUtils.showCustomToast(ModifyPassActivity.this, "������������벻����ͬ", false);
					return;
				}
				if(!modify_newpass.getText().toString().equals(modify_newpass_again.getText().toString())) {
					CommonUtils.showCustomToast(ModifyPassActivity.this, "��������ȷ���������һ��", false);
					return;
				}
				
				modifyPassWord();
			}});
	}
	
	private void modifyPassWord() {
		dialog=CommonUtils.showCustomAlertProgressDialog(ModifyPassActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("oldpassword", modify_oldpass.getText().toString());
		params.add("newpassword", modify_newpass.getText().toString());
		params.add("confirmpassword", modify_newpass_again.getText().toString());
		UserModel model=DB.getInstance(ModifyPassActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "changepwd", model.getToken(), ModifyPassActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(ModifyPassActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/changepwd", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(ModifyPassActivity.this, new String(responseBody));
				finish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(ModifyPassActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(ModifyPassActivity.this, getResources().getString(R.string.network_error), false);
					finish();
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
