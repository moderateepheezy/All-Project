package com.renyu.alumni.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.myview.MyChoiceLodingDialog.OnDialogItemClickListener;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class NewsDetailActivity extends SwipeBackActivity {

	SwipeBackLayout mSwipeBackLayout=null;
	
	ImageView nav_left_item=null;
	TextView nav_title=null;
	TextView nav_right_item_text=null;
	
	PullToRefreshWebView newdetail_webview=null;
	WebView newdetail_webview_2=null;
	ProgressBar newdetail_pb=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsdetail);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
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
				
				if(CommonUtils.isAppInstalled(NewsDetailActivity.this, "com.tencent.mobileqq")) {
					imageArrayList.add(R.drawable.qq_logo);
					strArrayList.add("QQ����");
					imageArrayList.add(R.drawable.qzone_logo);
					strArrayList.add("QQ�ռ�");
				}
				if(CommonUtils.isAppInstalled(NewsDetailActivity.this, "com.tencent.mm")) {
					imageArrayList.add(R.drawable.weixin_logo);
					strArrayList.add("΢�ź���");
					imageArrayList.add(R.drawable.weixinpy_logo);
					strArrayList.add("΢������Ȧ");
				}
				if(CommonUtils.isAppInstalled(NewsDetailActivity.this, "com.sina.weibo")) {
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
				CommonUtils.showCustomAlertDialog(NewsDetailActivity.this, strArray, imageArray, new OnDialogItemClickListener() {

					@Override
					public void click(int pos) {
						// TODO Auto-generated method stub
						if(strArray[pos].equals("QQ����")) {
							CommonUtils.shareQQ(NewsDetailActivity.this, getIntent().getExtras().getString("desc"), "http://morningtel.qiniudn.com/ic_launcher.png", getIntent().getExtras().getString("name"), getIntent().getExtras().getString("url"));
						}
						else if(strArray[pos].equals("QQ�ռ�")) {
							CommonUtils.shareQQKJ(NewsDetailActivity.this, getIntent().getExtras().getString("desc"), "http://morningtel.qiniudn.com/ic_launcher.png", getIntent().getExtras().getString("name"), getIntent().getExtras().getString("url"));
						}
						else if(strArray[pos].equals("΢�ź���")) {
							CommonUtils.shareWeixin(NewsDetailActivity.this, getIntent().getExtras().getString("desc"), getIntent().getExtras().getString("name"), "", getIntent().getExtras().getString("url"));
						}
						else if(strArray[pos].equals("΢������Ȧ")) {
							CommonUtils.shareWeixinPy(NewsDetailActivity.this, getIntent().getExtras().getString("desc"), getIntent().getExtras().getString("name"), "", getIntent().getExtras().getString("url"));
						}
						else if(strArray[pos].equals("����΢��")) {
							CommonUtils.shareWeibo(NewsDetailActivity.this, getIntent().getExtras().getString("desc"), "http://morningtel.qiniudn.com/ic_launcher.png", getIntent().getExtras().getString("name"), getIntent().getExtras().getString("url"));
						}
					}});
			}
		});
		
		newdetail_pb=(ProgressBar) findViewById(R.id.newdetail_pb);
		newdetail_pb.setMax(100);
		newdetail_webview=(PullToRefreshWebView) findViewById(R.id.newdetail_webview);
		newdetail_webview.setProgressBar(newdetail_pb);
		newdetail_webview_2=newdetail_webview.getRefreshableView();
		newdetail_webview_2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		newdetail_webview_2.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		WebSettings settings=newdetail_webview_2.getSettings();
		settings.setJavaScriptEnabled(true);
		newdetail_webview_2.addJavascriptInterface(this, "JumpActivity");
		newdetail_webview_2.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				//newdetail_webview_2.loadUrl("file:///android_asset/web/index.html");
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				newdetail_pb.setVisibility(View.VISIBLE);
				newdetail_webview.setStart();
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				newdetail_webview.setEnd();
			}
			
			@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
				return true;
            }
		});
		settings.setBuiltInZoomControls(false);
		newdetail_webview_2.loadUrl(getIntent().getExtras().getString("url"));
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
