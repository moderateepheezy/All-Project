package com.renyu.alumni.ucenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SuggestActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	EditText suggest_content=null;
	EditText suggest_method=null;
	TextView suggest_commit=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("���ڴ��Ĺ��ܺ����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		suggest_content=(EditText) findViewById(R.id.suggest_content);
		suggest_method=(EditText) findViewById(R.id.suggest_method);
		UserModel model=DB.getInstance(SuggestActivity.this).getUserModel();
		if(model!=null) {
			suggest_method.setVisibility(View.GONE);
		}
		else {
			suggest_method.setVisibility(View.VISIBLE);
		}
		suggest_commit=(TextView) findViewById(R.id.suggest_commit);
		suggest_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserModel model=DB.getInstance(SuggestActivity.this).getUserModel();
				if(model!=null) {
					if(suggest_content.getText().toString().equals("")) {
						CommonUtils.showCustomToast(SuggestActivity.this, "��������д�������", false);
						return;
					}
				}
				else {
					if(suggest_content.getText().toString().equals("")||suggest_method.getText().toString().equals("")) {
						CommonUtils.showCustomToast(SuggestActivity.this, "��������д�����������ϵ��ʽ", false);
						return;
					}
				}
				suggest();
				finish();
			}});
	}
	
	private void suggest() {
		CommonUtils.showCustomToast(SuggestActivity.this, "��л���Ľ���", true);
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject obj=new JSONObject();
		try {
			obj.put("advice", suggest_content.getText().toString());
			obj.put("contacts_way", suggest_method.getText().toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			UserModel model=DB.getInstance(SuggestActivity.this).getUserModel();
			if(model!=null) {
				Security se=new Security();
				String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "advice", model.getToken(), SuggestActivity.this);
				Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
				client.post(SuggestActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/person/advice", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						System.out.println(new String(responseBody));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						
					}});
			}
			else {
				Security se=new Security();
				String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "advice", "null", SuggestActivity.this);
				Header[] headers={new BasicHeader("Validation", serToken)};
				client.post(SuggestActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/person/advice", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						System.out.println(new String(responseBody));
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						
					}});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
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
