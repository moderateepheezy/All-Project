package com.renyu.alumni.organization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.renyu.alumni.model.CreateclassinfoModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.ReviewModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ReviewClassActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	RelativeLayout organization_detail_result=null;
	ImageView organization_review_class_image=null;
	TextView organization_review_class_title=null;
	TextView organization_review_class_otherinfo=null;
	TextView organization_review_class_year=null;
	TextView organization_review_class_degree=null;
	TextView organization_review_class_department=null;
	TextView organization_review_class_class=null;
	TextView organization_review_class_handmaster=null;
	TextView reviewclass_flag_text=null;
	ImageView reviewclass_flag_image=null;
	LinearLayout organization_review_class_nohandmaster=null;
	TextView organization_review_class_notext=null;
	LinearLayout organization_review_class_layout=null;
	LinearLayout organization_review_class_left=null;
	LinearLayout organization_review_class_right=null;

	MyLoadingDialog dialog=null;
	
	CreateclassinfoModel model=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewclass);
		
		model=(CreateclassinfoModel) getIntent().getExtras().getSerializable("model");
		
		bitmapUtils=BitmapHelp.getBitmapUtils(ReviewClassActivity.this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));	
		
		init();
		
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�༶����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		organization_detail_result=(RelativeLayout) findViewById(R.id.organization_detail_result);
		organization_detail_result.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ReviewClassActivity.this, BussinessCardActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", Integer.parseInt(model.getUser_id()));
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		organization_review_class_image=(ImageView) findViewById(R.id.organization_review_class_image);
		organization_review_class_title=(TextView) findViewById(R.id.organization_review_class_title);
		organization_review_class_otherinfo=(TextView) findViewById(R.id.organization_review_class_otherinfo);
		organization_review_class_year=(TextView) findViewById(R.id.organization_review_class_year);
		organization_review_class_degree=(TextView) findViewById(R.id.organization_review_class_degree);
		organization_review_class_department=(TextView) findViewById(R.id.organization_review_class_department);
		organization_review_class_class=(TextView) findViewById(R.id.organization_review_class_class);
		organization_review_class_handmaster=(TextView) findViewById(R.id.organization_review_class_handmaster);
		reviewclass_flag_text=(TextView) findViewById(R.id.reviewclass_flag_text);
		reviewclass_flag_image=(ImageView) findViewById(R.id.reviewclass_flag_image);
		organization_review_class_nohandmaster=(LinearLayout) findViewById(R.id.organization_review_class_nohandmaster);
		organization_review_class_notext=(TextView) findViewById(R.id.organization_review_class_notext);
		organization_review_class_layout=(LinearLayout) findViewById(R.id.organization_review_class_layout);
		organization_review_class_left=(LinearLayout) findViewById(R.id.organization_review_class_left);
		organization_review_class_left.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				operateNewUser(model.getUser_id(), ""+model.getClass_id(), true, null, "");
			}});
		organization_review_class_right=(LinearLayout) findViewById(R.id.organization_review_class_right);
		organization_review_class_right.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ReviewClassActivity.this, CreateClassRejectActivity.class);
				startActivity(intent);
			}});
		approvalclass();
	}
	
	public void operateNewUser(String user_id, String class_id, boolean isApply, ArrayList<String> array, String refusereason) {
		dialog=CommonUtils.showCustomAlertProgressDialog(ReviewClassActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(ReviewClassActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "createclass", model.getToken(), ReviewClassActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("class_id", class_id);
			obj.put("type", isApply?"1":"0");
			obj.put("create_user_id", user_id);
			if(!isApply) {
				JSONArray obj_array=new JSONArray();
				for(int i=0;i<array.size();i++) {
					obj_array.put(Integer.parseInt(array.get(i)));
				}
				obj.put("refuse_types", obj_array);
				obj.put("refusereason", refusereason);
			}
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(ReviewClassActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/createclass", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					JsonParse.showMessage(ReviewClassActivity.this, new String(responseBody));
					Intent intent=new Intent();
					intent.setAction("refresh");
					sendBroadcast(intent);
					finish();
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(statusCode==401) {
						JsonParse.showMessage(ReviewClassActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(ReviewClassActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ��˰༶��Ϣ
	 */
	private void approvalclass() {
		dialog=CommonUtils.showCustomAlertProgressDialog(ReviewClassActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(ReviewClassActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "approvalclass", model.getToken(), ReviewClassActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(ReviewClassActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/approvalclass/"+this.model.getClass_id(), headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				final ReviewModel model=JsonParse.getReviewModel(new String(responseBody));
				bitmapUtils.display(organization_review_class_image, ParamsManager.bucketName+model.getUmodel().getAvatar_large(), config);
				organization_review_class_title.setText(model.getUmodel().getUser_name());
				organization_review_class_otherinfo.setText(model.getUmodel().getCity_name()+" "+model.getUmodel().getIndustry()+" "+(model.getUmodel().getCompanyname()).trim());
				organization_review_class_year.setText(model.getCmodel().getYear()+"��");
				organization_review_class_degree.setText(model.getCmodel().getDegree_name());
				organization_review_class_department.setText(model.getCmodel().getCollege_name());
				organization_review_class_class.setText(model.getCmodel().getClass_name());
				organization_review_class_handmaster.setText(model.getCmodel().getTeacherName());
				if(model.getCmodel().getTeacher().size()>0) {
					reviewclass_flag_text.setText("��ʵ�ɹ�");
					reviewclass_flag_text.setTextColor(getResources().getColor(R.color.reviewclass_ok));
					reviewclass_flag_image.setImageResource(R.drawable.reviewclass_ok);
					organization_review_class_nohandmaster.setVisibility(View.GONE);
					organization_review_class_layout.setVisibility(View.VISIBLE);
					for(int i=0;i<model.getCmodel().getTeacher().size();i++) {
						View view=LayoutInflater.from(ReviewClassActivity.this).inflate(R.layout.view_reviewclass, null);
						TextView view_reviewclass_year=(TextView) view.findViewById(R.id.view_reviewclass_year);
						view_reviewclass_year.setText(model.getCmodel().getYear()+"��");
						TextView view_reviewclass_college_name=(TextView) view.findViewById(R.id.view_reviewclass_college_name);
						view_reviewclass_college_name.setText(model.getCmodel().getTeacher().get(i).getCollege_name());
						TextView view_reviewclass_teacher=(TextView) view.findViewById(R.id.view_reviewclass_teacher);
						view_reviewclass_teacher.setText(model.getCmodel().getTeacher().get(i).getTeacher());
						organization_review_class_layout.addView(view);
					}
				}
				else {
					reviewclass_flag_text.setText("��ʵʧ��");
					reviewclass_flag_text.setTextColor(getResources().getColor(R.color.organization_detail_text));
					reviewclass_flag_image.setImageResource(R.drawable.reviewclass_fail);
					organization_review_class_nohandmaster.setVisibility(View.VISIBLE);
					organization_review_class_notext.setText(model.getCmodel().getYear()+"������������");
					organization_review_class_layout.setVisibility(View.GONE);
					organization_review_class_nohandmaster.setOnClickListener(new LinearLayout.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ReviewClassActivity.this, ReviewTeacherListActivity.class);
							Bundle bundle=new Bundle();
							bundle.putInt("year", model.getCmodel().getYear());
							bundle.putString("title", model.getCmodel().getYear()+"������������");
							intent.putExtras(bundle);
							startActivity(intent);
						}});
					
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(ReviewClassActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(ReviewClassActivity.this, getResources().getString(R.string.network_error), false);
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
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		ArrayList<String> array=intent.getExtras().getStringArrayList("refuse_types");
		String refusereason=intent.getExtras().getString("refusereason");
		operateNewUser(model.getUser_id(), ""+model.getClass_id(), false, array, refusereason);
	}
}
