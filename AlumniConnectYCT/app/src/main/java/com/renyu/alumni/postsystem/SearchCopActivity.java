package com.renyu.alumni.postsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.AreaChoiceActivity;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.CityModel;
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
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchCopActivity extends SwipeBackActivity {

	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	EditText searchcop_industry=null;
	EditText searchcop_company=null;
	EditText searchcop_keyword=null;
	TextView searchcop_findcity=null;
	TextView searchcop_commit=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchcop);
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("������");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		searchcop_findcity=(TextView) findViewById(R.id.searchcop_findcity);
		searchcop_findcity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(searchcop_industry.getWindowToken(), 0);
				
				Intent intent=new Intent(SearchCopActivity.this, AreaChoiceActivity.class);
				startActivityForResult(intent, ParamsManager.LOGIN_CHOICE_AREA);
			}
		});
		searchcop_industry=(EditText) findViewById(R.id.searchcop_industry);
		searchcop_company=(EditText) findViewById(R.id.searchcop_company);
		searchcop_keyword=(EditText) findViewById(R.id.searchcop_keyword);
		searchcop_commit=(TextView) findViewById(R.id.searchcop_commit);
		searchcop_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchman();
			}});
	}
	
	private void searchman() {
		dialog=CommonUtils.showCustomAlertProgressDialog(SearchCopActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		final UserModel model=DB.getInstance(SearchCopActivity.this).getUserModel();
		Security se=new Security();
		Header[] headers=null;
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "searchman", model.getToken(), SearchCopActivity.this);
			Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "searchman", "null", SearchCopActivity.this);
			Header[] headers_={new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		JSONObject obj=new JSONObject();
		ByteArrayEntity entity=null;
		try {
			obj.put("industry", searchcop_industry.getText().toString());
			obj.put("companyname", searchcop_company.getText().toString());
			obj.put("city_name", searchcop_findcity.getText().toString());
			obj.put("otherskey", searchcop_keyword.getText().toString());
			entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.post(SearchCopActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/searchman", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				ArrayList<UserModel> models=JsonParse.getSearchMan(new String(responseBody));
				if(models.size()>0) {
					Intent intent=new Intent(SearchCopActivity.this, SearchCopResultActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("searchman", models);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				else {
					CommonUtils.showCustomToast(SearchCopActivity.this, "û�в�ѯ�����У����Ϣ��������", false);
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(statusCode==401) {
					JsonParse.showMessage(SearchCopActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(SearchCopActivity.this, getResources().getString(R.string.network_error), false);
					finish();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.LOGIN_CHOICE_AREA) {
			CityModel model=(CityModel) data.getExtras().getSerializable("CityModel");
			searchcop_findcity.setText(model.getCityName());
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
