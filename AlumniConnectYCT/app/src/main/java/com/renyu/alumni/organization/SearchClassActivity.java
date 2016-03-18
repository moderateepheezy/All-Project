package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.login.ChoiceSchoolInfoActivity;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchClassActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	TextView search_class_year=null;
	TextView search_class_edu=null;
	TextView search_class_department=null;
	TextView search_class_commit=null;
	
	//ѧԺid
	String college_id="";
	//ѧλid
	String edu_id="1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_class);
		
		init();
	}
	
	private void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("���Ұ༶");
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
		
		search_class_year=(TextView) findViewById(R.id.search_class_year);
		search_class_year.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchClassActivity.this, ChoiceSchoolInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("type", "year");
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.ORGANIZATION_YEAR);
			}});
		search_class_edu=(TextView) findViewById(R.id.search_class_edu);
		search_class_edu.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchClassActivity.this, ChoiceSchoolInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("type", "edu");
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.ORGANIZATION_EDU);
			}});
		search_class_department=(TextView) findViewById(R.id.search_class_department);
		search_class_department.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(search_class_year.getText().toString().equals("")) {
					return;
				}
				Intent intent=new Intent(SearchClassActivity.this, ChoiceSchoolInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("type", "grade");
				bundle.putString("year", search_class_year.getText().toString());
				intent.putExtras(bundle);
				startActivityForResult(intent, ParamsManager.ORGANIZATION_GRADE);
			}});
		search_class_commit=(TextView) findViewById(R.id.search_class_commit);
		search_class_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(search_class_year.getText().toString().equals("")||
						search_class_edu.getText().toString().equals("")||
						search_class_department.getText().toString().equals("")) {
					CommonUtils.showCustomToast(SearchClassActivity.this, "������Ϣ����Ϊ��", false);
					return;
				}
				Intent intent=new Intent(SearchClassActivity.this, SearchResultActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("year", search_class_year.getText().toString());
				bundle.putString("edu", search_class_edu.getText().toString());
				bundle.putString("department", search_class_department.getText().toString());
				bundle.putString("college_id", college_id);
				bundle.putString("edu_id", edu_id);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) {
			switch(requestCode) {
			case ParamsManager.ORGANIZATION_YEAR:
				search_class_year.setText(data.getExtras().getString("year"));
				break;
			case ParamsManager.ORGANIZATION_EDU:
				search_class_edu.setText(data.getExtras().getString("edu"));
				if(data.getExtras().getString("edu").equals("����")) {
					edu_id="1";
				}
				else if(data.getExtras().getString("edu").equals("˶ʿ")) {
					edu_id="2";
				}
				else if(data.getExtras().getString("edu").equals("��ʿ")) {
					edu_id="3";
				}
				else if(data.getExtras().getString("edu").equals("ר��")) {
					edu_id="4";
				}
				break;
			case ParamsManager.ORGANIZATION_GRADE:
				search_class_department.setText(data.getExtras().getString("college_name"));
				college_id=data.getExtras().getString("college_id");
				break;
			}
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setResult(RESULT_OK, intent);
		finish();
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
