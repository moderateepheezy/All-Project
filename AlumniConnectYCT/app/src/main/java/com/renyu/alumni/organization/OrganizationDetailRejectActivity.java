package com.renyu.alumni.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrganizationDetailRejectActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	TextView nav_title=null;
	ImageView nav_left_item=null;
	TextView nav_right_item_text=null;
	
	CheckBox organization_detail_reject_image=null;
	RelativeLayout organization_detail_reject_other=null;
	RelativeLayout organization_detail_reject_layout=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organizationdetail_reject);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("��˾ܾ�");
		nav_left_item=(ImageView) findViewById(R.id.nav_left_item);
		nav_left_item.setVisibility(View.VISIBLE);
		nav_left_item.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_right_item_text=(TextView) findViewById(R.id.nav_right_item_text);
		nav_right_item_text.setText("���");
		nav_right_item_text.setVisibility(View.VISIBLE);
		nav_right_item_text.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(organization_detail_reject_image.isChecked()) {
					Intent intent=new Intent(OrganizationDetailRejectActivity.this, OrganizationDetailApplyActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle bundle=new Bundle();
					bundle.putBoolean("checked", true);
					bundle.putString("refusereason", "");
					intent.putExtras(bundle);
					startActivity(intent);
				}
				else {
					CommonUtils.showCustomToast(OrganizationDetailRejectActivity.this, "������ѡ��һ��ܾ�ԭ��", false);
				}
			}});
		
		organization_detail_reject_image=(CheckBox) findViewById(R.id.organization_detail_reject_image);
		organization_detail_reject_other=(RelativeLayout) findViewById(R.id.organization_detail_reject_other);
		organization_detail_reject_other.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(OrganizationDetailRejectActivity.this, OrganizationDetailRejectMoreActivity.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("checked", organization_detail_reject_image.isChecked());
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		organization_detail_reject_layout=(RelativeLayout) findViewById(R.id.organization_detail_reject_layout);
		organization_detail_reject_layout.setOnClickListener(new RelativeLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(organization_detail_reject_image.isChecked()) {
					organization_detail_reject_image.setChecked(false);
				}
				else {
					organization_detail_reject_image.setChecked(true);
				}
			}});
	}
}
