package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CreateClassRejectMoreActivity extends SwipeBackActivity {

	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	
	TextView reportmore_edit_title=null;
	EditText reportmore_edit=null;
	TextView reportmore_commit=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reportmore);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("审核拒绝");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		reportmore_edit_title=(TextView) findViewById(R.id.reportmore_edit_title);
		reportmore_edit_title.setText("拒绝原因");
		reportmore_edit=(EditText) findViewById(R.id.reportmore_edit);
		reportmore_edit.setHint("请输入拒绝班级审批的原因");
		reportmore_commit=(TextView) findViewById(R.id.reportmore_commit);
		reportmore_commit.setText("完成");
		reportmore_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reportmore_edit.getText().toString().equals("")) {
					CommonUtils.showCustomToast(CreateClassRejectMoreActivity.this, "请输入拒绝班级审批的原因", false);
					return;
				}
				Intent intent=new Intent(CreateClassRejectMoreActivity.this, ReviewClassActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("refuse_types", getIntent().getExtras().getStringArrayList("refuse_types"));
				bundle.putString("refusereason", reportmore_edit.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}}); 
	}
}
