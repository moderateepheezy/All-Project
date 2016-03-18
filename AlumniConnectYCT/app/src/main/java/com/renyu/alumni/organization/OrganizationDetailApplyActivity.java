package com.renyu.alumni.organization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.renyu.alumni.model.AluassociationinfoModel;
import com.renyu.alumni.model.ApprovaluserModel;
import com.renyu.alumni.model.ClassInfoModel;
import com.renyu.alumni.model.ClassUserModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrganizationDetailApplyActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	LinearLayout organization_detail_apply_ok=null;
	LinearLayout organization_detail_apply_cancel=null;
	ImageView organization_detail_apply_image=null;
	ImageView organization_detail_apply_image_verify=null;
	TextView organization_detail_apply_name=null;
	TextView organization_detail_apply_classinfo=null;
	TextView organization_detail_apply_industry=null;
	LinearLayout organization_detail_apply_clicklayout=null;
	ImageView organization_detail_apply_class_image=null;
	TextView organization_detail_apply_class_title=null;
	TextView organization_detail_apply_class_otherinfo=null;
	TextView organization_detail_apply_reason=null;
	
	ClassUserModel cmodel=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organizationdetail_apply);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(OrganizationDetailApplyActivity.this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));	
		
		cmodel=(ClassUserModel) getIntent().getExtras().getSerializable("model");
		
		init();
	}

	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			nav_title.setText("����༶����");
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			nav_title.setText("����У�ѻ�����");
		}
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		String city=cmodel.getCity_name();
		String industry=cmodel.getIndustry();
		String companyname=cmodel.getCompanyname();
		organization_detail_apply_name=(TextView) findViewById(R.id.organization_detail_apply_name);
		organization_detail_apply_name.setText(cmodel.getUser_name());
		organization_detail_apply_classinfo=(TextView) findViewById(R.id.organization_detail_apply_classinfo);
		if(cmodel.getYear().equals("")&&cmodel.getClass_name().equals("")) {
			organization_detail_apply_classinfo.setVisibility(View.GONE);
		}
		else {
			organization_detail_apply_classinfo.setVisibility(View.VISIBLE);
			organization_detail_apply_classinfo.setText(cmodel.getYear()+"��"+cmodel.getClass_name());
		}
		organization_detail_apply_industry=(TextView) findViewById(R.id.organization_detail_apply_industry);
		if(city.equals("")&&industry.equals("")&&companyname.equals("")) {
			organization_detail_apply_industry.setVisibility(View.GONE);
		}
		else {
			organization_detail_apply_industry.setVisibility(View.VISIBLE);
			organization_detail_apply_industry.setText((city+" "+industry+" "+companyname).trim());
		}
		organization_detail_apply_image=(ImageView) findViewById(R.id.organization_detail_apply_image);
		bitmapUtils.display(organization_detail_apply_image, ParamsManager.bucketName+cmodel.getAvatar_large(), config);
		organization_detail_apply_image_verify=(ImageView) findViewById(R.id.organization_detail_apply_image_verify);
		if(cmodel.getIs_authentication()==1) {
			organization_detail_apply_image_verify.setVisibility(View.GONE);
		}
		else {
			organization_detail_apply_image_verify.setVisibility(View.VISIBLE);
			organization_detail_apply_image_verify.setImageResource(R.drawable.ic_user_verification_no);
		}
		organization_detail_apply_clicklayout=(LinearLayout) findViewById(R.id.organization_detail_apply_clicklayout);
		organization_detail_apply_clicklayout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(OrganizationDetailApplyActivity.this, BussinessCardActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", cmodel.getUser_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		organization_detail_apply_class_image=(ImageView) findViewById(R.id.organization_detail_apply_class_image);
		organization_detail_apply_class_title=(TextView) findViewById(R.id.organization_detail_apply_class_title);
		organization_detail_apply_class_otherinfo=(TextView) findViewById(R.id.organization_detail_apply_class_otherinfo);
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			ClassInfoModel data_obj=(ClassInfoModel) getIntent().getExtras().getSerializable("ClassInfoModel");
			organization_detail_apply_class_title.setText(data_obj.getClass_name());
			organization_detail_apply_class_otherinfo.setText("�����ˣ�"+data_obj.getAdmin_name()+"  �Ѽ��룺"+data_obj.getStudent_num()+"��");
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			AluassociationinfoModel data_obj=(AluassociationinfoModel) getIntent().getExtras().getSerializable("AluassociationinfoModel");
			organization_detail_apply_class_title.setText(data_obj.getAluassociation_name());
			organization_detail_apply_class_otherinfo.setText("����Ա��"+data_obj.getAdmin_name()+"  �Ѽ��룺"+data_obj.getAluassociation_num()+"��");
		}
		organization_detail_apply_reason=(TextView) findViewById(R.id.organization_detail_apply_reason);
		
		organization_detail_apply_ok=(LinearLayout) findViewById(R.id.organization_detail_apply_ok);
		organization_detail_apply_ok.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(OrganizationDetailApplyActivity.this).setTitle("��ʾ").setMessage(getIntent().getExtras().getString("desp")).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent=getIntent();
						Bundle bundle=new Bundle();
						bundle.putBoolean("oper", true);
						bundle.putSerializable("model", cmodel);
						intent.putExtras(bundle);
						setResult(RESULT_OK, intent);
						finish();
					}}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).show();
				
			}});
		organization_detail_apply_cancel=(LinearLayout) findViewById(R.id.organization_detail_apply_cancel);
		organization_detail_apply_cancel.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(OrganizationDetailApplyActivity.this, OrganizationDetailRejectActivity.class);
				startActivity(intent);
			}});
		
		loadDetailApply();
	}
	
	private void loadDetailApply() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(OrganizationDetailApplyActivity.this).getUserModel();
		Security se=new Security();
		if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
			ClassInfoModel data_obj=(ClassInfoModel) getIntent().getExtras().getSerializable("ClassInfoModel");
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "approvalclassuser", model.getToken(), OrganizationDetailApplyActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			params.add("class_id", ""+data_obj.getClass_id());
			params.add("apply_user_id", ""+cmodel.getUser_id());
			client.get(OrganizationDetailApplyActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/approvalclassuser", headers, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					ApprovaluserModel model_=JsonParse.getApprovaluserModel(new String(responseBody), "ClassInfoModel");
					organization_detail_apply_reason.setText(model_.getApply_validation());
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailApplyActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailApplyActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
		else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
			AluassociationinfoModel data_obj=(AluassociationinfoModel) getIntent().getExtras().getSerializable("AluassociationinfoModel");
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "approvalaluuser", model.getToken(), OrganizationDetailApplyActivity.this);
			Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			params.add("aluassociation_id", ""+data_obj.getAluassociation_id());
			params.add("apply_user_id", ""+cmodel.getUser_id());
			client.get(OrganizationDetailApplyActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/approvalaluuser", headers, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					ApprovaluserModel model_=JsonParse.getApprovaluserModel(new String(responseBody), "AluassociationinfoModel");
					organization_detail_apply_reason.setText(model_.getApply_validation());
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					if(statusCode==401) {
						JsonParse.showMessage(OrganizationDetailApplyActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(OrganizationDetailApplyActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			});
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent_) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent_);
		Intent intent=getIntent();
		Bundle bundle=new Bundle();
		bundle.putBoolean("oper", false);
		bundle.putSerializable("model", cmodel);
		bundle.putBoolean("checked", intent_.getExtras().getBoolean("checked"));
		bundle.putString("refusereason", intent_.getExtras().getString("refusereason"));
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}
