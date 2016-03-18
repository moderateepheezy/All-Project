package com.renyu.alumni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.model.JsonParse;
import com.renyu.alumni.myview.MyLoadingDialog;
import com.renyu.alumni.security.Security;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChoiceSchoolInfoActivity extends SwipeBackActivity {
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	ListView choiceschoolinfo_list=null;
	ArrayAdapter<String> adapter=null;
	ArrayList<String> strs=null;
	HashMap<String, String> collegeMap=null;
	
	MyLoadingDialog dialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choiceschoolinfo);
		
		strs=new ArrayList<String>();
		if(getIntent().getExtras().getString("type").equals("year")) {
			//1960-2014
			for(int i=1960;i<2015;i++) {
				strs.add(""+i);
			}
		}
		else if(getIntent().getExtras().getString("type").equals("grade")) {
			loadCollegeInfo(getIntent().getExtras().getString("year"));
		}
		else if(getIntent().getExtras().getString("type").equals("edu")) {
			strs.add("ר��");
			strs.add("����");
			strs.add("˶ʿ");
			strs.add("��ʿ");
		}
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		if(getIntent().getExtras().getString("type").equals("year")) {
			nav_title.setText("ѡ��ѧ��");
		}
		else if(getIntent().getExtras().getString("type").equals("grade")) {
			nav_title.setText("ѡ��ѧԺ");
		}
		else if(getIntent().getExtras().getString("type").equals("edu")) {
			nav_title.setText("ѡ��ѧλ");
		}
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		choiceschoolinfo_list=(ListView) findViewById(R.id.choiceschoolinfo_list);
		adapter=new ArrayAdapter<String>(ChoiceSchoolInfoActivity.this, R.layout.adapter_areachoice, strs);
		choiceschoolinfo_list.setAdapter(adapter);
		choiceschoolinfo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=getIntent();
				Bundle bundle=new Bundle();
				if(getIntent().getExtras().getString("type").equals("year")) {
					bundle.putString("year", strs.get(position));
				}
				else if(getIntent().getExtras().getString("type").equals("grade")) {
					bundle.putString("college_id", collegeMap.get(strs.get(position)));
					bundle.putString("college_name", strs.get(position));
				}
				else if(getIntent().getExtras().getString("type").equals("edu")) {
					bundle.putString("edu", strs.get(position));
				}
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}
	
	private void loadCollegeInfo(String year) {
		dialog=CommonUtils.showCustomAlertProgressDialog(ChoiceSchoolInfoActivity.this, "���ڲ�ѯ");
		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		Security se=new Security();
		String serToken=se.getToken(""+(ParamsManager.extratime+System.currentTimeMillis()), "college", "null", ChoiceSchoolInfoActivity.this);
		Header[] headers={new BasicHeader("Validation", serToken)};
		client.get(ChoiceSchoolInfoActivity.this, ParamsManager.HttpUrl+"StudentsContacts/contactsapi/user/college/"+year, headers, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				collegeMap=JsonParse.getCollegeInfoByYear(new String(arg2));
				Iterator<Entry<String, String>> it=collegeMap.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, String> entry=it.next();
					strs.add(entry.getKey());
				}
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				if(arg0==401) {
					JsonParse.showMessage(ChoiceSchoolInfoActivity.this, new String(arg2));
				}
				else {
					CommonUtils.showCustomToast(ChoiceSchoolInfoActivity.this, getResources().getString(R.string.network_error), false);
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

}
