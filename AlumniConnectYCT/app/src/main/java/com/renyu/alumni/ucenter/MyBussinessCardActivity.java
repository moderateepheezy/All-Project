package com.renyu.alumni.ucenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
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
import com.renyu.alumni.organization.BussinessCardActivity;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MyBussinessCardActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	PullToRefreshListView news_list=null;
	ListView actualListView=null;
	MyBussinessCardAdapter adapter=null;
	TextView news_nolist=null;
	
	int page=1;
	boolean isLoading=false;
	
	ArrayList<UserModel> userModels;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_news);
		
		userModels=new ArrayList<UserModel>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�ҵ���Ƭ��");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		news_nolist=(TextView) findViewById(R.id.news_nolist);
		news_nolist.setText("����δ�ղ�У����Ƭ");
		news_list=(PullToRefreshListView) findViewById(R.id.news_list);
		news_list.setMode(Mode.BOTH);
		news_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// Update the LastUpdatedLabel
				//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				// Do work to refresh the list here.
				int scrollValue=news_list.getHeadScroll();
				//����0��ʱ��Ϊ����ˢ�£�С��0��ʱ��Ϊ����ˢ��
				if(scrollValue>0) {
					if(!isLoading) {
						loadData();
					}						
				}
				else {
					if(!isLoading) {
						page=1;
						loadData();
					}
				}
			}
		});
		actualListView=news_list.getRefreshableView();
		adapter=new MyBussinessCardAdapter(MyBussinessCardActivity.this, userModels);
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("type").equals("choice")) {
					Intent intent=getIntent();
					Bundle bundle=new Bundle();
					bundle.putSerializable("choiceModel", userModels.get(position-1));
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
				else {
					Intent intent=new Intent(MyBussinessCardActivity.this, BussinessCardActivity.class);
					Bundle bundle=new Bundle();
					bundle.putInt("user_id", userModels.get(position-1).getUser_id());
					bundle.putBoolean("mask", false);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshData();
			}
		}, 300);
	}
	
	public void refreshData() {
		news_list.setRefreshing(true);
	}
	
	private void loadData() {
		isLoading=true;
		UserModel model=DB.getInstance(MyBussinessCardActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getmycards", model.getToken(), MyBussinessCardActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("page", ""+page);
		params.add("pagesize", "15");
		client.get(MyBussinessCardActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/personalcenter/getmycards", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				if(page==1) {
					userModels.clear();
				}
				if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("type").equals("choice")) {
					UserModel umodel=DB.getInstance(MyBussinessCardActivity.this).getUserModel();
					userModels.add(umodel);
				}
				userModels.addAll(JsonParse.getActivityMember(new String(responseBody)));
				adapter.notifyDataSetChanged();
				page++;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(MyBussinessCardActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(MyBussinessCardActivity.this, getResources().getString(R.string.network_error), false);
				}
				
				if(page==1) {
					userModels.clear();
				}
				if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("type").equals("choice")) {
					UserModel umodel=DB.getInstance(MyBussinessCardActivity.this).getUserModel();
					userModels.add(umodel);
				}
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				news_list.onRefreshComplete();
				isLoading=false;
				if(userModels.size()==0) {
					news_nolist.setVisibility(View.VISIBLE);
				}
				else {
					news_nolist.setVisibility(View.GONE);
				}
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
