package com.renyu.alumni.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.renyu.alumni.myview.SwitchButton;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DonationUserInfoActivity extends SwipeBackActivity {
	@InjectView(R.id.nav_title) TextView nav_title=null;
	@InjectView(R.id.nav_left_item) ImageView nav_left_item;
	
	@InjectView(R.id.donation_userinfo_name) EditText donation_userinfo_name;
	@InjectView(R.id.donation_userinfo_phone) EditText donation_userinfo_phone;
	@InjectView(R.id.donation_userinfo_email) EditText donation_userinfo_email;
	@InjectView(R.id.donation_userinfo_company) EditText donation_userinfo_company;
	@InjectView(R.id.donation_userinfo_address) EditText donation_userinfo_address;
	@InjectView(R.id.donation_userinfo_zipcode) EditText donation_userinfo_zipcode;
	@InjectView(R.id.donation_userinfo_anonymous) SwitchButton donation_userinfo_anonymous;
	@InjectView(R.id.donation_userinfo_start) TextView donation_userinfo_start;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donationuserinfo);
		ButterKnife.inject(this);
		
		init();
	}

	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title.setText("У����Ϣ");
		nav_left_item.setVisibility(View.VISIBLE);
	}
	
	@OnClick(R.id.nav_left_item)
	public void nav_left_item_click(View view) {
		finish();
	}
	
	@OnClick(R.id.donation_userinfo_start)
	public void donation_userinfo_start_click(View view) {
		if(donation_userinfo_name.getText().toString().equals("")||donation_userinfo_phone.getText().toString().equals("")||donation_userinfo_email.getText().toString().equals("")) {
			CommonUtils.showCustomToast(DonationUserInfoActivity.this, "�뽫У����Ϣ��д����", false);
			return;
		}
		savedonateinfo();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		donation_userinfo_name.requestFocus();
		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(donation_userinfo_name, 0);
	}
	
	private void savedonateinfo() {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationUserInfoActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject params=new JSONObject();
		ByteArrayEntity entity=null;
		try {
			params.put("user_name", donation_userinfo_name.getText().toString());
			params.put("phone_num", donation_userinfo_phone.getText().toString());
			params.put("email", donation_userinfo_email.getText().toString());
			params.put("work_unit", donation_userinfo_company.getText().toString());
			params.put("address", donation_userinfo_address.getText().toString());
			params.put("postcode", donation_userinfo_zipcode.getText().toString());
			params.put("is_anonymous", donation_userinfo_anonymous.isChecked()?"1":"0");
			entity=new ByteArrayEntity(params.toString().getBytes("UTF-8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final UserModel model=DB.getInstance(DonationUserInfoActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonateinfo", model.getToken(), DonationUserInfoActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.post(DonationUserInfoActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonateinfo", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					String donateid=JsonParse.getDonateid(new String(responseBody));
					if(donateid.equals("-1")) {
						CommonUtils.showCustomToast(DonationUserInfoActivity.this, "��ȡ�����Ϣʧ�ܣ�������", false);
					}
					else {
						Intent intent=new Intent(DonationUserInfoActivity.this, DonationPayActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("donateid", donateid);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationUserInfoActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationUserInfoActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonateinfo", "null", DonationUserInfoActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(DonationUserInfoActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonateinfo", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					String donateid=JsonParse.getDonateid(new String(responseBody));
					if(donateid.equals("-1")) {
						CommonUtils.showCustomToast(DonationUserInfoActivity.this, "��ȡ�����Ϣʧ�ܣ�������", false);
					}
					else {
						Intent intent=new Intent(DonationUserInfoActivity.this, DonationPayActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("donateid", donateid);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationUserInfoActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationUserInfoActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
	
	}
	
}
