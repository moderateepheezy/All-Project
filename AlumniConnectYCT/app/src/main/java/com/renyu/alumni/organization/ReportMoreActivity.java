package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
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

public class ReportMoreActivity extends SwipeBackActivity {

	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	EditText reportmore_edit=null;
	TextView reportmore_commit=null;
	
	ArrayList<String> choiceList=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reportmore);
		
		choiceList=getIntent().getExtras().getStringArrayList("choiceList");
		
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
		
		reportmore_edit=(EditText) findViewById(R.id.reportmore_edit);
		reportmore_commit=(TextView) findViewById(R.id.reportmore_commit);
		reportmore_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reportmore_edit.getText().toString().equals("")) {
					CommonUtils.showCustomToast(ReportMoreActivity.this, "������ٱ���У�ѵ�ԭ��", false);
					return;
				}
				report();
			}});
	}
	
	private void report() {
		dialog=CommonUtils.showCustomAlertProgressDialog(ReportMoreActivity.this, "�����ύ");
		try {
			AsyncHttpClient client=new AsyncHttpClient();
			UserModel umodel=DB.getInstance(ReportMoreActivity.this).getUserModel();
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "report", umodel.getToken(), ReportMoreActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+umodel.getToken()), new BasicHeader("Validation", serToken)};
			JSONArray array=new JSONArray();
			for(int i=0;i<choiceList.size();i++) {
				array.put(Integer.parseInt(choiceList.get(i)));
			}
			JSONObject obj=new JSONObject();
			obj.put("user_id", getIntent().getExtras().getInt("user_id"));
			obj.put("report_types", array);
			obj.put("report_content", reportmore_edit.getText().toString());		
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(ReportMoreActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/report", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					JsonParse.showMessage(ReportMoreActivity.this, new String(responseBody));
					Intent intent=getIntent();
					setResult(RESULT_OK, intent);
					finish();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(ReportMoreActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(ReportMoreActivity.this, getResources().getString(R.string.network_error), false);
					}
					finish();
				}});
		} catch(Exception e) {
			e.printStackTrace();
			dialog.dismiss();
		}
	
	}
}
