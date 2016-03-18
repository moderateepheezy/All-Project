package com.renyu.alumni.main2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.main2.content.ContentDetailPublicFragment;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.postsystem.SearchCopActivity;
import com.renyu.alumni.postsystem.SendPostActivity;
import com.renyu.alumni.ucenter.MessageCenterFragment;

import java.util.LinkedList;

public class ContentFragment extends Fragment {
	
	View view=null;
	
	ImageView fragment_main2_content_send=null;
	RelativeLayout fragment_main2_content_sendlayout=null;
	LinearLayout fragment_main2_content_sendpost=null;
	LinearLayout fragment_main2_content_connections=null;
	LinearLayout fragment_main2_content_sendlayout_close=null;
	
	FragmentTabHost fragment_main2_content_tabhost=null;
	LinearLayout fragment_main2_content_public_layout=null;
	ImageView fragment_main2_content_public_image=null;
	TextView fragment_main2_content_public_text=null;
	LinearLayout fragment_main2_content_inner_layout=null;
	ImageView fragment_main2_content_inner_image=null;
	TextView fragment_main2_content_inner_text=null;
	
	private int[] icon_array={R.drawable.ic_launcher, R.drawable.ic_launcher};
	private String[] title_array={"����Ƶ��", "����Ƶ��"};
	
	//����������
	LinkedList<View> nav_layouts=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			nav_layouts=new LinkedList<View>();
			
			view=inflater.inflate(R.layout.fragment_main2_content, container, false);
			
			fragment_main2_content_tabhost=(FragmentTabHost) view.findViewById(R.id.fragment_main2_content_tabhost);
			
