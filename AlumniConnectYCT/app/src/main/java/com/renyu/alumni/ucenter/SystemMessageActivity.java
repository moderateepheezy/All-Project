package com.renyu.alumni.ucenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SystemMessageActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView news_nolist=null;
	PullToRefreshListView news_list=null;
	ListView actralListView=null;
	SystemMessageAdapter adapter=null;
	ArrayList<String> strs=null;
	
	boolean isLoading=false;
	//�Ƿ��ڸ�ҳ����յ��㲥
	boolean isReceiver=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_news);
		
		strs=new ArrayList<String>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("ϵͳ��Ϣ");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isReceiver) {
					Intent intent=getIntent();
					setResult(RESULT_OK, intent);
				}
				finish();
			}});
		
		news_nolist=(TextView) findViewById(R.id.news_nolist);
		news_nolist.setText("�����������ϵͳ��Ϣ");
		news_list=(PullToRefreshListView) findViewById(R.id.news_list);
		news_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				// Do work to refresh the list here.
				if(!isLoading) {
					loadSystemMessage();
				}
			}
		});
		actralListView=news_list.getRefreshableView();
		adapter=new SystemMessageAdapter(SystemMessageActivity.this, strs);
		actralListView.setAdapter(adapter);
		refreshList();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("SYSTEMNOTIFICATION");
		registerReceiver(receiver, filter);
	}
	
	private void loadSystemMessage() {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(SystemMessageActivity.this).getUserModel();
		RequestParams params=new RequestParams();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getsysmessage", model.getToken(), SystemMessageActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(SystemMessageActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/push/getsysmessage", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				strs.clear();
				strs.addAll(JsonParse.getSystemMessageList(new String(responseBody)));
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(SystemMessageActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(SystemMessageActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				news_list.onRefreshComplete();
				isLoading=false;
				if(strs.size()!=0) {
					news_nolist.setVisibility(View.GONE);
					return;
				}
				news_nolist.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void refreshList() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				news_list.setRefreshing(true);
			}
		}, 300);
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
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	};
	
	BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("SYSTEMNOTIFICATION")) {
				isReceiver=true;
				if(isLoading) {
					return;
				}
				refreshList();
			}
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			if(isReceiver) {
				Intent intent=getIntent();
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	};
}
