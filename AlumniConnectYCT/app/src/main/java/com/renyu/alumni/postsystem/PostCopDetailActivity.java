package com.renyu.alumni.postsystem;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eyeem.chips.BubbleSpan;
import com.eyeem.chips.ChipsTextView;
import com.eyeem.chips.DefaultBubbles;
import com.eyeem.chips.Linkify;
import com.eyeem.chips.Regex;
import com.eyeem.chips.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.MessageManager;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.ImageChoiceModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.PostCommentsModel;
import com.renyu.alumni.model.PostCopDetailModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.myview.NoScrollGridView;
import com.renyu.alumni.organization.BussinessCardActivity;
import com.renyu.alumni.receiver.CustomPushReceiver;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.ucenter.MessageActivity;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PostCopDetailActivity extends SwipeBackActivity {

	TextView nav_title=null;
	ImageView nav_left_item=null;
	TextView nav_right_item_text=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	PullToRefreshListView activity_detail_cop_list=null;
	ListView acturalListview=null;
	PostCommentAdapter adapter=null;
	TextView view_activity_detail_commentnum=null;
	LinearLayout activity_detail_operation_right=null;
	LinearLayout activity_detail_operation_left=null;
	
	ArrayList<ImageChoiceModel> picInfoList=null;
	PostCopDetailModel detail_model=null;
	ArrayList<PostCommentsModel> comments_model=null;
	int page=1;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;

	MyLoadingDialog dialog=null;
	
	boolean isLoading=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postcopdetail);
		
		picInfoList=new ArrayList<ImageChoiceModel>();
		comments_model=new ArrayList<PostCommentsModel>();
		
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
				
				if(CommonUtils.isAppInstalled(PostCopDetailActivity.this, "com.tencent.mobileqq")) {
					imageArrayList.add(R.drawable.qq_logo);
					strArrayList.add("QQ����");
					imageArrayList.add(R.drawable.qzone_logo);
					strArrayList.add("QQ�ռ�");
				}
				if(CommonUtils.isAppInstalled(PostCopDetailActivity.this, "com.tencent.mm")) {
					imageArrayList.add(R.drawable.weixin_logo);
					strArrayList.add("΢�ź���");
					imageArrayList.add(R.drawable.weixinpy_logo);
					strArrayList.add("΢������Ȧ");
				}
				if(CommonUtils.isAppInstalled(PostCopDetailActivity.this, "com.sina.weibo")) {
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
				CommonUtils.showCustomAlertDialog(PostCopDetailActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						if(detail_model==null) {
							return;
						}
						String imageUrl=detail_model.getPicInfoList().size()>0?ParamsManager.bucketName+detail_model.getPicInfoList().get(0):"http://morningtel.qiniudn.com/ic_launcher.png";
						if(strArray[pos].equals("QQ����")) {
							CommonUtils.shareQQ(PostCopDetailActivity.this, detail_model.getResource_content(), imageUrl, detail_model.getResource_title(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("QQ�ռ�")) {
							CommonUtils.shareQQKJ(PostCopDetailActivity.this, detail_model.getResource_content(), imageUrl, detail_model.getResource_title(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢�ź���")) {
							CommonUtils.shareWeixin(PostCopDetailActivity.this, detail_model.getResource_content(), detail_model.getResource_title(), "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("΢������Ȧ")) {
							CommonUtils.shareWeixinPy(PostCopDetailActivity.this, detail_model.getResource_content(), detail_model.getResource_title(), "", "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
						else if(strArray[pos].equals("����΢��")) {
							CommonUtils.shareWeibo(PostCopDetailActivity.this, detail_model.getResource_content(), imageUrl, detail_model.getResource_title(), "http://aga.ustc.edu.cn/ustcapp/index.html");
						}
					}});
			}
		});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		activity_detail_cop_list=(PullToRefreshListView) findViewById(R.id.activity_detail_cop_list);
		activity_detail_cop_list.setMode(Mode.PULL_FROM_END);
		activity_detail_cop_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				loadComments();
			}
		});;
		acturalListview=activity_detail_cop_list.getRefreshableView();
		activity_detail_operation_right=(LinearLayout) findViewById(R.id.activity_detail_operation_right);
		activity_detail_operation_right.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserModel uModel=DB.getInstance(PostCopDetailActivity.this).getUserModel();
				if(uModel==null) {
					Intent intent=new Intent(PostCopDetailActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}
				if(detail_model==null) {
					return;
				}
				Intent intent=new Intent(PostCopDetailActivity.this, SendPostCommentActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("resource_id", getIntent().getExtras().getInt("resource_id"));
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.POSTCOMMENT_REFRESH);
			}});
		activity_detail_operation_left=(LinearLayout) findViewById(R.id.activity_detail_operation_left);
		activity_detail_operation_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserModel uModel=DB.getInstance(PostCopDetailActivity.this).getUserModel();
				if(uModel==null) {
					Intent intent=new Intent(PostCopDetailActivity.this, LoginActivity.class);
					startActivity(intent);
					return;
				}
				if(detail_model==null) {
					return;
				}
				if(uModel!=null&&uModel.getUser_id()==detail_model.getUser_id()) {
					return;
				}
				
				//����ģ����Ϣ
				JSONObject obj=new JSONObject();
				try {
					obj.put("resource_id", ""+getIntent().getExtras().getInt("resource_id"));
					obj.put("resource_title", detail_model.getResource_title());
					obj.put("resource_content", detail_model.getResource_content());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MessageManager.getInstance(PostCopDetailActivity.this).addModualData(obj.toString(), detail_model.getUser_id(), detail_model.getUser_name(), detail_model.getAvatar_large(), CustomPushReceiver.MODUAL_MODEL);				

				Intent intent=new Intent(PostCopDetailActivity.this, MessageActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", detail_model.getUser_id());
				bundle.putString("username", detail_model.getUser_name());
				bundle.putString("avatar_large", detail_model.getAvatar_large());
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		loadResource();
	}
	
	private void loadResource() {
		dialog=CommonUtils.showCustomAlertProgressDialog(PostCopDetailActivity.this, "���ڼ���");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("resource_id", getIntent().getExtras().getInt("resource_id"));
		params.put("pagesize", "20");
		final UserModel model=DB.getInstance(PostCopDetailActivity.this).getUserModel();
		Security se=new Security();
		Header[] headers=null;
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getresource", model.getToken(), PostCopDetailActivity.this);
			Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getresource", "null", PostCopDetailActivity.this);
			Header[] headers_={new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		client.get(PostCopDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/getresource", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				detail_model=JsonParse.getPubchannelResource(new String(responseBody));
				dialog.dismiss();
				if(detail_model==null) {
					CommonUtils.showCustomToast(PostCopDetailActivity.this, getResources().getString(R.string.network_error), false);
					finish();
					return;
				}
				initHeadViewUI();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(statusCode==401) {
					JsonParse.showMessage(PostCopDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(PostCopDetailActivity.this, getResources().getString(R.string.network_error), false);
					finish();
				}
			}
		});
	}
	
	private void initHeadViewUI() {
		View view=LayoutInflater.from(PostCopDetailActivity.this).inflate(R.layout.view_activity_detail_cop, null);
		CircleImageView view_activity_detail_userimage=(CircleImageView) view.findViewById(R.id.view_activity_detail_userimage);
		view_activity_detail_userimage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PostCopDetailActivity.this, BussinessCardActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", detail_model.getUser_id());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		bitmapUtils.display(view_activity_detail_userimage, ParamsManager.bucketName+detail_model.getAvatar_large()+"?imageView/2/w/200/h/200", config);
		TextView view_activity_detail_pubtime=(TextView) view.findViewById(R.id.view_activity_detail_pubtime);
		view_activity_detail_pubtime.setText(CommonUtils.getPulicIndexTimeExtra(Long.parseLong(detail_model.getGenerate_time()+"000")));
		TextView view_activity_detail_viewnums=(TextView) view.findViewById(R.id.view_activity_detail_viewnums);
		view_activity_detail_viewnums.setText(detail_model.getView_times()+"�����");
		TextView view_activity_detail_username=(TextView) view.findViewById(R.id.view_activity_detail_username);
		view_activity_detail_username.setText(detail_model.getUser_name());
		TextView view_activity_detail_grade=(TextView) view.findViewById(R.id.view_activity_detail_grade);
		String grade=detail_model.getYear()+"�� "+detail_model.getCollege_name();
		if(!grade.trim().equals("��")) {
			view_activity_detail_grade.setText(grade.trim());
			view_activity_detail_grade.setVisibility(View.VISIBLE);
		}
		TextView view_activity_detail_industry=(TextView) view.findViewById(R.id.view_activity_detail_industry);
		String industry=detail_model.getCity_name()+" "+detail_model.getIndustry();
		if(!industry.trim().equals("")) {
			view_activity_detail_industry.setText(industry);
			view_activity_detail_industry.setVisibility(View.VISIBLE);
		}
		ChipsTextView view_activity_detail_lable_value=(ChipsTextView) view.findViewById(R.id.view_activity_detail_lable_value);
		TextPaint paint = new TextPaint();
	    Resources r = getResources();
	    float _dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
	    paint.setAntiAlias(true);
	    paint.setTextSize(_dp);
	    view_activity_detail_lable_value.setTextPaint(paint);
	    view_activity_detail_lable_value.setOnBubbleClickedListener(new ChipsTextView.OnBubbleClickedListener() {
	    	@Override
	        public void onBubbleClicked(View view, BubbleSpan bubbleSpan) {
	        	System.out.println(((Linkify.Entity) bubbleSpan.data()).text);
	         }
	    });
	    ArrayList<String> tags=new ArrayList<String>();
	    String tag=detail_model.getResource_tags();
	    if(tag.indexOf(",")!=-1) {
	    	for(int i=0;i<tag.split(",").length;i++) {
		    	tags.add(tag.split(",")[i]);
		    }
	    	update(view_activity_detail_lable_value, tags);
	    }
		TextView view_activity_detail_title=(TextView) view.findViewById(R.id.view_activity_detail_title);
		view_activity_detail_title.setText(detail_model.getResource_title());
		TextView view_activity_detail_desp=(TextView) view.findViewById(R.id.view_activity_detail_desp);
		view_activity_detail_desp.setText(detail_model.getResource_content());
		NoScrollGridView view_activity_detail_grid=(NoScrollGridView) view.findViewById(R.id.view_activity_detail_grid);
		for(int i=0;i<detail_model.getPicInfoList().size();i++) {
			ImageChoiceModel imageModel=new ImageChoiceModel();
 			imageModel.setPath(ParamsManager.bucketName+detail_model.getPicInfoList().get(i));
			imageModel.setId(i);
			picInfoList.add(imageModel);
		}
		ImageGridAdapter imageAdapter=new ImageGridAdapter(PostCopDetailActivity.this, picInfoList);
		view_activity_detail_grid.setAdapter(imageAdapter);
		view_activity_detail_commentnum=(TextView) view.findViewById(R.id.view_activity_detail_commentnum);
		view_activity_detail_commentnum.setText("���ۣ�"+detail_model.getCommentscount()+"��");
		acturalListview.addHeaderView(view);
		adapter=new PostCommentAdapter(PostCopDetailActivity.this, comments_model);
		acturalListview.setAdapter(adapter);
		loadComments();
	}

	private void loadComments() {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("resource_id", getIntent().getExtras().getInt("resource_id"));
		params.put("page", ""+page);
		params.put("pagesize", "20");
		final UserModel model=DB.getInstance(PostCopDetailActivity.this).getUserModel();
		Security se=new Security();
		Header[] headers=null;
		if(model!=null) {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getcomments", model.getToken(), PostCopDetailActivity.this);
			Header[] headers_={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		else {
			String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getcomments", "null", PostCopDetailActivity.this);
			Header[] headers_={new BasicHeader("Validation", serToken)};
			headers=headers_;
		}
		client.get(PostCopDetailActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/pubchannel/getcomments", headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				ArrayList<PostCommentsModel> models=JsonParse.getPubchannelComments(new String(responseBody));
				if(page==1) {
					comments_model.clear();
				}
				if(models.size()>0) {
					comments_model.addAll(models);
					adapter.notifyDataSetChanged();
					page++;
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(PostCopDetailActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(PostCopDetailActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				isLoading=false;
				activity_detail_cop_list.onRefreshComplete();
			}
		});
	}
	
	public void update(ChipsTextView view_activity_detail_lable_value, ArrayList<String> tags) {
		// scan to find bubble matches and populate text view accordingly
		String flattenedText="";
		for(int i=0;i<tags.size();i++) {
			flattenedText+="[a:"+tags.get(i)+"] ";
		}
		Linkify.Entities entities = new Linkify.Entities();
		if (!TextUtils.isEmpty(flattenedText)) {
			Matcher matcher = Regex.VALID_BUBBLE.matcher(flattenedText);
	        while (matcher.find()) {
	        	String bubbleText = matcher.group(1);
	            Linkify.Entity entity = new Linkify.Entity(matcher.start(), matcher.end(), bubbleText, bubbleText, Linkify.Entity.ALBUM);
	            entities.add(entity);
	        }
	    }
		// now bubblify text edit
	    SpannableStringBuilder ssb = new SpannableStringBuilder(flattenedText);
	    for (Linkify.Entity e : entities) {
	    	Utils.bubblify(ssb, e.text, e.start, e.end, view_activity_detail_lable_value.getWidth() - view_activity_detail_lable_value.getPaddingLeft() - view_activity_detail_lable_value.getPaddingRight(), DefaultBubbles.get(DefaultBubbles.BLUE, this, view_activity_detail_lable_value.getTextSize()), null, e);
	    }
	    view_activity_detail_lable_value.setText(ssb);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==ParamsManager.POSTCOMMENT_REFRESH) {
			page=1;
			loadComments();
			int num=detail_model.getCommentscount();
			detail_model.setCommentscount(num+1);
			view_activity_detail_commentnum.setText("���ۣ�"+detail_model.getCommentscount()+"��");
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
