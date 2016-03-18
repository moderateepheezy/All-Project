package com.renyu.alumni.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.alipay.AliPayActivty;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PayModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.wxapi.PayActivity;

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

public class DonationPayActivity extends SwipeBackActivity {
	
	@InjectView(R.id.nav_title) TextView nav_title=null;
	@InjectView(R.id.nav_left_item) ImageView nav_left_item;
	@InjectView(R.id.donation_money) EditText donation_money;
	@InjectView(R.id.donation_pay_alipay) LinearLayout donation_pay_alipay;
	@InjectView(R.id.donation_pay_alipay_image) ImageView donation_pay_alipay_image;
	@InjectView(R.id.donation_pay_weixinpay) LinearLayout donation_pay_weixinpay;
	@InjectView(R.id.donation_pay_weixinpay_image) ImageView donation_pay_weixinpay_image;
	@InjectView(R.id.donation_pay_start) TextView donation_pay_start=null;
	
	SwipeBackLayout mSwipeBackLayout=null;

	MyLoadingDialog dialog=null;
	
	int pay_type=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donationpay);
		ButterKnife.inject(this);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title.setText("У����Ϣ");
		nav_left_item.setVisibility(View.VISIBLE);
		
		donation_pay_alipay.performClick();
	}
	
	@OnClick(R.id.nav_left_item)
	public void nav_left_item_click(View view) {
		finish();
	}
	
	@OnClick(R.id.donation_pay_alipay)
	public void donation_pay_alipay_click(View view) {
		donation_pay_alipay_image.setImageResource(R.drawable.ic_pay_choice);
		donation_pay_weixinpay_image.setImageResource(R.drawable.ic_pay_normal);
		
		pay_type=1;
	}
	
	@OnClick(R.id.donation_pay_weixinpay)
	public void donation_pay_weixinpay_click(View view) {
		donation_pay_alipay_image.setImageResource(R.drawable.ic_pay_normal);
		donation_pay_weixinpay_image.setImageResource(R.drawable.ic_pay_choice);
		
		pay_type=2;
	}
	
	@OnClick(R.id.donation_pay_start) 
	public void donation_pay_start_click(View view) {
		if(pay_type==-1) {
			return;
		}
		if(donation_money.getText().toString().equals("")) {
			CommonUtils.showCustomToast(DonationPayActivity.this, "������������", false);
			return;
		}
		savedonateorder();
	}
	
	private void savedonateorder() {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationPayActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject params=new JSONObject();
		ByteArrayEntity entity=null;
		try {
			params.put("donateid", getIntent().getExtras().getString("donateid"));
			params.put("donate_config_id", "1");
			params.put("donate_money", donation_money.getText().toString());
			params.put("pay_type", ""+pay_type);
			entity=new ByteArrayEntity(params.toString().getBytes("UTF-8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final UserModel model=DB.getInstance(DonationPayActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonateorder", model.getToken(), DonationPayActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.post(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonateorder", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(JsonParse.getDonate_order_id(new String(responseBody)).equals("")) {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����������Ϣʧ�ܣ�������", false);
					}
					else {
						if(pay_type==2) {
							getWXPayParam(JsonParse.getDonate_order_id(new String(responseBody)));
						}
						else if(pay_type==1) {
							getAliPayParam(JsonParse.getDonate_order_id(new String(responseBody)));
						}
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "savedonateorder", "null", DonationPayActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/savedonateorder", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(JsonParse.getDonate_order_id(new String(responseBody)).equals("")) {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����������Ϣʧ�ܣ�������", false);
					}
					else {
						if(pay_type==2) {
							getWXPayParam(JsonParse.getDonate_order_id(new String(responseBody)));
						}
						else if(pay_type==1) {
							getAliPayParam(JsonParse.getDonate_order_id(new String(responseBody)));
						}
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		
	}
	
	private void getWXPayParam(final String donate_order_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationPayActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("donate_order_id", donate_order_id);
		final UserModel model=DB.getInstance(DonationPayActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getWXPayParam", model.getToken(), DonationPayActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.get(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/getWXPayParam", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					PayModel model=JsonParse.getWeixinPayModel(new String(responseBody));
					if(model!=null) {
						Intent intent=new Intent(DonationPayActivity.this, PayActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("appid", model.getAppid());
						bundle.putString("noncestr", model.getNoncestr());
						bundle.putString("package", model.getPackage_());
						bundle.putString("partnerid", model.getPartnerid());
						bundle.putString("prepayid", model.getPrepayid());
						bundle.putString("sign", model.getSign());
						bundle.putString("timestamp", model.getTimestamp());
						intent.putExtras(bundle);
						startActivity(intent);
						
						ParamsManager.donate_order_id=donate_order_id;
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����Ԥ֧����Ϣʧ�ܣ�������", false);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getWXPayParam", "null", DonationPayActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.get(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/getWXPayParam", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					PayModel model=JsonParse.getWeixinPayModel(new String(responseBody));
					if(model!=null) {
						Intent intent=new Intent(DonationPayActivity.this, PayActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("appid", model.getAppid());
						bundle.putString("noncestr", model.getNoncestr());
						bundle.putString("package", model.getPackage_());
						bundle.putString("partnerid", model.getPartnerid());
						bundle.putString("prepayid", model.getPrepayid());
						bundle.putString("sign", model.getSign());
						bundle.putString("timestamp", model.getTimestamp());
						intent.putExtras(bundle);
						startActivity(intent);
						
						ParamsManager.donate_order_id=donate_order_id;
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����Ԥ֧����Ϣʧ�ܣ�������", false);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
	}
	
	private void getAliPayParam(final String donate_order_id) {
		dialog=CommonUtils.showCustomAlertProgressDialog(DonationPayActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("donate_order_id", donate_order_id);
		final UserModel model=DB.getInstance(DonationPayActivity.this).getUserModel();
		Security se=new Security();
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getAliPayParam", model.getToken(), DonationPayActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			client.get(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/getAliPayParam", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					String alipay_str=JsonParse.getAliPayModel(new String(responseBody));
					if(alipay_str.equals("")) {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����Ԥ֧����Ϣʧ�ܣ�������", false);
					}
					else {
						Intent intent=new Intent(DonationPayActivity.this, AliPayActivty.class);
						Bundle bundle=new Bundle();
						bundle.putString("payInfo", alipay_str);
						intent.putExtras(bundle);
						startActivity(intent);
						
						ParamsManager.donate_order_id=donate_order_id;
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getAliPayParam", "null", DonationPayActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.get(DonationPayActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/donate/getAliPayParam", headers, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					String alipay_str=JsonParse.getAliPayModel(new String(responseBody));
					if(alipay_str.equals("")) {
						CommonUtils.showCustomToast(DonationPayActivity.this, "��ȡ����Ԥ֧����Ϣʧ�ܣ�������", false);
					}
					else {
						Intent intent=new Intent(DonationPayActivity.this, AliPayActivty.class);
						Bundle bundle=new Bundle();
						bundle.putString("payInfo", alipay_str);
						intent.putExtras(bundle);
						startActivity(intent);
						
						ParamsManager.donate_order_id=donate_order_id;
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(DonationPayActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(DonationPayActivity.this, getResources().getString(R.string.network_error), false);
						finish();
					}
				}
			});
		}
	}
}
