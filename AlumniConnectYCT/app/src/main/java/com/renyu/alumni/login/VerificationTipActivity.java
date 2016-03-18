package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class VerificationTipActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView regist_verification_tip_commit=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification_tip);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ע����֤");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		regist_verification_tip_commit=(TextView) findViewById(R.id.regist_verification_tip_commit);
		regist_verification_tip_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regist();
			}});
	}
	
	private void regist() {
		dialog=CommonUtils.showCustomAlertProgressDialog(VerificationTipActivity.this, "����ע��");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_name", getIntent().getExtras().getString("user_name"));
			obj.put("mail", getIntent().getExtras().getString("mail"));
			obj.put("password", getIntent().getExtras().getString("password"));
			obj.put("year", getIntent().getExtras().getString("year"));
			obj.put("xuehao", getIntent().getExtras().getString("xuehao"));
			obj.put("profession", getIntent().getExtras().getString("profession"));
			obj.put("choosexuehao", "");
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "registerone", "null", VerificationTipActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(VerificationTipActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/registerone", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(VerificationTipActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(VerificationTipActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub					
					int result=JsonParse.getLoginResult(VerificationTipActivity.this, new String(arg2));
					JsonParse.showMessage(VerificationTipActivity.this, new String(arg2));
					if(result==1||result==2) {
						UserModel model=JsonParse.getUserModel(new String(arg2));
						//������Ϣ��¼
						MessageManager.getInstance(getApplicationContext()).startMessageUser();
						//�����Ÿ�����
						ParamsManager.XGLoadTimes=0;
						CommonUtils.registXG(getApplicationContext(), model.getToken());
						//���û���Ϣ��¼�����ݿ����
						DB.getInstance(VerificationTipActivity.this).updateUserModel(model);
						
						Intent intent=new Intent(VerificationTipActivity.this, RegistMoreActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("email", getIntent().getExtras().getString("mail"));
						bundle.putString("user_id", ""+model.getUser_id());
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else if(result==3) {
						JsonParse.showMessage(VerificationTipActivity.this, new String(arg2));
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
