package com.renyu.alumni.ucenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.UserModel;

import java.util.ArrayList;

public class MyBussinessCardAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<UserModel> models=null;
	
	BitmapUtils bitmapUtils=null;
	BitmapDisplayConfig config=null;
	
	public MyBussinessCardAdapter(Context context, ArrayList<UserModel> models) {
		this.context=context;
		this.models=models;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_organization_class));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_organization_class));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return models.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ActivityMembersHolder holder=null;
		if(convertView==null) {
			holder=new ActivityMembersHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_organization_detail, null);
			holder.adapter_organization_detail_image=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_image);
			holder.adapter_organization_detail_isadmin=(TextView) convertView.findViewById(R.id.adapter_organization_detail_isadmin);
			holder.adapter_organization_detail_layout=(LinearLayout) convertView.findViewById(R.id.adapter_organization_detail_layout);
			holder.adapter_organization_detail_classinfo=(TextView) convertView.findViewById(R.id.adapter_organization_detail_classinfo);
			holder.adapter_organization_detail_industry=(TextView) convertView.findViewById(R.id.adapter_organization_detail_industry);
			holder.adapter_organization_detail_name=(TextView) convertView.findViewById(R.id.adapter_organization_detail_name);
			holder.adapter_organization_detail_image_verify=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_image_verify);
			convertView.setTag(holder);
		}
		else {
			holder=(ActivityMembersHolder) convertView.getTag();
		}
		holder.adapter_organization_detail_isadmin.setVisibility(View.GONE);
		holder.adapter_organization_detail_layout.setVisibility(View.GONE);
		holder.adapter_organization_detail_name.setText(models.get(position).getUser_name());
		bitmapUtils.display(holder.adapter_organization_detail_image, ParamsManager.bucketName+models.get(position).getAvatar_large(), config);
		if(models.get(position).getIs_authentication()==1) {
			holder.adapter_organization_detail_image_verify.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_image_verify.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_image_verify.setImageResource(R.drawable.ic_user_verification_no);
		}
		if(models.get(position).getYear()==0&&models.get(position).getClass_name().equals("")) {
			holder.adapter_organization_detail_classinfo.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_classinfo.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_classinfo.setText(models.get(position).getYear()+"��"+models.get(position).getClass_name());
		}
		String city="";
		if(models.get(position).getCity_secret()==1) {
			city=models.get(position).getCity_name();
		}
		String industry="";
		if(models.get(position).getIndustry_secret()==1) {
			industry=models.get(position).getIndustry();
		}
		String companyname="";
		if(models.get(position).getCompany_secret()==1) {
			companyname=models.get(position).getCompanyname();
		}
		if(city.equals("")&&industry.equals("")&&companyname.equals("")) {
			holder.adapter_organization_detail_industry.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_industry.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_industry.setText((city+" "+industry+" "+companyname).trim());
		}
		return convertView;
	}

}

class ActivityMembersHolder {
	ImageView adapter_organization_detail_image=null;
	ImageView adapter_organization_detail_image_verify=null;
	TextView adapter_organization_detail_name=null;
	TextView adapter_organization_detail_isadmin=null;
	LinearLayout adapter_organization_detail_layout=null;
	TextView adapter_organization_detail_classinfo=null;
	TextView adapter_organization_detail_industry=null;
}
