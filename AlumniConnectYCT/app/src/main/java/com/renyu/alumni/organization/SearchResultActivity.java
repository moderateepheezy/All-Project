package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
import com.renyu.alumni.model.ClassInfoModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchResultActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	PullToRefreshListView searchresult_list=null;
	ListView actualListView=null;
	SearchResultAdapter adapter=null;
	View footerView=null;
	TextView view_searchresult_title=null;
	
	ArrayList<ClassInfoModel> models=null;
	//�ж��Ƿ����ڼ���
	boolean isLoading=false;
	
	String year="";
	String edu="";
	String department="";
	String college_id="";
	String edu_id="";

	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		
		models=new ArrayList<ClassInfoModel>();
		year=getIntent().getExtras().getString("year");
		edu=getIntent().getExtras().getString("edu");
		department=getIntent().getExtras().getString("department");
		college_id=getIntent().getExtras().getString("college_id");
		edu_id=getIntent().getExtras().getString("edu_id");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("���ҽ��");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		searchresult_list=(PullToRefreshListView) findViewById(R.id.searchresult_list);
		searchresult_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// Update the LastUpdatedLabel
				//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				// Do work to refresh the list here.
				if(!isLoading) {
					loadSearchData();
				}
			}
		});
		actualListView=searchresult_list.getRefreshableView();
		adapter=new SearchResultAdapter(SearchResultActivity.this, models, SearchResultActivity.this);
		actualListView.setAdapter(adapter);
		footerView=initFooterView();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshSearchList();
			}
		}, 300);
	}
	
	/**
	 * ����༶����
	 * @param class_id
	 * @param apply_validation
	 */
	public void joinClassMember(int class_id, String apply_validation) {
		dialog=CommonUtils.showCustomAlertProgressDialog(SearchResultActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(SearchResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "joinclass", model.getToken(), SearchResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("class_id", class_id);
			obj.put("apply_validation", apply_validation);
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(SearchResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/joinclass", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(SearchResultActivity.this, new String(responseBody));
					//���շ���У����֯ҳ��
					Intent intent=new Intent(SearchResultActivity.this, SearchClassActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					if(statusCode==401) {
						JsonParse.showMessage(SearchResultActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(SearchResultActivity.this, getResources().getString(R.string.network_error), false);
					}
				}
			
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					dialog.dismiss();
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
	
	public void refreshSearchList() {
		searchresult_list.setRefreshing(true);
	}
	
	private void loadSearchData() {
		actualListView.removeFooterView(footerView);
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("year", year);
		params.add("class_degree", edu_id);
		params.add("college_id", college_id);
		final UserModel model=DB.getInstance(SearchResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "searchclass", model.getToken(), SearchResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(SearchResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/searchclass", headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				models.clear();
				models.addAll(JsonParse.getSearchClassInfoModel(new String(responseBody)));
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(SearchResultActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(SearchResultActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				if(models.size()==0) {
					view_searchresult_title.setText(year+"�� "+edu+"\n"+department+"\n\nû�в�ѯ���κΰ༶��������");
				}
				else {
					view_searchresult_title.setText("���û�����İ༶�������ԣ�");
				}
				actualListView.addFooterView(footerView);
				searchresult_list.onRefreshComplete();
				isLoading=false;
			}
		});
	}
	
	private View initFooterView() {
		View view=LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.view_searchresult_footer, null);
		TextView view_searchresult_refind=(TextView) view.findViewById(R.id.view_searchresult_refind);
		view_searchresult_refind.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		TextView view_searchresult_create=(TextView) view.findViewById(R.id.view_searchresult_create);
		view_searchresult_create.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchResultActivity.this, CreateClassActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("year", year);
				bundle.putString("edu", edu);
				bundle.putString("department", department);
				bundle.putString("college_id", college_id);
				bundle.putString("edu_id", edu_id);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		view_searchresult_title=(TextView) view.findViewById(R.id.view_searchresult_title);
		return view;
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
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		joinClassMember(intent.getExtras().getInt("id"), intent.getExtras().getString("apply_validation"));		
	}
}
