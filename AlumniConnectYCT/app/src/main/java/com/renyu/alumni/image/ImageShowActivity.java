package com.renyu.alumni.image;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.baidu.mobstat.StatService;
import com.renyu.alumni.R;

import java.util.ArrayList;

public class ImageShowActivity extends FragmentActivity {
	
	PhotoViewPager imageshow_viewpager=null;
	
	//ͼƬ�ϼ�
	ArrayList<String> imageUrls=null;
	ArrayList<String> contents=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imageshow);
		
		init();
	}
	
	private void init() {
		imageshow_viewpager=(PhotoViewPager) findViewById(R.id.imageshow_viewpager);
		imageUrls=getIntent().getExtras().getStringArrayList("imageUrl");
		contents=getIntent().getExtras().getStringArrayList("content");
		imageshow_viewpager.setAdapter(new ImageShowAdapter(getSupportFragmentManager(), imageUrls, contents));
		imageshow_viewpager.setCurrentItem(getIntent().getExtras().getInt("currentPage"));
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
