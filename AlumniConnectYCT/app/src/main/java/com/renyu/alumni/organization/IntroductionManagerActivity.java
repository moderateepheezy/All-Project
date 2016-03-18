package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class IntroductionManagerActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	TextView reportmore_edit_title=null;
	EditText reportmore_edit=null;
	TextView reportmore_commit=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reportmore);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�༭���");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		reportmore_edit_title=(TextView) findViewById(R.id.reportmore_edit_title);
		reportmore_edit_title.setText(getIntent().getExtras().getString("title")+"���");
		reportmore_edit=(EditText) findViewById(R.id.reportmore_edit);
		if(!getIntent().getExtras().getString("content").equals("���޼��")) {
			reportmore_edit.setText(getIntent().getExtras().getString("content"));
		}
		reportmore_edit.setHint("������100������");
		reportmore_edit.addTextChangedListener(tw);
		reportmore_commit=(TextView) findViewById(R.id.reportmore_commit);
		reportmore_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reportmore_edit.getText().toString().equals("")) {
					CommonUtils.showCustomToast(IntroductionManagerActivity.this, "�������У�ѻ�������", false);
					return;
				}
				updatedesc();
			}});
		
	}

	TextWatcher tw=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			int maxLen=100;
			Editable editable=reportmore_edit.getText();  
	        int len=editable.length();  
	        if(len>maxLen) {  
	            int selEndIndex=Selection.getSelectionEnd(editable);  
	            String str=editable.toString();  
	            //��ȡ���ַ���  
	            String newStr=str.substring(0,maxLen);  
	            reportmore_edit.setText(newStr);  
	            editable=reportmore_edit.getText();  
	              
	            //���ַ����ĳ���  
	            int newLen=editable.length();  
	            //�ɹ��λ�ó����ַ�������  
	            if(selEndIndex>newLen) {  
	                selEndIndex=editable.length();  
	            }  
	            //�����¹�����ڵ�λ��  
	            Selection.setSelection(editable, selEndIndex);  
	        }
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * У�ѻ����Ա�༭У�ѻ���
	 */
	private void updatedesc() {
		try {
			AsyncHttpClient client=new AsyncHttpClient();
			UserModel umodel=DB.getInstance(IntroductionManagerActivity.this).getUserModel();
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "updatedesc", umodel.getToken(), IntroductionManagerActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			JSONObject obj=new JSONObject();
			obj.put("aluassociation_id", getIntent().getExtras().getString("aluassociation_id"));
			obj.put("aluassociation_desc", reportmore_edit.getText().toString());				
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(IntroductionManagerActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/updatedesc", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(IntroductionManagerActivity.this, new String(responseBody));
					Intent intent=getIntent();
					Bundle bundle=new Bundle();
					bundle.putString("intro", reportmore_edit.getText().toString());
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(IntroductionManagerActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(IntroductionManagerActivity.this, getResources().getString(R.string.network_error), false);
					}
				}});
		} catch(Exception e) {
			
		}
	
	}
}
