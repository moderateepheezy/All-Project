package com.renyu.alumni.organization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.image.ImageShowActivity;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.AluassociationinfoModel;
import com.renyu.alumni.model.BussinessCardModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.myview.PullScrollView;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.ucenter.MessageActivity;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BussinessCardActivity extends SwipeBackActivity implements PullScrollView.OnTurnListener {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	ImageView nav_right_item=null;
	
	CircleImageView bussinesscard_userimage=null;
	ImageView bussinesscard_userimage_verify=null;
	TextView bussinesscard_name=null;
	LinearLayout bussinesscard_class_layout=null;
	LinearLayout bussiness_card_oper_layout=null;
	LinearLayout bussinesscard_favourite_layout=null;
	ImageView bussinesscard_favourite_image=null;
	TextView bussinesscard_favourite_text=null;
	LinearLayout bussinesscard_talk_layout=null;
	TextView bussinesscard_comp=null;
	TextView bussinesscard_industry=null;
	TextView bussinesscard_city=null;
	TextView bussinesscard_qq=null;
	TextView bussinesscard_mail=null;
	TextView bussinesscard_phone=null;
	LinearLayout bussiness_card_alumni_layout=null;

	int user_id=-1;
	//�Ƿ��Ѿ��ղ�
	int ftype=-1;
	BussinessCardModel cmodel=null;
	
	MyLoadingDialog dialog=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bussinesscard);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(BussinessCardActivity.this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));		
		
		user_id=getIntent().getExtras().getInt("user_id");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("��Ƭ");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item=(ImageView) findViewById(R.id.nav_right_item);
		UserModel model=DB.getInstance(BussinessCardActivity.this).getUserModel();
		if(model.getUser_id()!=user_id) {
			nav_right_item.setVisibility(View.VISIBLE);
		}
		nav_right_item.setImageResource(R.drawable.ic_bussiness_choice);
		nav_right_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] strArray={"�ٱ�"};
				CommonUtils.showCustomAlertDialog(BussinessCardActivity.this, strArray, null, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(BussinessCardActivity.this, ReportActivity.class);
						Bundle bundle=new Bundle();
						bundle.putInt("user_id", user_id);
						intent.putExtras(bundle);
						startActivity(intent);
					}});	
			}});
		
		bussinesscard_userimage=(CircleImageView) findViewById(R.id.bussinesscard_userimage);
		bussinesscard_userimage.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//��ʾ��ȫ��ͼƬurl
				final ArrayList<String> imageUrls=new ArrayList<String>();
				imageUrls.add(ParamsManager.bucketName+cmodel.getAvatar_large());
				final ArrayList<String> contents=new ArrayList<String>();
				contents.add("");
				Intent intent=new Intent(BussinessCardActivity.this, ImageShowActivity.class);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("imageUrl", imageUrls);
				bundle.putStringArrayList("content", contents);
				bundle.putInt("currentPage", 0);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		bussinesscard_userimage_verify=(ImageView) findViewById(R.id.bussinesscard_userimage_verify);
		bussinesscard_name=(TextView) findViewById(R.id.bussinesscard_name);
		bussinesscard_class_layout=(LinearLayout) findViewById(R.id.bussinesscard_class_layout);
		bussiness_card_oper_layout=(LinearLayout) findViewById(R.id.bussiness_card_oper_layout);
		bussinesscard_favourite_layout=(LinearLayout) findViewById(R.id.bussinesscard_favourite_layout);
		bussinesscard_favourite_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ftype==1) {
					storeState(0);
				}
				else if(ftype==0) {
					storeState(1);
				}
			}});
		bussinesscard_favourite_image=(ImageView) findViewById(R.id.bussinesscard_favourite_image);
		bussinesscard_favourite_text=(TextView) findViewById(R.id.bussinesscard_favourite_text);
		bussinesscard_talk_layout=(LinearLayout) findViewById(R.id.bussinesscard_talk_layout);
		bussinesscard_talk_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cmodel==null) {
					Intent intent=new Intent(BussinessCardActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}

				Intent intent=new Intent(BussinessCardActivity.this, MessageActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", cmodel.getUser_id());
				bundle.putString("username", cmodel.getUser_name());
				bundle.putString("avatar_large", cmodel.getAvatar_large());
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		bussinesscard_comp=(TextView) findViewById(R.id.bussinesscard_comp);
		bussinesscard_industry=(TextView) findViewById(R.id.bussinesscard_industry);
		bussinesscard_city=(TextView) findViewById(R.id.bussinesscard_city);
		bussinesscard_qq=(TextView) findViewById(R.id.bussinesscard_qq);
		bussinesscard_mail=(TextView) findViewById(R.id.bussinesscard_mail);
		bussinesscard_phone=(TextView) findViewById(R.id.bussinesscard_phone);
		bussiness_card_alumni_layout=(LinearLayout) findViewById(R.id.bussiness_card_alumni_layout);
		loadUserData();
	}
	
	private void loadClassView(ArrayList<String> str, final ArrayList<AluassociationinfoModel> models, String type) {
		if(type.equals("classinfo")) {
			bussinesscard_class_layout.removeAllViews();
			for(int i=0;i<str.size();i++) {
				View view=LayoutInflater.from(BussinessCardActivity.this).inflate(R.layout.view_bussinesscard_class, null);
				TextView bussiness_new_class_text=(TextView) view.findViewById(R.id.bussiness_new_class_text);
				bussiness_new_class_text.setText(str.get(i));
				bussinesscard_class_layout.addView(view);
			}
		}
		else if(type.equals("aluassociationinfo")) {
			bussiness_card_alumni_layout.removeAllViews();
			for(int i=0;i<models.size();i++) {
				final int pos=i;
				View view=LayoutInflater.from(BussinessCardActivity.this).inflate(R.layout.view_bussinesscard_item, null);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(BussinessCardActivity.this, OrganizationDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("type", "AluassociationinfoModel");
						bundle.putSerializable("AluassociationinfoModel", models.get(pos));
						if(models.get(pos).getAluassociation_role_state()==3) {
							bundle.putBoolean("isNew", true);
						}
						else {
							bundle.putBoolean("isNew", false);
						}
						bundle.putBoolean("refreshCurrent", true);
						bundle.putBoolean("isBussinessCard", true);
						intent.putExtras(bundle);
						startActivity(intent);
					}});
				ImageView bussinesscard_item_classimage=(ImageView) view.findViewById(R.id.bussinesscard_item_classimage);
				bussinesscard_item_classimage.setVisibility(View.VISIBLE);
				TextView bussinesscard_item_classname=(TextView) view.findViewById(R.id.bussinesscard_item_classname);
				bussinesscard_item_classname.setText(models.get(i).getAluassociation_name());
				bussinesscard_item_classname.setVisibility(View.VISIBLE);
				bussiness_card_alumni_layout.addView(view);
			}
		}
	}
	
	private void loadUserData() {
		dialog=CommonUtils.showCustomAlertProgressDialog(BussinessCardActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		final UserModel model=DB.getInstance(BussinessCardActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "userinfo", model.getToken(), BussinessCardActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(BussinessCardActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/userinfo/"+user_id, headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				cmodel=JsonParse.getBussinessCardModel(new String(responseBody));
				bitmapUtils.display(bussinesscard_userimage, ParamsManager.bucketName+cmodel.getAvatar_large(), config);
				if(cmodel.getIs_authentication()==1) {
					bussinesscard_userimage_verify.setVisibility(View.GONE);
				}
				else {
					bussinesscard_userimage_verify.setVisibility(View.VISIBLE);
					bussinesscard_userimage_verify.setImageResource(R.drawable.ic_user_verification_no);
				}
				if(cmodel.getStore_state()==1) {
					bussinesscard_favourite_image.setImageResource(R.drawable.ic_bussinesscard_favourite_on);
					bussinesscard_favourite_text.setText("�Ѽ�����Ƭ��");
					ftype=1;
				}
				else {
					bussinesscard_favourite_image.setImageResource(R.drawable.ic_bussinesscard_favourite);
					bussinesscard_favourite_text.setText("������Ƭ��");
					ftype=0;
				}
				bussinesscard_name.setText(cmodel.getUser_name());
				if(model.getUser_id()==cmodel.getUser_id()) {
					bussiness_card_oper_layout.setVisibility(View.GONE);
				}
				else {
					bussiness_card_oper_layout.setVisibility(View.VISIBLE);
				}
				bussinesscard_comp.setText(cmodel.getCompanyname());
				bussinesscard_industry.setText("");
				bussinesscard_city.setText(cmodel.getCity_name());
				bussinesscard_qq.setText(cmodel.getQq());
				bussinesscard_mail.setText(cmodel.getMail());
				bussinesscard_phone.setText(cmodel.getPhonenum());
				bussinesscard_industry.setText(cmodel.getIndustry());
				if(cmodel.getAluassociationinfos().size()>0) {
					loadClassView(null, cmodel.getAluassociationinfos(), "aluassociationinfo");
				}
				if(cmodel.getClassinfos().size()>0) {
					loadClassView(cmodel.getClassinfos(), null, "classinfo");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(BussinessCardActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(BussinessCardActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		});
	}

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		System.out.println("onTurn");
	}
	
	private void storeState(final int type) {
		dialog=CommonUtils.showCustomAlertProgressDialog(BussinessCardActivity.this, "�����ύ");
		final UserModel model=DB.getInstance(BussinessCardActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "store", model.getToken(), BussinessCardActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("store_user_id", ""+user_id);
		params.add("type", ""+type);
		client.get(BussinessCardActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/store", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(BussinessCardActivity.this, new String(responseBody));
				if(type==1) {
					bussinesscard_favourite_image.setImageResource(R.drawable.ic_bussinesscard_favourite_on);
					bussinesscard_favourite_text.setText("�Ѽ�����Ƭ��");
				}
				else {
					bussinesscard_favourite_image.setImageResource(R.drawable.ic_bussinesscard_favourite);
					bussinesscard_favourite_text.setText("������Ƭ��");
				}
				ftype=type;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(BussinessCardActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(BussinessCardActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		});
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
