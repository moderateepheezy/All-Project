package com.renyu.alumni.main2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.donation.DonationActivity;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.login.LoginActivity;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.myview.CircleImageView;
import com.renyu.alumni.organization.OrganizationActivity;
import com.renyu.alumni.ucenter.MyBussinessCardActivity;
import com.renyu.alumni.ucenter.SettingActivity;
import com.renyu.alumni.ucenter.UserInfoActivity;

public class MenuFragment extends Fragment {
	
	View view=null;
	
	LinearLayout menu_people_layout=null;
	CircleImageView menu_people_image=null;
	TextView menu_people_text=null;
	TextView menu_myclass=null;
	TextView menu_myalumni=null;
	TextView menu_mybussiness=null;
	TextView menu_mysetting=null;
	TextView menu_myinfo=null;
	TextView menu_donation=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(getActivity().getResources().getDrawable(R.drawable.ic_main_people));
		config.setLoadingDrawable(getActivity().getResources().getDrawable(R.drawable.ic_main_people));	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			view=inflater.inflate(R.layout.fragment_main2_menu, container, false);
			menu_people_layout=(LinearLayout) view.findViewById(R.id.menu_people_layout);
			menu_people_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UserModel model=DB.getInstance(getActivity()).getUserModel();
					if(model==null) {
						Intent intent=new Intent(getActivity(), LoginActivity.class);
						startActivity(intent);
						return;
					}
					//部分手机有Bug
					ParamsManager.isOpenCount=0;
					Intent intent=new Intent(getActivity(), UserInfoActivity.class);
					startActivity(intent);
				}});
			menu_people_image=(CircleImageView) view.findViewById(R.id.menu_people_image);
			menu_people_text=(TextView) view.findViewById(R.id.menu_people_text);	
			menu_myinfo=(TextView) view.findViewById(R.id.menu_myinfo);
			menu_myinfo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UserModel model=DB.getInstance(getActivity()).getUserModel();
					if(model==null) {
						Intent intent=new Intent(getActivity(), LoginActivity.class);
						startActivity(intent);
						return;
					}
					//部分手机有Bug
					ParamsManager.isOpenCount=0;
					Intent intent=new Intent(getActivity(), UserInfoActivity.class);
					startActivity(intent);
				}
			});
			menu_myclass=(TextView) view.findViewById(R.id.menu_myclass);
			menu_myclass.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), OrganizationActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("queryType", "myclass");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			menu_myalumni=(TextView) view.findViewById(R.id.menu_myalumni);
			menu_myalumni.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), OrganizationActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("queryType", "myalumni");
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			menu_mybussiness=(TextView) view.findViewById(R.id.menu_mybussiness);
			menu_mybussiness.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), MyBussinessCardActivity.class);
					startActivity(intent);
				}
			});
			menu_mysetting=(TextView) view.findViewById(R.id.menu_mysetting);
			menu_mysetting.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), SettingActivity.class);
					startActivity(intent);
				}
			});
			menu_donation=(TextView) view.findViewById(R.id.menu_donation);
			menu_donation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(getActivity(), DonationActivity.class);
					startActivity(intent);
				}
			});
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		return view;
	}
	
	public void refreshLoginInfo() {
		UserModel model=DB.getInstance(getActivity()).getUserModel();
		if(model!=null&&menu_people_text!=null&&menu_people_image!=null) {
			menu_people_text.setText(model.getUser_name());
			bitmapUtils.display(menu_people_image, ParamsManager.bucketName+model.getAvatar_large(), config);
		}
		else if(model==null&&menu_people_text!=null&&menu_people_image!=null) {
			menu_people_text.setText("登陆");
			menu_people_image.setImageResource(R.drawable.ic_main_people);
		}
	}

}
