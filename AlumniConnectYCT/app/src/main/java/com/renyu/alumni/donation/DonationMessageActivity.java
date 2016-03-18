package com.renyu.alumni.donation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DonationMessageActivity extends Activity {
	
	@InjectView(R.id.nav_title) TextView nav_title;
	@InjectView(R.id.nav_left_item) ImageView nav_left_item;
	@InjectView(R.id.donation_pay_edit) EditText donation_pay_edit;
	@InjectView(R.id.donation_pay_finish) TextView donation_pay_finish;
	@InjectView(R.id.donation_pay_num) TextView donation_pay_num;
		
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_donationmessage);
		ButterKnife.inject(this);
		
		init();
	}
	
	private void init() {
		nav_title.setText("��������");
		nav_left_item.setVisibility(View.VISIBLE);
		
		checkorder();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@OnClick(R.id.nav_left_item)
	public void nav_left_item_click(View view) {
		Intent intent=new Intent(DonationMessageActivity.this, DonationActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.donation_pay_finish)
	public void onPayFinish(View view) {
		if(donation_pay_edit.getText().toString().equals("")) {
			CommonUtils.showCustomToast(DonationMessageActivity.this, "����д��������", false);
			return;
		}
		savedonatedesc();
	}
	
	private void checkorder() {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationMessageActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("donate_order_id", ParamsManager.donate_order_id);
		final UserModel model=DB.getInstance(DonationMessageActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "checkorder", model.getToken(), DonationMessageActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.get(DonationMessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/checkorder", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					HashMap<String, String> map=JsonParse.getCheckResult(new String(responseBody));
					if(Integer.parseInt(map.get("result"))==1) {
						if(Integer.parseInt(map.get("donate_state"))==2) {
							SpannableString string=new SpannableString("�յ���� "+map.get("donate_money")+" Ԫ");
							string.setSpan(new ForegroundColorSpan(Color.parseColor("#f07216")), 5, 5+map.get("donate_money").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							string.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 5, 5+map.get("donate_money").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							donation_pay_num.setText(string);
						}
					}
					else {
						donation_pay_num.setText(map.get("comments"));
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationMessageActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "checkorder", "null", DonationMessageActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.get(DonationMessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/checkorder", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					HashMap<String, String> map=JsonParse.getCheckResult(new String(responseBody));
					if(Integer.parseInt(map.get("result"))==1) {
						if(Integer.parseInt(map.get("donate_state"))==2) {
							SpannableString string=new SpannableString("�յ���� "+map.get("donate_money")+" Ԫ");
							string.setSpan(new ForegroundColorSpan(Color.parseColor("#f07216")), 5, 5+map.get("donate_money").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							string.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 5, 5+map.get("donate_money").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							donation_pay_num.setText(string);
						}
						else if(Integer.parseInt(map.get("donate_state"))==1) {
							donation_pay_num.setText("����֧����");
						}
						else if(Integer.parseInt(map.get("donate_state"))==0) {
							donation_pay_num.setText("֧��ʧ��");
						}
					}
					else {
						donation_pay_num.setText(map.get("comments"));
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationMessageActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
	}
	
	private void savedonatedesc() {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationMessageActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject params=new JSONObject();
		ByteArrayEntity entity=null;
		try {
			params.put("donate_order_id", ParamsManager.donate_order_id);
			params.put("donate_desc", donation_pay_edit.getText().toString());
			entity=new ByteArrayEntity(params.toString().getBytes("UTF-8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final UserModel model=DB.getInstance(DonationMessageActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonatedesc", model.getToken(), DonationMessageActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.post(DonationMessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonatedesc", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(JsonParse.getLoginResult(DonationMessageActivity.this, new String(responseBody))==1) {
						CommonUtils.showCustomToast(DonationMessageActivity.this, "�ύ�ɹ�����л���ľ���", false);
						Intent intent=new Intent(DonationMessageActivity.this, DonationActivity.class);
						startActivity(intent);
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, "�ύʧ��", false);
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationMessageActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonatedesc", "null", DonationMessageActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(DonationMessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonatedesc", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(JsonParse.getLoginResult(DonationMessageActivity.this, new String(responseBody))==1) {
						CommonUtils.showCustomToast(DonationMessageActivity.this, "�ύ�ɹ�����л���ľ���", false);
						Intent intent=new Intent(DonationMessageActivity.this, DonationActivity.class);
						startActivity(intent);
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, "�ύʧ��", false);
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationMessageActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationMessageActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(DonationMessageActivity.this, DonationActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ParamsManager.donate_order_id="";
	}
}
