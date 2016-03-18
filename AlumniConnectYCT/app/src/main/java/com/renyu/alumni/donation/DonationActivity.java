package com.renyu.alumni.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.alumni.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DonationActivity extends SwipeBackActivity {

	@InjectView(R.id.nav_title) TextView nav_title;
	@InjectView(R.id.nav_left_item) ImageView nav_left_item;
	@InjectView(R.id.donation_tip_start) TextView donation_tip_start;

	SwipeBackLayout mSwipeBackLayout=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation_tip);
		ButterKnife.inject(this);
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		nav_title.setText("У�Ѿ��");
		nav_left_item.setVisibility(View.VISIBLE);
	}
	
	@OnClick(R.id.nav_left_item)
	public void nav_left_item_click(View view) {
		finish();
	}
	
	@OnClick(R.id.donation_tip_start)
	public void donation_tip_start_click(View view) {
		Intent intent=new Intent(DonationActivity.this, DonationUserInfoActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		finish();
	}
}
