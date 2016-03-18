package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegistOKActivity extends SwipeBackActivity {

	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView regist_ok_name=null;
	TextView regist_ok_year=null;
	TextView regist_ok_speciality=null;
	
	LinearLayout regist_ok_nolayout=null;
	TextView regist_ok_rewrite=null;
	TextView regist_ok_commit=null;
	
	LinearLayout regist_ok_layout=null;
	LinearLayout regist_ok_list=null;
	RelativeLayout regist_ok_cancel_layout=null;
	ImageView regist_ok_cancel=null;
	RelativeLayout regist_ok_later_layout=null;
	ImageView regist_ok_later=null;
	TextView regist_ok_next=null;

	MyLoadingDialog dialog=null;
	
	ArrayList<String> verificationUserList=null;
	//ȫ��radio����
	ArrayList<ImageView> imageviews=null;
	//��ǰѡ�е�position
	int currentPosition=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_ok);
		
		imageviews=new ArrayList<ImageView>();
		verificationUserList=getIntent().getExtras().getStringArrayList("verificationUserList");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ע����֤");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		regist_ok_name=(TextView) findViewById(R.id.regist_ok_name);
		regist_ok_name.setText("������"+getIntent().getExtras().getString("user_name"));
		regist_ok_year=(TextView) findViewById(R.id.regist_ok_year);
		regist_ok_year.setText("ѧ����"+getIntent().getExtras().getString("year"));
		regist_ok_speciality=(TextView) findViewById(R.id.regist_ok_speciality);
		regist_ok_speciality.setText("רҵ��"+getIntent().getExtras().getString("profession"));
		regist_ok_nolayout=(LinearLayout) findViewById(R.id.regist_ok_nolayout);
		regist_ok_layout=(LinearLayout) findViewById(R.id.regist_ok_layout);
		regist_ok_list=(LinearLayout) findViewById(R.id.regist_ok_list);
		if(getIntent().getExtras().getBoolean("find")) {
			regist_ok_layout.setVisibility(View.VISIBLE);
			regist_ok_nolayout.setVisibility(View.GONE);
			loadList();
		}
		else {
			regist_ok_layout.setVisibility(View.GONE);
			regist_ok_nolayout.setVisibility(View.VISIBLE);
		}
		regist_ok_rewrite=(TextView) findViewById(R.id.regist_ok_rewrite);
		regist_ok_rewrite.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		regist_ok_commit=(TextView) findViewById(R.id.regist_ok_commit);
		regist_ok_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(RegistOKActivity.this, VerificationTipActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("user_name", getIntent().getExtras().getString("user_name"));
				bundle.putString("mail", getIntent().getExtras().getString("mail"));
				bundle.putString("password", getIntent().getExtras().getString("password"));
				bundle.putString("year", getIntent().getExtras().getString("year"));
				bundle.putString("xuehao", getIntent().getExtras().getString("xuehao"));
				bundle.putString("profession", getIntent().getExtras().getString("profession"));
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		
		regist_ok_cancel_layout=(RelativeLayout) findViewById(R.id.regist_ok_cancel_layout);
		regist_ok_cancel_layout.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<imageviews.size();i++) {
					if(i==(imageviews.size()-2)) {
						imageviews.get(i).setImageResource(R.drawable.ic_verify_checkbox_select);
					}
					else {
						imageviews.get(i).setImageResource(R.drawable.ic_verify_checkbox_nor);
					}
				}
				currentPosition=imageviews.size()-2;
			}});
		regist_ok_cancel=(ImageView) findViewById(R.id.regist_ok_cancel);
		imageviews.add(regist_ok_cancel);
		regist_ok_later_layout=(RelativeLayout) findViewById(R.id.regist_ok_later_layout);
		regist_ok_later_layout.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<imageviews.size();i++) {
					if(i==(imageviews.size()-1)) {
						imageviews.get(i).setImageResource(R.drawable.ic_verify_checkbox_select);
					}
					else {
						imageviews.get(i).setImageResource(R.drawable.ic_verify_checkbox_nor);
					}
				}
				currentPosition=imageviews.size()-1;
			}});
		regist_ok_later=(ImageView) findViewById(R.id.regist_ok_later);
		imageviews.add(regist_ok_later);
		regist_ok_next=(TextView) findViewById(R.id.regist_ok_next);
		regist_ok_next.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentPosition==-1) {
					CommonUtils.showCustomToast(RegistOKActivity.this, "��ѡ������һ��", false);
					return;
				}
				if(imageviews.size()-1==currentPosition) {
					Intent intent=new Intent(RegistOKActivity.this, VerificationTipActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("user_name", getIntent().getExtras().getString("user_name"));
					bundle.putString("mail", getIntent().getExtras().getString("mail"));
					bundle.putString("password", getIntent().getExtras().getString("password"));
					bundle.putString("year", getIntent().getExtras().getString("year"));
					bundle.putString("xuehao", getIntent().getExtras().getString("xuehao"));
					bundle.putString("profession", getIntent().getExtras().getString("profession"));
					intent.putExtras(bundle);
					startActivity(intent);
				}
				else if(imageviews.size()-2==currentPosition) {
					finish();
				}
				else {
					regist(verificationUserList.get(currentPosition).split("&")[1]);
				}
			}});
	}
	
	private void loadList() {
		
		for(int i=0;i<verificationUserList.size();i++) {
			final int pos=i;
			final String str=verificationUserList.get(i);
			View view=LayoutInflater.from(RegistOKActivity.this).inflate(R.layout.adapter_verificationresult, null);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					currentPosition=pos;
					for(int j=0;j<imageviews.size();j++) {
						if(j==pos) {
							imageviews.get(j).setImageResource(R.drawable.ic_verify_checkbox_select);
						}
						else {
							imageviews.get(j).setImageResource(R.drawable.ic_verify_checkbox_nor);
						}
					}
				}
			});
			ImageView verificationresult_choice=(ImageView) view.findViewById(R.id.verificationresult_choice);
			TextView verificationresult_name=(TextView) view.findViewById(R.id.verificationresult_name);
			verificationresult_name.setText(str.split("&")[0]);
			TextView verificationresult_department=(TextView) view.findViewById(R.id.verificationresult_department);
			verificationresult_department.setText(str.split("&")[2]);
			TextView verificationresult_num=(TextView) view.findViewById(R.id.verificationresult_num);
			verificationresult_num.setText(str.split("&")[1]);
			regist_ok_list.addView(view);
			imageviews.add(verificationresult_choice);
		}
	}
	
	private void regist(final String choosexuehao) {
		dialog=CommonUtils.showCustomAlertProgressDialog(RegistOKActivity.this, "����ע��");
		AsyncHttpClient client=new AsyncHttpClient();
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_name", getIntent().getExtras().getString("user_name"));
			obj.put("mail", getIntent().getExtras().getString("mail"));
			obj.put("password", getIntent().getExtras().getString("password"));
			obj.put("year", getIntent().getExtras().getString("year"));
			obj.put("xuehao", getIntent().getExtras().getString("xuehao"));
			obj.put("profession", getIntent().getExtras().getString("profession"));
			obj.put("choosexuehao", choosexuehao);
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			Security se=new Security();
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "registerone", "null", RegistOKActivity.this);
			Header[] headers={new BasicHeader("Validation", serToken)};
			client.post(RegistOKActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/registerone", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(RegistOKActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(RegistOKActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub					
					int result=JsonParse.getLoginResult(RegistOKActivity.this, new String(arg2));
					
					if(result==1||result==2) {
						UserModel model=JsonParse.getUserModel(new String(arg2));
						//�����Ÿ�����
						ParamsManager.XGLoadTimes=0;
						CommonUtils.registXG(getApplicationContext(), model.getToken());
						//���û���Ϣ��¼�����ݿ����
						DB.getInstance(RegistOKActivity.this).updateUserModel(model);
						//������Ϣ��¼
						MessageManager.getInstance(getApplicationContext()).startMessageUser();
						
						Intent intent=new Intent(RegistOKActivity.this, RegistMoreActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("email", getIntent().getExtras().getString("mail"));
						bundle.putString("user_id", ""+model.getUser_id());
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else if(result==0) {
						Intent intent=new Intent(RegistOKActivity.this, RegistRepeatFailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("errorMessage", JsonParse.getErrorMessage(RegistOKActivity.this, new String(arg2)));
						bundle.putString("mail", getIntent().getExtras().getString("mail"));
						bundle.putString("choosexuehao", choosexuehao);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else if(result==3) {
						JsonParse.showMessage(RegistOKActivity.this, new String(arg2));
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
