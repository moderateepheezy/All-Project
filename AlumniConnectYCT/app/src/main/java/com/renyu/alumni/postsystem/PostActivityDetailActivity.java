package com.renyu.alumni.postsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import com.renyu.alumni.image.ImageShowActivity;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.ImageChoiceModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PostDetailModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.myview.NoScrollGridView;
import com.renyu.alumni.organization.BussinessCardActivity;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PostActivityDetailActivity extends SwipeBackActivity {

	TextView nav_title=null;
	ImageView nav_left_item=null;
	TextView nav_right_item_text=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	CircleImageView activity_detail_userimage=null;
	TextView activity_detail_pubtime=null;
	TextView activity_detail_viewnums=null;
	TextView activity_detail_username=null;
	LinearLayout activity_detail_content_layout=null;
	TextView activity_detail_title=null;
	TextView activity_detail_time=null;
	TextView activity_detail_address=null;
	TextView activity_detail_person=null;
	TextView activity_detail_lasttime=null;
	TextView activity_detail_desp=null;
	NoScrollGridView activity_detail_grid=null;
	ImageGridAdapter imageAdapter=null;
	LinearLayout activity_detail_operation_layout=null;
	TextView activity_detail_operation_text=null;
	
	MyLoadingDialog dialog=null;
	
	PostDetailModel detail_model=null;
	
	ArrayList<ImageChoiceModel> picInfoList=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postactivitydetail);
		
		picInfoList=new ArrayList<ImageChoiceModel>();
		
		bitmapUtils=BitmapHelp.getBitmapUtils(this);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_organization_user_default));	
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("����");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("����");
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> strArrayList=new ArrayList<String>();
				ArrayList<Integer> imageArrayList=new ArrayList<Integer>();
				
				if(CommonUtils.isAppInstalled(PostActivityDetailActivity.this, "com.tencent.mobileqq")) {
					imageArrayList.add(R.drawable.qq_logo);
					strArrayList.add("QQ����");
					imageArrayList.add(R.drawable.qzone_logo);
					strArrayList.add("QQ�ռ�");
				}
				if(CommonUtils.isAppInstalled(PostActivityDetailActivity.this, "com.tencent.mm")) {
					imageArrayList.add(R.drawable.weixin_logo);
					strArrayList.add("΢�ź���");
					imageArrayList.add(R.drawable.weixinpy_logo);
					strArrayList.add("΢������Ȧ");
				}
				if(CommonUtils.isAppInstalled(PostActivityDetailActivity.this, "com.sina.weibo")) {
					imageArrayList.add(R.drawable.weibo_logo);
					strArrayList.add("����΢��");
				}
				Object[] strArrayTemp=(Object[]) strArrayList.toArray();
				final String[] strArray=new String[strArrayTemp.length];
				for(int i=0;i<strArrayTemp.length;i++) {
					strArray[i]=String.valueOf(strArrayTemp[i]);
				}
				Object[] imageArrayTemp=(Object[]) (imageArrayList.toArray());
				final int[] imageArray=new int[imageArrayTemp.length];
				for(int i=0;i<imageArrayTemp.length;i++) {
					imageArray[i]=Integer.valueOf(String.valueOf(imageArrayTemp[i]));
				}
				CommonUtils.showCustomAlertDialog(PostActivityDetailActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						if(detail_model==null) {
							return;
						}
						String imageUrl=detail_model.getPicinfo().size()>0?detail_model.getPicinfo().get(0):"http://morningtel.qiniudn.com/ic_launcher.png";
						if(strArray[pos].equals("QQ����")) {
							CommonUtils.shareQQ(PostActivityDetailActivity.this, detail_model.getActivity_desc(), imageUrl, detail_model.getActivity_name(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("QQ�ռ�")) {
							CommonUtils.shareQQKJ(PostActivityDetailActivity.this, detail_model.getActivity_desc(), imageUrl, detail_model.getActivity_name(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢�ź���")) {
							CommonUtils.shareWeixin(PostActivityDetailActivity.this, detail_model.getActivity_desc(), detail_model.getActivity_name(), "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢������Ȧ")) {
							CommonUtils.shareWeixinPy(PostActivityDetailActivity.this, detail_model.getActivity_desc(), detail_model.getActivity_name(), "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("����΢��")) {
							CommonUtils.shareWeibo(PostActivityDetailActivity.this, detail_model.getActivity_desc(), imageUrl, detail_model.getActivity_name(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
					}});
			}
		});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		activity_detail_userimage=(CircleImageView) findViewById(R.id.activity_detail_userimage);
		activity_detail_userimage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PostActivityDetailActivity.this, BussinessCardActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", detail_model.getUser_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		activity_detail_pubtime=(TextView) findViewById(R.id.activity_detail_pubtime);
		activity_detail_viewnums=(TextView) findViewById(R.id.activity_detail_viewnums);
		activity_detail_username=(TextView) findViewById(R.id.activity_detail_username);
		activity_detail_content_layout=(LinearLayout) findViewById(R.id.activity_detail_content_layout);
		activity_detail_title=(TextView) findViewById(R.id.activity_detail_title);
		activity_detail_time=(TextView) findViewById(R.id.activity_detail_time);
		activity_detail_address=(TextView) findViewById(R.id.activity_detail_address);
		activity_detail_person=(TextView) findViewById(R.id.activity_detail_person);
		activity_detail_lasttime=(TextView) findViewById(R.id.activity_detail_lasttime);
		activity_detail_desp=(TextView) findViewById(R.id.activity_detail_desp);
		activity_detail_grid=(NoScrollGridView) findViewById(R.id.activity_detail_grid);
		imageAdapter=new ImageGridAdapter(PostActivityDetailActivity.this, picInfoList);
		activity_detail_grid.setAdapter(imageAdapter);
		activity_detail_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				//��ʾ��ȫ��ͼƬurl
				final ArrayList<String> imageUrls=new ArrayList<String>();
				final ArrayList<String> contents=new ArrayList<String>();
				for(int i=0;i<picInfoList.size();i++) {
					ImageChoiceModel picModel=picInfoList.get(i);
					imageUrls.add(picModel.getPath());
					contents.add(picModel.getContent());
				}
				
				Intent intent=new Intent(PostActivityDetailActivity.this, ImageShowActivity.class);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("imageUrl", imageUrls);
				bundle.putStringArrayList("content", contents);
				bundle.putInt("currentPage", position);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		activity_detail_operation_layout=(LinearLayout) findViewById(R.id.activity_detail_operation_layout);
		activity_detail_operation_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}});

		activity_detail_operation_text=(TextView) findViewById(R.id.activity_detail_operation_text);
		activity_detail_operation_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserModel uModel=DB.getInstance(PostActivityDetailActivity.this).getUserModel();		
				if(uModel==null) {
					Intent intent=new Intent(PostActivityDetailActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}
				if(detail_model==null) {
					return;
				}
				else {
					if(detail_model.getIs_apply()==0) {
						joinActivity(1);
					}
					else if(detail_model.getIs_apply()==1) {
						joinActivity(0);
					}
				}
			}
		});
		
		loadBeginData();
	}
	
	private void loadBeginData() {
		dialog=CommonUtils.showCustomAlertProgressDialog(PostActivityDetailActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("activity_id", getIntent().getExtras().getInt("activity_id"));
		final UserModel model=DB.getInstance(PostActivityDetailActivity.this).getUserModel();
		Security se=new Security();
		Header[] headers=null;
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getactivityinfo", model.getToken(), PostActivityDetailActivity.this);
			Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getactivityinfo", "null", PostActivityDetailActivity.this);
			Header[] headers_={new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		client.get(PostActivityDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/getactivityinfo", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				detail_model=JsonParse.getPubchannelActivity(new String(responseBody));
				dialog.dismiss();
				if(detail_model==null) {
					CommonUtils.showCustomToast(PostActivityDetailActivity.this, getResources().getString(R.string.network_error), false);
					finish();
					return;
				}
				initUI();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(statusCode==401) {
					JsonParse.showMessage(PostActivityDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(PostActivityDetailActivity.this, getResources().getString(R.string.network_error), false);
					finish();
				}
			}
		});
	}
	
	private void initUI() {	
		activity_detail_content_layout.setVisibility(View.VISIBLE);
		bitmapUtils.display(activity_detail_userimage, ParamsManager.bucketName+detail_model.getAvatar_large()+"?imageView/2/w/200/h/200", config);
		activity_detail_pubtime.setText(CommonUtils.getPulicIndexTimeExtra(Long.parseLong(detail_model.getGenerate_time()+"000")));
		activity_detail_viewnums.setText(detail_model.getView_times()+"�����");
		activity_detail_username.setText(detail_model.getUser_name());
		activity_detail_title.setText(detail_model.getActivity_name());
		activity_detail_time.setText("ʱ�䣺"+CommonUtils.getTimeFormat(Long.parseLong(detail_model.getBegin_time()+"000")));
		activity_detail_address.setText("��ַ��"+detail_model.getAddress());
		activity_detail_person.setText("��֯�ߣ�"+detail_model.getCreate_org());
		if(detail_model.getOffdate().equals("0��")) {
			activity_detail_lasttime.setText("������Ѿ�����");
		}
		else {
			activity_detail_lasttime.setText("���뱨����ֹʱ���ʣ"+detail_model.getOffdate());
		}
		activity_detail_desp.setText(detail_model.getActivity_desc());
		for(int i=0;i<detail_model.getPicinfo().size();i++) {
			ImageChoiceModel imageModel=new ImageChoiceModel();
			imageModel.setPath(detail_model.getPicinfo().get(i));
			imageModel.setId(i);
			picInfoList.add(imageModel);
		}
		imageAdapter.notifyDataSetChanged();
		if(detail_model.getIs_apply()==0) {
			activity_detail_operation_text.setText("��Ҫ����");
		}
		else if(detail_model.getIs_apply()==1) {
			activity_detail_operation_text.setText("�Ѿ�����");
		}
		else if(detail_model.getIs_apply()==2) {
			activity_detail_operation_text.setText("��ǰʱ�䲻�ڻ������");
		}
	}
	
	/**
	 * ������ȡ��
	 * @param join
	 */
	private void joinActivity(final int join) {
		dialog=CommonUtils.showCustomAlertProgressDialog(PostActivityDetailActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("type", join);
		params.put("activity_id", detail_model.getActivity_id());
		UserModel model=DB.getInstance(PostActivityDetailActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "join", model.getToken(), PostActivityDetailActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(PostActivityDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/activity/join", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				JsonParse.showMessage(PostActivityDetailActivity.this, new String(responseBody));
				if(join==0) {
					detail_model.setIs_apply(0);
				}
				else if(join==1) {
					detail_model.setIs_apply(1);
				}
				if(detail_model.getIs_apply()==0) {
					activity_detail_operation_text.setText("��Ҫ����");
				}
				else if(detail_model.getIs_apply()==1) {
					activity_detail_operation_text.setText("�Ѿ�����");
				}
				else if(detail_model.getIs_apply()==2) {
					activity_detail_operation_text.setText("��ǰʱ�䲻�ڻ������");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(PostActivityDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(PostActivityDetailActivity.this, getResources().getString(R.string.network_error), false);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
