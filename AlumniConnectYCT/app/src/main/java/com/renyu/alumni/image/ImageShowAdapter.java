package com.renyu.alumni.image;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ImageShowAdapter extends FragmentPagerAdapter {
	
	ArrayList<String> imageUrls=null;
	ArrayList<String> contents=null;

	public ImageShowAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	public ImageShowAdapter(FragmentManager fm, ArrayList<String> imageUrls, ArrayList<String> contents) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.imageUrls=imageUrls;
		this.contents=contents;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return ImageShowFragment.newInstance(imageUrls.get(arg0), contents.get(arg0));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

}
