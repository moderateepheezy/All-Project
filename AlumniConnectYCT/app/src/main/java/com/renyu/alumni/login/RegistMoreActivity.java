package com.renyu.alumni.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.AreaChoiceActivity;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.model.CityModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.myview.SwitchButton;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

public class RegistMoreActivity extends Activity {

	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	EditText regist_compname=null;
	SwitchButton regist_switch_compname=null;
	EditText regist_phone=null;
	SwitchButton regist_switch_phone=null;
	EditText regist_qq=null;
	SwitchButton regist_switch_qq=null;
	TextView regist_email_=null;
	SwitchButton regist_switch_email=null;
	TextView regist_city=null;
	SwitchButton regist_switch_city=null;
	EditText regist_industry=null;
	SwitchButton regist_switch_industry=null;

	MyLoadingDialog dialog=null;
	
	String email="";
	String user_id="";
	//ѡ��ĳ���model
	CityModel model=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_more);
		
		email=getIntent().getExtras().getString("email");
		user_id=getIntent().getExtras().getString("user_id");
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("������Ϣ");
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("���");
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registMore();
			}});
		
		regist_compname=(EditText) findViewById(R.id.regist_compname);
		regist_switch_compname=(SwitchButton) findViewById(R.id.regist_switch_compname);
		regist_phone=(EditText) findViewById(R.id.regist_phone);
		regist_switch_phone=(SwitchButton) findViewById(R.id.regist_switch_phone);
		regist_qq=(EditText) findViewById(R.id.regist_qq);
		regist_switch_qq=(SwitchButton) findViewById(R.id.regist_switch_qq);
		regist_email_=(TextView) findViewById(R.id.regist_email_);
		regist_email_.setText(email);
		regist_switch_email=(SwitchButton) findViewById(R.id.regist_switch_email);
		regist_city=(TextView) findViewById(R.id.regist_city);
		regist_city.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistMoreActivity.this, AreaChoiceActivity.class);
				startActivityForResult(intent, ParamsManager.LOGIN_CHOICE_AREA);
			}});
		regist_switch_city=(SwitchButton) findViewById(R.id.regist_switch_city);
		regist_industry=(EditText) findViewById(R.id.regist_industry);
		regist_switch_industry=(SwitchButton) findViewById(R.id.regist_switch_industry);
	}
	
	private void registMore() {
		dialog=CommonUtils.showCustomAlertProgressDialog(RegistMoreActivity.this, "����ע��");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_id", user_id);
			obj.put("companyname", regist_compname.getText().toString());
			obj.put("company_secret", !regist_switch_compname.isChecked()?0:1);
			obj.put("phonenum", regist_phone.getText().toString());
			obj.put("phonenum_secret", !regist_switch_phone.isChecked()?0:1);
			obj.put("qq", regist_qq.getText().toString());
			obj.put("qq_secret", !regist_switch_qq.isChecked()?0:1);
			obj.put("industry", regist_industry.getText().toString());
			obj.put("industry_secret", !regist_switch_industry.isChecked()?0:1);
			obj.put("mail_secret", !regist_switch_email.isChecked()?0:1);
			if(model==null) {
				obj.put("city_code", -1);
				obj.put("city_name", "");
			}
			else {
				obj.put("city_code", model.getCityCode());
				obj.put("city_name", regist_city.getText().toString());
			}
			obj.put("city_secret", !regist_switch_city.isChecked()?0:1);
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "registertwo", "null", RegistMoreActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(RegistMoreActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/registertwo", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(RegistMoreActivity.this, new String(arg2));
					finishActivity();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(RegistMoreActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(RegistMoreActivity.this, getResources().getString(R.string.network_error), false);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.LOGIN_CHOICE_AREA) {
			model=(CityModel) data.getExtras().getSerializable("CityModel");
			regist_city.setText(model.getCityName());
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void finishActivity() {
		Intent intent=new Intent(RegistMoreActivity.this, LoginActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("from", "RegistMoreActivity");
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
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
