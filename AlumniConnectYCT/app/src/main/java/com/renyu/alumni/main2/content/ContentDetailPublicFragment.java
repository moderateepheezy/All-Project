package com.renyu.alumni.main2.content;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.main2.MainActivity2;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;

import java.util.ArrayList;

public class ContentDetailPublicFragment extends Fragment {
	
	View view=null;
	
	CircleImageView open_menu=null;
	ViewPager contentdetail_viewpager=null;
	ContentPagerAdapter adapter=null;
	TextView contentdetail_index_1=null;
	TextView contentdetail_index_2=null;
	TextView contentdetail_index_3=null;
	
	ArrayList<Fragment> mFragments;
	
	BitmapUtils bitmapUtils=null;
	BitmapDisplayConfig config=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getActivity().getResources().getDrawable(R.drawable.ic_people_open));
		config.setLoadingDrawable(getActivity().getResources().getDrawable(R.drawable.ic_people_open));	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			mFragments=new ArrayList<Fragment>();
			view=inflater.inflate(R.layout.fragment_contentdetail, container, false);
			contentdetail_viewpager=(ViewPager) view.findViewById(R.id.contentdetail_viewpager);
			contentdetail_viewpager.setOffscreenPageLimit(3);
			adapter=new ContentPagerAdapter(getChildFragmentManager());
			contentdetail_viewpager.setAdapter(adapter);
			contentdetail_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
    			@Override
    			public void onPageScrollStateChanged(int arg0) { }

    			@Override
    			public void onPageScrolled(int arg0, float arg1, int arg2) { }

    			@Override
    			public void onPageSelected(int position) {
    				switch (position) {
    				case 0:
    					((SlidingFragmentActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    					break;
    				default:
    					((SlidingFragmentActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    					break;
    				}
    				changePageIndicator(position);
    			}

    		});
    		
			((MainActivity2) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    		
	    	open_menu=(CircleImageView) view.findViewById(R.id.contentdetail_open_menu);
	    	open_menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity2) getActivity()).showMenu();
				}});
	    	
	    	addFragment(new int[] {1, 2, 3}, savedInstanceState);
	    	contentdetail_index_1=(TextView) view.findViewById(R.id.contentdetail_index_1);
			contentdetail_index_1.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					contentdetail_viewpager.setCurrentItem(0);
					changePageIndicator(0);
				}});
			contentdetail_index_2=(TextView) view.findViewById(R.id.contentdetail_index_2);
			contentdetail_index_2.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					contentdetail_viewpager.setCurrentItem(1);
					changePageIndicator(1);
				}});
			contentdetail_index_3=(TextView) view.findViewById(R.id.contentdetail_index_3);
			contentdetail_index_3.setOnClickListener(new TextView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					contentdetail_viewpager.setCurrentItem(2);
					changePageIndicator(2);
				}});
	    	changePageIndicator(0);
		}
		else {
    		if(contentdetail_viewpager!=null) {
    			switch(contentdetail_viewpager.getCurrentItem()) {
	    			case 0:
						((MainActivity2) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						break;
					default:
						((MainActivity2) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
						break;
    			}
    		}
    	}
		
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UserModel model=DB.getInstance(getActivity()).getUserModel();
		if(model!=null) {
			bitmapUtils.display(open_menu, ParamsManager.bucketName+model.getAvatar_large(), config);
		}
		else if(model==null) {
			open_menu.setImageResource(R.drawable.ic_people_open);
		}
	}
	
	private void changePageIndicator(int position) {
		contentdetail_index_3.setTextColor(Color.WHITE);
		contentdetail_index_3.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
		contentdetail_index_2.setTextColor(Color.WHITE);
		contentdetail_index_2.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
		contentdetail_index_1.setTextColor(Color.WHITE);
		contentdetail_index_1.setBackgroundColor(getResources().getColor(R.color.tab_text_press));
		if(position==0) {
			contentdetail_index_1.setTextColor(getResources().getColor(R.color.tab_text_press));
			contentdetail_index_1.setBackgroundColor(Color.WHITE);
		}
		else if(position==1) {
			contentdetail_index_2.setTextColor(getResources().getColor(R.color.tab_text_press));
			contentdetail_index_2.setBackgroundColor(Color.WHITE);
		}
		else if(position==2) {
			contentdetail_index_3.setTextColor(getResources().getColor(R.color.tab_text_press));
			contentdetail_index_3.setBackgroundColor(Color.WHITE);
		}
	}
	
	public void addFragment(int[] ids, Bundle savedInstanceState) {
		if(mFragments!=null&&mFragments.size()>0){
			FragmentTransaction ft = getChildFragmentManager().beginTransaction();
			for(Fragment f: mFragments){
				ft.remove(f);
			}
			ft.commitAllowingStateLoss();
			ft=null;
			getChildFragmentManager().executePendingTransactions();
		}
		mFragments.clear();
		for(int i=0;i<ids.length;i++) {
			DetailChildFragment f=null;
			if(savedInstanceState!=null) {
				f=(DetailChildFragment) getChildFragmentManager().findFragmentByTag("tab"+i);
			}
			if(f==null) {
				f=DetailChildFragment.newInstance(""+ids[i]);
			}
			mFragments.add(f);
		}
		adapter.notifyDataSetChanged();
		contentdetail_viewpager.setCurrentItem(0);
	}

	public class ContentPagerAdapter extends FragmentPagerAdapter {
    	FragmentManager fm=null;
		
		public ContentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.fm=fm;
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(mFragments==null) {
			return;
		}
		for(int i=0;i<mFragments.size();i++) {
			if(mFragments.get(i).isAdded()) {
				getChildFragmentManager().putFragment(outState, "tab"+i, mFragments.get(i));
			}
		}
	}
}
