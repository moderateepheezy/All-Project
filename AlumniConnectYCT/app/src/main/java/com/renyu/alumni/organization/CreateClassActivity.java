package com.renyu.alumni.organization;

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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CreateClassActivity extends SwipeBackActivity {

	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	TextView createclass_title=null;
	EditText createclass_headmaster_name=null;
	EditText createclass_name=null;
	TextView createclass_commit=null;
	
	String year="";
	String edu="";
	String department="";

	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createclass);
		
		year=getIntent().getExtras().getString("year");
		edu=getIntent().getExtras().getString("edu");
		department=getIntent().getExtras().getString("department");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�����༶");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		createclass_title=(TextView) findViewById(R.id.createclass_title);
		createclass_headmaster_name=(EditText) findViewById(R.id.createclass_headmaster_name);
		createclass_name=(EditText) findViewById(R.id.createclass_name);
		createclass_name.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_GO) {
					if(createclass_name.getText().toString().equals("")||createclass_headmaster_name.getText().toString().equals("")) {
						CommonUtils.showCustomToast(CreateClassActivity.this, "������������༶�����Լ�����������", false);
						return true;
					}
					createClass();
				}
				return true;
			}
		});
		createclass_commit=(TextView) findViewById(R.id.createclass_commit);
		createclass_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(createclass_name.getText().toString().equals("")||createclass_headmaster_name.getText().toString().equals("")) {
					CommonUtils.showCustomToast(CreateClassActivity.this, "������������༶�����Լ�����������", false);
					return;
				}
				createClass();
			}});
		
		createclass_title.setText(year+"�� "+edu+"\n"+department);
	}
	
	private void createClass() {
		dialog=CommonUtils.showCustomAlertProgressDialog(CreateClassActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(CreateClassActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "newclass", model.getToken(), CreateClassActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("year", year);
			obj.put("class_degree", getIntent().getExtras().getString("edu_id"));
			obj.put("college_id", getIntent().getExtras().getString("college_id"));
			obj.put("class_name", createclass_name.getText().toString());
			obj.put("teacher", createclass_headmaster_name.getText().toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(CreateClassActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/newclass", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(CreateClassActivity.this, new String(responseBody));
					dialog.dismiss();
					Intent intent=new Intent(CreateClassActivity.this, SearchClassActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(CreateClassActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(CreateClassActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
				}
			});
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
