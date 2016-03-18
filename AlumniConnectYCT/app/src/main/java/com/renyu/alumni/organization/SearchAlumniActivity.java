package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchAlumniActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	LinearLayout search_alumni_local=null;
	LinearLayout search_alumni_school=null;
	LinearLayout search_alumni_work=null;
	LinearLayout search_alumni_favorite=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_alumni);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("����У�ѻ�");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		search_alumni_local=(LinearLayout) findViewById(R.id.search_alumni_local);
		search_alumni_local.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchAlumniActivity.this, SearchAlumniResultActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("type_id", 1);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		search_alumni_school=(LinearLayout) findViewById(R.id.search_alumni_school);
		search_alumni_school.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchAlumniActivity.this, SearchAlumniResultActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("type_id", 2);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		search_alumni_work=(LinearLayout) findViewById(R.id.search_alumni_work);
		search_alumni_work.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchAlumniActivity.this, SearchAlumniResultActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("type_id", 3);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		search_alumni_favorite=(LinearLayout) findViewById(R.id.search_alumni_favorite);
		search_alumni_favorite.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SearchAlumniActivity.this, SearchAlumniResultActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("type_id", 4);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
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
