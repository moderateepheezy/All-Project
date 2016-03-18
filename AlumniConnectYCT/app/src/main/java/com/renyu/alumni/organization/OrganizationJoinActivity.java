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

public class OrganizationJoinActivity extends SwipeBackActivity {

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
		nav_title.setText("�������");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		reportmore_edit_title=(TextView) findViewById(R.id.reportmore_edit_title);
		reportmore_edit_title.setText("��������֤��Ϣ");
		reportmore_edit=(EditText) findViewById(R.id.reportmore_edit);
		reportmore_edit.setHint("������100������");
		reportmore_commit=(TextView) findViewById(R.id.reportmore_commit);
		reportmore_commit.setText("����");
		reportmore_commit.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(reportmore_edit.getText().toString().equals("")) {
					CommonUtils.showCustomToast(OrganizationJoinActivity.this, "������100��������֤��Ϣ", false);
					return;
				}
				Intent intent=null;
				Bundle bundle=new Bundle();
				if(getIntent().getExtras().getBoolean("refreshCurrent")) {
					intent=new Intent(OrganizationJoinActivity.this, OrganizationDetailActivity.class);
					bundle.putString("type", getIntent().getExtras().getString("type"));
				}
				else {
					if(getIntent().getExtras().getString("type").equals("ClassInfoModel")) {
						intent=new Intent(OrganizationJoinActivity.this, SearchResultActivity.class);
					}
					else if(getIntent().getExtras().getString("type").equals("AluassociationinfoModel")) {
						intent=new Intent(OrganizationJoinActivity.this, SearchAlumniResultActivity.class);
					}
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				bundle.putInt("id", getIntent().getExtras().getInt("id"));
				bundle.putString("apply_validation", reportmore_edit.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}}); 
	}
}
