package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.renyu.alumni.common.AreaChoiceActivity;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.CityModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.myview.SwitchButton;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class UserInfoActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
		
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	LinearLayout ucenter_info_layout=null;
	ImageView ucenter_info_image=null;
	TextView ucenter_info_name=null;
	TextView ucenter_info_year=null;
	TextView ucenter_info_collage=null;
	EditText ucenter_info_compname=null;
	SwitchButton ucenter_info_switch_compname=null;
	EditText ucenter_info_phone=null;
	SwitchButton ucenter_info_switch_phone=null;
	EditText ucenter_info_qq=null;
	SwitchButton ucenter_info_switch_qq=null;
	EditText ucenter_info_email_=null;
	SwitchButton ucenter_info_switch_email=null;
	TextView ucenter_info_city=null;
	SwitchButton ucenter_info_switch_city=null;
	EditText ucenter_info_industry=null;
	SwitchButton ucenter_info_switch_industry=null;
	LinearLayout ucenter_info_password=null;
	TextView ucenter_info_no_layout=null;
	TextView ucenter_info_label=null;
	LinearLayout ucenter_info_label_layout=null;
	
	File cameraFile=null;
	Uri cameraUrl=null;
	//����ͼƬλ��
	String filePath="";
	//Ŀ¼λ��
	String dirPath="";
	//�ļ��������
	File fileOutPut=null;
	UserModel model_=null;
	//ѡ��ĳ���model
	CityModel cmodel=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	MyLoadingDialog dialog=null;
	
	//�Ƿ�������ˢ��
	boolean isChange=false;
	
	ArrayList<String> labels=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ucenter_info);
		
		if(savedInstanceState!=null) {
			filePath=savedInstanceState.getString("filePath");
			ParamsManager.isOpenCount++;
		}
		dirPath=Environment.getExternalStorageDirectory().getPath()+"/Alumni/Thumb";
		
		bitmapUtils=BitmapHelp.getBitmapUtils(UserInfoActivity.this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(UserInfoActivity.this.getResources().getDrawable(R.drawable.ic_user_press));
		config.setLoadingDrawable(UserInfoActivity.this.getResources().getDrawable(R.drawable.ic_user_press));	
		
		labels=new ArrayList<String>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�ҵ�����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�Ƿ��У�ѻ�����ʧ�����ҳ�����
				if(getIntent().getExtras()!=null&&getIntent().getExtras().getBoolean("isNeedBack")) {
					Intent intent=getIntent();
					if(isChange) {
						setResult(RESULT_OK, intent);
					}
				}
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateUserInfo();
			}});
		
		ucenter_info_image=(ImageView) findViewById(R.id.ucenter_info_image);
		ucenter_info_layout=(LinearLayout) findViewById(R.id.ucenter_info_layout);
		ucenter_info_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] strArray={"���", "���"};
				int[] imageArray={R.drawable.ic_photo, R.drawable.ic_camera};
				CommonUtils.showCustomAlertDialog(UserInfoActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						if(pos==0) {
							//����ϵͳ���
							Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							startActivityForResult(intent, ParamsManager.ALBUM);
						}
						else if(pos==1) {
							cameraFile=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
							if(!cameraFile.exists()) {
								try {
									cameraFile.createNewFile();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							cameraUrl=Uri.fromFile(cameraFile);
							filePath=cameraUrl.getPath();
							Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraUrl);
							startActivityForResult(intent, ParamsManager.CAMERA);
						}
					}});
			}});
		ucenter_info_name=(TextView) findViewById(R.id.ucenter_info_name);
		ucenter_info_year=(TextView) findViewById(R.id.ucenter_info_year);
		ucenter_info_collage=(TextView) findViewById(R.id.ucenter_info_collage);
		ucenter_info_compname=(EditText) findViewById(R.id.ucenter_info_compname);
		ucenter_info_switch_compname=(SwitchButton) findViewById(R.id.ucenter_info_switch_compname);
		ucenter_info_phone=(EditText) findViewById(R.id.ucenter_info_phone);
		ucenter_info_switch_phone=(SwitchButton) findViewById(R.id.ucenter_info_switch_phone);
		ucenter_info_qq=(EditText) findViewById(R.id.ucenter_info_qq);
		ucenter_info_switch_qq=(SwitchButton) findViewById(R.id.ucenter_info_switch_qq);
		ucenter_info_email_=(EditText) findViewById(R.id.ucenter_info_email_);
		ucenter_info_switch_email=(SwitchButton) findViewById(R.id.ucenter_info_switch_email);
		ucenter_info_city=(TextView) findViewById(R.id.ucenter_info_city);
		ucenter_info_city.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserInfoActivity.this, AreaChoiceActivity.class);
				startActivityForResult(intent, ParamsManager.LOGIN_CHOICE_AREA);
			}});
		ucenter_info_switch_city=(SwitchButton) findViewById(R.id.ucenter_info_switch_city);
		ucenter_info_industry=(EditText) findViewById(R.id.ucenter_info_industry);
		ucenter_info_switch_industry=(SwitchButton) findViewById(R.id.ucenter_info_switch_industry);
		ucenter_info_password=(LinearLayout) findViewById(R.id.ucenter_info_password);
		ucenter_info_password.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserInfoActivity.this, ModifyPassActivity.class);
				startActivity(intent);
			}});
		ucenter_info_no_layout=(TextView) findViewById(R.id.ucenter_info_no_layout);
		ucenter_info_no_layout.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadUserInfo();
			}});
		ucenter_info_label=(TextView) findViewById(R.id.ucenter_info_label);
		ucenter_info_label_layout=(LinearLayout) findViewById(R.id.ucenter_info_label_layout);
		ucenter_info_label_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(UserInfoActivity.this, UserLabelActivity.class);
				Bundle bundle=new Bundle();
				String[] str_array=new String[labels.size()];
				for(int i=0;i<labels.size();i++) {
					str_array[i]=labels.get(i);
				}
				bundle.putStringArray("labels", str_array);
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.LOGIN_CHOICE_LABEL);
			}
		});
		
		if(ParamsManager.isOpenCount%2==0&&ParamsManager.isOpenCount!=0) {
			if(!filePath.equals("")) {
				fileOutPut=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
				Intent intent = new Intent("com.android.camera.action.CROP");      
		        intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*"); 
		        intent.putExtra("crop", "true");// crop=true �������ܳ������Ĳü�ҳ��. 
		        intent.putExtra("aspectX", 5);// ������Ϊ�ü���ı���. 
		        intent.putExtra("aspectY", 4);// x:y=1:2 
		        intent.putExtra("output", Uri.fromFile(fileOutPut)); 
		        intent.putExtra("outputFormat", "JPEG");//���ظ�ʽ    
		        startActivityForResult(intent, ParamsManager.CROP);
				
			}
		}
		loadUserInfo();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		//����
		if(arg1==RESULT_OK&&arg0==ParamsManager.CAMERA) {
			if(cameraFile!=null) {
				filePath=cameraFile.getPath();
			}
			else {
				return;
			}
		}
		//���
		else if(arg1==RESULT_OK&&arg0==ParamsManager.ALBUM) {
			Uri uri=arg2.getData();
			if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
				filePath=CommonUtils.getPath(uri, UserInfoActivity.this);
			}
			else {
				String[] projection={MediaStore.Images.Media.DATA};
				Cursor cs=managedQuery(uri, projection, null, null, null);
				int columnNumber=cs.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cs.moveToFirst();
				filePath=cs.getString(columnNumber);
				filePath.replaceAll("file:///", "/");	
			}		
		}
		else if(arg1==RESULT_OK&&arg0==ParamsManager.CROP) {
			if(fileOutPut!=null) {
				bitmapUtils.display(ucenter_info_image, fileOutPut.getPath(), config);
				CommonUtils.prepareImage(UserInfoActivity.this, fileOutPut.getPath(), getResources().getInteger(R.integer.avatar_activity_state));
			}
			return ;
		}
		else if(arg1==RESULT_OK&&arg0==ParamsManager.LOGIN_CHOICE_AREA) {
			cmodel=(CityModel) arg2.getExtras().getSerializable("CityModel");
			ucenter_info_city.setText(cmodel.getCityName());
			return ;
		}
		else if(arg1==RESULT_OK&&arg0==ParamsManager.LOGIN_CHOICE_LABEL) {
			labels=arg2.getExtras().getStringArrayList("labels");
			String str="";
			for(int i=0;i<labels.size();i++) {
				str+=(i==labels.size()-1?labels.get(i):labels.get(i)+"/");
			}
			ucenter_info_label.setText(str);
			return ;
		}
		else {
			return ;
		}
		fileOutPut=new File(dirPath+"/"+System.currentTimeMillis()+".jpg");
		Intent intent = new Intent("com.android.camera.action.CROP");      
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*"); 
        intent.putExtra("crop", "true");// crop=true �������ܳ������Ĳü�ҳ��. 
        intent.putExtra("aspectX", 5);// ������Ϊ�ü���ı���. 
        intent.putExtra("aspectY", 4);// x:y=1:2 
        intent.putExtra("output", Uri.fromFile(fileOutPut)); 
        intent.putExtra("outputFormat", "JPEG");//���ظ�ʽ    
        startActivityForResult(intent, ParamsManager.CROP);      
	}
	
	private void loadUserInfo() {
		if(ParamsManager.isOpenCount==0) {
			dialog=CommonUtils.showCustomAlertProgressDialog(UserInfoActivity.this, "���ڼ���");
		}
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(UserInfoActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getuserinfo", model.getToken(), UserInfoActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(UserInfoActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/getuserinfo", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				if(ParamsManager.isOpenCount==0) {
					dialog.dismiss();
				}
				model_=JsonParse.getCurrentUserModel(new String(responseBody));
				bitmapUtils.display(ucenter_info_image, ParamsManager.bucketName+model_.getAvatar_large(), config);
				
				UserModel umodel=DB.getInstance(UserInfoActivity.this).getUserModel();
				umodel.setAvatar_large(model_.getAvatar_large());
				DB.getInstance(UserInfoActivity.this).updateUserAvatar(umodel);
				
				Intent intent=new Intent();
				intent.setAction("IMAGEUPDATE");
				sendBroadcast(intent);
				
				String label_temp="";
				for(int i=0;i<model_.getPersonal_tags().size();i++) {
					if(i!=model_.getPersonal_tags().size()-1) {
						label_temp+=model_.getPersonal_tags().get(i)+"/";
					}
					else {
						label_temp+=model_.getPersonal_tags().get(i);
					}
				}
				labels.addAll(model_.getPersonal_tags());
				ucenter_info_label.setText(label_temp);
				ucenter_info_name.setText(model_.getUser_name());
				ucenter_info_year.setText(""+model_.getYear());
				ucenter_info_compname.setText(model_.getCompanyname());
				if(model_.getCompany_secret()==1) {
					ucenter_info_switch_compname.setChecked(true);
				}
				else {
					ucenter_info_switch_compname.setChecked(false);
				}
				ucenter_info_phone.setText(model_.getPhonenum());
				if(model_.getPhonenum_secret()==1) {
					ucenter_info_switch_phone.setChecked(true);
				}
				else {
					ucenter_info_switch_phone.setChecked(false);
				}
				ucenter_info_qq.setText(""+model_.getQq());
				if(model_.getQq_secret()==1) {
					ucenter_info_switch_qq.setChecked(true);
				}
				else {
					ucenter_info_switch_qq.setChecked(false);
				}
				ucenter_info_email_.setText(model_.getMail());
				if(model_.getMail_secret()==1) {
					ucenter_info_switch_email.setChecked(true);
				}
				else {
					ucenter_info_switch_email.setChecked(false);
				}
				ucenter_info_city.setText(model_.getCity_name());
				if(model_.getCity_secret()==1) {
					ucenter_info_switch_city.setChecked(true);
				}
				else {
					ucenter_info_switch_city.setChecked(false);
				}
				ucenter_info_industry.setText(model_.getIndustry());
				if(model_.getIndustry_secret()==1) {
					ucenter_info_switch_industry.setChecked(true);
				}
				else {
					ucenter_info_switch_industry.setChecked(false);
				}
				ucenter_info_no_layout.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(ParamsManager.isOpenCount==0) {
					dialog.dismiss();
				}
				ucenter_info_no_layout.setVisibility(View.VISIBLE);
			}
		
		});
	}
	
	private void updateUserInfo() {
		dialog=CommonUtils.showCustomAlertProgressDialog(UserInfoActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(UserInfoActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "update", model.getToken(), UserInfoActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("user_id", model_.getUser_id());
			obj.put("companyname", ucenter_info_compname.getText().toString());
			obj.put("company_secret", !ucenter_info_switch_compname.isChecked()?0:1);
			obj.put("phonenum", ucenter_info_phone.getText().toString());
			obj.put("phonenum_secret", !ucenter_info_switch_phone.isChecked()?0:1);
			obj.put("qq", ucenter_info_qq.getText().toString());
			obj.put("qq_secret", !ucenter_info_switch_qq.isChecked()?0:1);
			obj.put("industry", ucenter_info_industry.getText().toString());
			obj.put("industry_secret", !ucenter_info_switch_industry.isChecked()?0:1);
			obj.put("mail", ucenter_info_email_.getText().toString());
			obj.put("mail_secret", !ucenter_info_switch_email.isChecked()?0:1);
			obj.put("city_name", ucenter_info_city.getText().toString());
			obj.put("city_secret", !ucenter_info_switch_city.isChecked()?0:1);
			JSONArray array=new JSONArray();
			for(int i=0;i<labels.size();i++) {
				array.put(labels.get(i));
			}
			obj.put("personal_tags", array);
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(UserInfoActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/update", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(UserInfoActivity.this, new String(arg2));
					finish();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					if(arg0==401) {
						JsonParse.showMessage(UserInfoActivity.this, new String(arg2));
					}
					else {
						CommonUtils.showCustomToast(UserInfoActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
					isChange=true;
				}
			});
		} catch(Exception e) {
			
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
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if(keyCode==KeyEvent.KEYCODE_BACK) {
//			//�Ƿ��У�ѻ�����ʧ�����ҳ�����
//			if(getIntent().getExtras()!=null&&getIntent().getExtras().getBoolean("isNeedBack")) {
//				Intent intent=getIntent();
//				if(isChange) {
//					setResult(RESULT_OK, intent);
//				}
//			}
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("filePath", filePath);
	}
	
}
