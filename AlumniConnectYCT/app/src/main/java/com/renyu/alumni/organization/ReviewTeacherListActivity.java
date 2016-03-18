package com.renyu.alumni.organization;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.common.encrypt.AuthorizationConfig;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.model.TeacherinfosModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ReviewTeacherListActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	ListView reviewteacher_list=null;
	
	List<HashMap<String, Object>> lists=null;
	SimpleAdapter adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewteacherlist);
		
		lists=new ArrayList<HashMap<String, Object>>();
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText(getIntent().getExtras().getString("title"));
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		reviewteacher_list=(ListView) findViewById(R.id.reviewteacher_list);
		
		getAllTeacher();
	}
	
	private void getAllTeacher() {
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		UserModel model=DB.getInstance(ReviewTeacherListActivity.this).getUserModel();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "teacherlist", model.getToken(), ReviewTeacherListActivity.this);
		Header[] headers={new BasicHeader("Authorization", AuthorizationConfig.TOKENPRE+model.getToken()), new BasicHeader("Validation", serToken)};
		client.get(ReviewTeacherListActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/aluassociation/teacherlist/"+getIntent().getExtras().getInt("year"), headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				// TODO Auto-generated method stub
				System.out.println(new String(responseBody));
				ArrayList<TeacherinfosModel> tempModel=JsonParse.getTeacherModels(new String(responseBody));
				for(int i=0;i<tempModel.size();i++) {
					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put("college_name", tempModel.get(i).getCollege_name());
					map.put("teacher", tempModel.get(i).getTeacher());
					map.put("degree_name", tempModel.get(i).getDegree_name());
					lists.add(map);
				}
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(statusCode==401) {
					JsonParse.showMessage(ReviewTeacherListActivity.this, new String(responseBody));
				}
				else {
					CommonUtils.showCustomToast(ReviewTeacherListActivity.this, getResources().getString(R.string.network_error), false);
				}
			}
		});
		adapter=new SimpleAdapter(ReviewTeacherListActivity.this, lists, R.layout.adapter_reviewteacherlist, new String[]{"college_name", "teacher", "degree_name"}, new int[]{R.id.college_name, R.id.teacher, R.id.degree_name});
		reviewteacher_list.setAdapter(adapter);
	}

}
