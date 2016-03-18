package com.renyu.alumni.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.renyu.alumni.service.DownloadService;

public class UpdateTipActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent=new Intent(UpdateTipActivity.this, DownloadService.class);
		Bundle bundle=new Bundle();
		bundle.putString("url", getIntent().getExtras().getString("url"));
		intent.putExtras(bundle);
		startService(intent);
		finish();
	}

}