			fragment_main2_content_sendlayout_close=(LinearLayout) view.findViewById(R.id.fragment_main2_content_sendlayout_close);
			fragment_main2_content_sendlayout_close.setOnClickListener(new LinearLayout.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeSendLayout();
				}});
			fragment_main2_content_sendlayout=(RelativeLayout) view.findViewById(R.id.fragment_main2_content_sendlayout);
			fragment_main2_content_sendpost=(LinearLayout) view.findViewById(R.id.fragment_main2_content_sendpost);
			fragment_main2_content_sendpost.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeSendLayout();
					UserModel model=DB.getInstance(getActivity()).getUserModel();
					if(model==null) {
						Intent intent=new Intent(getActivity(), LoginActivity.class);
						startActivity(intent);
						return;
					}
					Intent intent=new Intent(getActivity(), SendPostActivity.class);
					startActivity(intent);
				}});
			fragment_main2_content_connections=(LinearLayout) view.findViewById(R.id.fragment_main2_content_connections);
			fragment_main2_content_connections.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeSendLayout();
					UserModel model=DB.getInstance(getActivity()).getUserModel();
					if(model==null) {
						Intent intent=new Intent(getActivity(), LoginActivity.class);
						startActivity(intent);
						return;
					}
					Intent intent=new Intent(getActivity(), SearchCopActivity.class);
					startActivity(intent);
				}});
			fragment_main2_content_send=(ImageView) view.findViewById(R.id.fragment_main2_content_send);
			fragment_main2_content_send.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openSendLayout();
				}});
			setupTabView();
			fragment_main2_content_public_layout=(LinearLayout) view.findViewById(R.id.fragment_main2_content_public_layout);
			fragment_main2_content_public_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					fragment_main2_content_tabhost.setCurrentTab(0);
					fragment_main2_content_public_image.setImageResource(R.drawable.ic_main2_content_public_press);
					fragment_main2_content_public_text.setTextColor(Color.parseColor("#50a3ef"));
					fragment_main2_content_inner_image.setImageResource(R.drawable.ic_main2_content_people_normal);
					fragment_main2_content_inner_text.setTextColor(Color.parseColor("#b2b2b2"));
				}
			});
			fragment_main2_content_inner_layout=(LinearLayout) view.findViewById(R.id.fragment_main2_content_inner_layout);
			fragment_main2_content_inner_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					fragment_main2_content_tabhost.setCurrentTab(1);
					fragment_main2_content_public_image.setImageResource(R.drawable.ic_main2_content_public_normal);
					fragment_main2_content_public_text.setTextColor(Color.parseColor("#b2b2b2"));
					fragment_main2_content_inner_image.setImageResource(R.drawable.ic_main2_content_people_press);
					fragment_main2_content_inner_text.setTextColor(Color.parseColor("#50a3ef"));
				}
			});
			fragment_main2_content_public_image=(ImageView) view.findViewById(R.id.fragment_main2_content_public_image);
			fragment_main2_content_public_text=(TextView) view.findViewById(R.id.fragment_main2_content_public_text);
			fragment_main2_content_inner_image=(ImageView) view.findViewById(R.id.fragment_main2_content_inner_image);
			fragment_main2_content_inner_text=(TextView) view.findViewById(R.id.fragment_main2_content_inner_text);
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		if(savedInstanceState!=null&&savedInstanceState.getString("currentTab")!=null) {
			if(savedInstanceState.getString("currentTab").equals("����Ƶ��")) {
				fragment_main2_content_public_layout.performClick();
			}
			else if(savedInstanceState.getString("currentTab").equals("����Ƶ��")) {
				fragment_main2_content_inner_layout.performClick();
			}
		}
		return view;
	}
	
	private void setupTabView() {
		fragment_main2_content_tabhost.setup(getActivity(), getChildFragmentManager(), R.id.fragment_main2_realtabcontent);
		fragment_main2_content_tabhost.getTabWidget().setDividerDrawable(null);
		for(int i=0;i<icon_array.length;i++) {
			TabSpec spec=fragment_main2_content_tabhost.newTabSpec(title_array[i]).setIndicator(getTabItemView(i)); 
			switch(i) {
			case 0:
				fragment_main2_content_tabhost.addTab(spec, ContentDetailPublicFragment.class, null);
				break;
			case 1:
				fragment_main2_content_tabhost.addTab(spec, MessageCenterFragment.class, null);
				break;
			}
		}
		fragment_main2_content_tabhost.setCurrentTab(0);
	}
	
	private View getTabItemView(int pos) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.view_tab, null);
		ImageView tab_image=(ImageView) view.findViewById(R.id.tab_image);
		TextView tab_title=(TextView) view.findViewById(R.id.tab_title);		
		tab_image.setImageResource(icon_array[pos]);
		tab_title.setText(title_array[pos]);
		nav_layouts.add(view);
		return view;
	}
	
	public boolean isSendLayoutOpen() {
		return fragment_main2_content_sendlayout.getVisibility()==View.VISIBLE;
	}
	
	/**
	 * �رշ��Ͳ˵�
	 */
	public void closeSendLayout() {
		AnimatorSet set=new AnimatorSet();
		set.setInterpolator(new LinearInterpolator());
		set.setDuration(1000);
		set.playTogether(ObjectAnimator.ofFloat(fragment_main2_content_sendlayout, "alpha", 1, 0), ObjectAnimator.ofFloat(fragment_main2_content_sendlayout, "translationY", 0, CommonUtils.getScreenHeight(getActivity())));
		set.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				fragment_main2_content_sendlayout.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		set.start();
	}
	
	/**
	 * �򿪷��Ͳ˵�
	 */
	public void openSendLayout() {
		RelativeLayout.LayoutParams params=(LayoutParams) fragment_main2_content_sendlayout.getLayoutParams();
		params.height=(CommonUtils.getScreenHeight(getActivity())-CommonUtils.getStatusBarHeight(getActivity()));
		fragment_main2_content_sendlayout.setLayoutParams(params);
		AnimatorSet set=new AnimatorSet();
		set.setInterpolator(new LinearInterpolator());
		set.setDuration(1000);
		set.playTogether(ObjectAnimator.ofFloat(fragment_main2_content_sendlayout, "alpha", 0, 1), ObjectAnimator.ofFloat(fragment_main2_content_sendlayout, "translationY", CommonUtils.getScreenHeight(getActivity()), 0));
		set.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				fragment_main2_content_sendlayout.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		set.start();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("currentTab", fragment_main2_content_tabhost.getCurrentTabTag());
	}

}
