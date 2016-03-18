package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.renyu.alumni.model.AluassociationinfoModel;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;
import com.renyu.alumni.ucenter.UserInfoActivity;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchAlumniResultActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	PullToRefreshListView searchresult_list=null;
	ListView actualListView=null;
	SearchAlumniResultAdapter adapter=null;
	ArrayList<AluassociationinfoModel> models=null;

	//�ж��Ƿ����ڼ���
	boolean isLoading=false;

	MyLoadingDialog dialog=null;
	
	//֮ǰ�����������ݶ���
	int beforeJoinId=-1;
	String beforeJoinValidation="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		
		models=new ArrayList<AluassociationinfoModel>();
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		nav_title=(TextView) findViewById(R.id.nav_title);
		if(getIntent().getExtras().getInt("type_id")==1) {
			nav_title.setText("�ط�У����֯");
		}
		else if(getIntent().getExtras().getInt("type_id")==2) {
			nav_title.setText("ԺУУ����֯");
		}
		else if(getIntent().getExtras().getInt("type_id")==3) {
			nav_title.setText("��ҵУ����֯");
		}
		else if(getIntent().getExtras().getInt("type_id")==4) {
			nav_title.setText("��ȤУ����֯");
		}
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
				//�˴���bug�����л�ҳ��ʱ����ɵײ�subview����Сʱ�����ڴ˲���ʾ
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				// Do work to refresh the list here.
				if(!isLoading) {
					loadSearchData();
				}
			}
		});
		actualListView=searchresult_list.getRefreshableView();
		adapter=new SearchAlumniResultAdapter(SearchAlumniResultActivity.this, models, SearchAlumniResultActivity.this);
		actualListView.setAdapter(adapter);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshSearchList();
			}
		}, 300);
	}
	
	public void refreshSearchList() {
		searchresult_list.setRefreshing(true);
	}
	
	private void loadSearchData() {
		isLoading=true;
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(SearchAlumniResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "getinfobytype", model.getToken(), SearchAlumniResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(SearchAlumniResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/getinfobytype/"+getIntent().getExtras().getInt("type_id"), headers, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				models.clear();
				models.addAll(JsonParse.getSearchAluassociationinfoModel(new String(responseBody)));
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(SearchAlumniResultActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(SearchAlumniResultActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				searchresult_list.onRefreshComplete();
				isLoading=false;
			}
		});
	}
	
	/**
	 * ����У�ѻ�����
	 * @param aluassociation_id
	 * @param apply_validation
	 */
	public void joinAlumni(int aluassociation_id, String apply_validation) {
		dialog=CommonUtils.showCustomAlertProgressDialog(SearchAlumniResultActivity.this, "�����ύ");
		AsyncHttpClient client=new AsyncHttpClient();
		UserModel model=DB.getInstance(SearchAlumniResultActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "joinaluassociation", model.getToken(), SearchAlumniResultActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		JSONObject obj=new JSONObject();
		try {
			obj.put("aluassociation_id", aluassociation_id);
			obj.put("apply_validation", apply_validation);
			System.out.println(obj.toString());
			ByteArrayEntity entity=new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
			client.post(SearchAlumniResultActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/joinaluassociation", headers, entity, "application/json;charset=UTF-8", new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					JsonParse.showMessage(SearchAlumniResultActivity.this, new String(responseBody));
					//��������Ϣ����ȫ��ʱ�����û�ȥ����֮�����¼���
					if(JsonParse.getLoginResult(SearchAlumniResultActivity.this, new String(responseBody))==2) {
						//�����ֻ���Bug
						ParamsManager.isOpenCount=0;
						Intent intent=new Intent(SearchAlumniResultActivity.this, UserInfoActivity.class);
						Bundle bundle=new Bundle();
						bundle.putBoolean("isNeedBack", true);
						intent.putExtras(bundle);
						startActivityForResult(intent, ParamsManager.ORGANIZATION_ALUMNIREJOIN);
					}
					else {
						refreshSearchList();
						
						Intent intent=new Intent();
						intent.setAction("refresh");
						sendBroadcast(intent);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
					if(statusCode==401) {
						JsonParse.showMessage(SearchAlumniResultActivity.this, new String(responseBody));
					}
					else {
						CommonUtils.showCustomToast(SearchAlumniResultActivity.this, getResources().getString(R.string.network_error), false);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&ParamsManager.ORGANIZATION_ALUMNIREJOIN==requestCode) {
			joinAlumni(beforeJoinId, beforeJoinValidation);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		joinAlumni(intent.getExtras().getInt("id"), intent.getExtras().getString("apply_validation"));

		beforeJoinId=intent.getExtras().getInt("id");
		beforeJoinValidation=intent.getExtras().getString("apply_validation");
	}
}
