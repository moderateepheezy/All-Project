package com.renyu.alumni.organization;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.renyu.alumni.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class OrganizationActivity extends SwipeBackActivity {
	
	SwipeBackLayout mSwipeBackLayout=null;
	
	FrameLayout organization_layout=null;

	//��ѯ����
	String queryType="all";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization);
		
		queryType=getIntent().getExtras().getString("queryType");
		
		init();
	}
	
	private void init() {
		mSwipeBackLayout=getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		organization_layout=(FrameLayout) findViewById(R.id.organization_layout);
		OrganizationFragment fragment=OrganizationFragment.getInstance(queryType);
		FragmentManager manager=getSupportFragmentManager();
		FragmentTransaction tran=manager.beginTransaction();
		tran.replace(R.id.organization_layout, fragment);
		tran.commit();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
