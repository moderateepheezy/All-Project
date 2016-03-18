package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ReportActivity extends SwipeBackActivity {

	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	RelativeLayout report_falseinformation=null;
	CheckBox report_falseinformation_image=null;
	RelativeLayout report_advertising_fraud=null;
	CheckBox report_advertising_fraud_image=null;
	RelativeLayout report_message_harassment=null;
	CheckBox report_message_harassment_image=null;
	RelativeLayout report_sensitive_information=null;
	CheckBox report_sensitive_information_image=null;
	RelativeLayout report_other=null;
	
	
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�ٱ�");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("���");
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(report_falseinformation_image.isChecked()||report_advertising_fraud_image.isChecked()||report_message_harassment_image.isChecked()||report_sensitive_information_image.isChecked()) {
					report();
				}
				else {
					CommonUtils.showCustomToast(ReportActivity.this, "��ѡ������һ�־ٱ�����", false);
				}
			}});
		
		report_falseinformation=(RelativeLayout) findViewById(R.id.report_falseinformation);
		report_falseinformation.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				report_falseinformation_image.setChecked(!report_falseinformation_image.isChecked());
			}});
		report_falseinformation_image=(CheckBox) findViewById(R.id.report_falseinformation_image);
		report_advertising_fraud=(RelativeLayout) findViewById(R.id.report_advertising_fraud);
		report_advertising_fraud.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				report_advertising_fraud_image.setChecked(!report_advertising_fraud_image.isChecked());
			}});
		report_advertising_fraud_image=(CheckBox) findViewById(R.id.report_advertising_fraud_image);
		report_message_harassment=(RelativeLayout) findViewById(R.id.report_message_harassment);
		report_message_harassment.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				report_message_harassment_image.setChecked(!report_message_harassment_image.isChecked());
			}});
		report_message_harassment_image=(CheckBox) findViewById(R.id.report_message_harassment_image);
		report_sensitive_information=(RelativeLayout) findViewById(R.id.report_sensitive_information);
		report_sensitive_information.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				report_sensitive_information_image.setChecked(!report_sensitive_information_image.isChecked());
			}});
		report_sensitive_information_image=(CheckBox) findViewById(R.id.report_sensitive_information_image);
		report_other=(RelativeLayout) findViewById(R.id.report_other);
		report_other.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> choiceList=new ArrayList<String>();
				if(report_falseinformation_image.isChecked()) {
					choiceList.add("1");
				}
				if(report_advertising_fraud_image.isChecked()) {
					choiceList.add("2");
				}
				if(report_message_harassment_image.isChecked()) {
					choiceList.add("3");
				}
				if(report_sensitive_information_image.isChecked()) {
					choiceList.add("4");
				}
				Intent intent=new Intent(ReportActivity.this, ReportMoreActivity.class);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("choiceList", choiceList);
				bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.ORGANIZATION_REPORT);
			}});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.ORGANIZATION_REPORT) {
			finish();
		}
	}
	
	private void report() {
		dialog=CommonUtils.showCustomAlertProgressDialog(ReportActivity.this, "�����ύ");
		try {
			ArrayList<String> choiceList=new ArrayList<String>();
			if(report_falseinformation_image.isChecked()) {
				choiceList.add("1");
			}
			if(report_advertising_fraud_image.isChecked()) {
				choiceList.add("2");
			}
			if(report_message_harassment_image.isChecked()) {
				choiceList.add("3");
			}
			if(report_sensitive_information_image.isChecked()) {
				choiceList.add("4");
			}
			AsyncHttpClient client=new AsyncHttpClient();
			UserModel umodel=DB.getInstance(ReportActivity.this).getUserModel();
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "report", umodel.getToken(), ReportActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			JSONArray array=new JSONArray();
			for(int i=0;i<choiceList.size();i++) {
				array.put(Integer.parseInt(choiceList.get(i)));
			}
			JSONObject obj=new JSONObject();
			obj.put("user_id", getIntent().getExtras().getInt("user_id"));
			obj.put("report_types", array);
			obj.put("report_content", "");		
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(ReportActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/report", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					JsonParse.showMessage(ReportActivity.this, new String(responseBody));
					finish();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(ReportActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(ReportActivity.this, getResources().getString(R.string.network_error), false);
					}
					finish();
				}});
		} catch(Exception e) {
			e.printStackTrace();
			dialog.dismiss();
		}
	
	}
}
