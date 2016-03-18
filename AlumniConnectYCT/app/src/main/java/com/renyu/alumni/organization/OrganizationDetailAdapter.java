package com.renyu.alumni.organization;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.renyu.alumni.dao.DB;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.ClassUserModel;
import com.renyu.alumni.model.UserModel;
import com.renyu.alumni.ucenter.MessageActivity;

import java.util.ArrayList;

public class OrganizationDetailAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<ClassUserModel> models;
	int isAdmin=0;
	OrganizationDetailActivity activity;
	String type="";
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	public OrganizationDetailAdapter(Context context, ArrayList<ClassUserModel> models, OrganizationDetailActivity activity, String type) {
		this.context=context;
		this.models=models;
		this.activity=activity;
		this.type=type;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));	
	}
	
	public void setAdmin(int isAdmin) {
		this.isAdmin=isAdmin;
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
		final int position_=position;
		OrganizationDetailHolder holder=null;
		if(convertView==null) {
			holder=new OrganizationDetailHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_organization_detail, null);
			holder.adapter_organization_detail_clicklayout=(LinearLayout) convertView.findViewById(R.id.adapter_organization_detail_clicklayout);
			holder.adapter_organization_detail_name=(TextView) convertView.findViewById(R.id.adapter_organization_detail_name);
			holder.adapter_organization_detail_isadmin=(TextView) convertView.findViewById(R.id.adapter_organization_detail_isadmin);
			holder.adapter_organization_detail_apply_state=(TextView) convertView.findViewById(R.id.adapter_organization_detail_apply_state);
			holder.adapter_organization_detail_control=(LinearLayout) convertView.findViewById(R.id.adapter_organization_detail_control);
			holder.adapter_organization_detail_manager=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_manager);
			holder.adapter_organization_detail_talk=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_talk);
			holder.adapter_organization_detail_image=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_image);
			holder.adapter_organization_detail_image_verify=(ImageView) convertView.findViewById(R.id.adapter_organization_detail_image_verify);
			holder.adapter_organization_detail_classinfo=(TextView) convertView.findViewById(R.id.adapter_organization_detail_classinfo);
			holder.adapter_organization_detail_industry=(TextView) convertView.findViewById(R.id.adapter_organization_detail_industry);
			convertView.setTag(holder);
		}
		else {
			holder=(OrganizationDetailHolder) convertView.getTag();
		}
		holder.adapter_organization_detail_name.setText(models.get(position).getUser_name());
		bitmapUtils.display(holder.adapter_organization_detail_image, ParamsManager.bucketName+models.get(position).getAvatar_large(), config);
		if(models.get(position).getClass_admin()==1) {
			holder.adapter_organization_detail_isadmin.setVisibility(View.VISIBLE);
		}
		else {
			holder.adapter_organization_detail_isadmin.setVisibility(View.GONE);
		}
		if(isAdmin==1) {
			if(models.get(position).getClass_role_state()==1) {
				holder.adapter_organization_detail_apply_state.setVisibility(View.GONE);
				holder.adapter_organization_detail_control.setVisibility(View.VISIBLE);
				if(models.get(position).getClass_admin()==1) {
					holder.adapter_organization_detail_manager.setVisibility(View.GONE);
				}
				else {
					holder.adapter_organization_detail_manager.setVisibility(View.VISIBLE);
				}
			}
			else {
				holder.adapter_organization_detail_apply_state.setVisibility(View.VISIBLE);
				holder.adapter_organization_detail_control.setVisibility(View.GONE);
			}
		}
		else {
			holder.adapter_organization_detail_apply_state.setVisibility(View.GONE);
			holder.adapter_organization_detail_control.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_manager.setVisibility(View.GONE);
		}
		UserModel model=DB.getInstance(context).getUserModel();
		if(models.get(position).getUser_id()==model.getUser_id()) {
			holder.adapter_organization_detail_talk.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_talk.setVisibility(View.VISIBLE);
		}
		//
		final TextView adapter_organization_detail_apply_state_temp=holder.adapter_organization_detail_apply_state;
		final String tipMessage=type.equals("ClassInfoModel")?
				"��ȷ����/������Ǳ���ͬѧ�������Ϣʧ�棬�����ڷ��գ�\n���ͨ������/�����������������˵���ϵ��Ϣ�����ҽ���Ϊ�����ͬѧչ���ڸ�����Ƭ�ϣ�ϵͳ����Ϊ��ݿ��š�":
					"�������ˣ�������ϲ�������������/�������δ��֤��������/��δ�����κΰ༶���������Ϣʧ��ķ��գ�";
		holder.adapter_organization_detail_clicklayout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//���״̬�ؼ���ʾ�����ˣ�����������״̬
				if(adapter_organization_detail_apply_state_temp.getVisibility()==View.VISIBLE) {
					activity.startApply(models.get(position_), tipMessage);
				}
				else {
					Intent intent=new Intent(context, BussinessCardActivity.class);
					Bundle bundle=new Bundle();
					bundle.putInt("user_id", models.get(position_).getUser_id());
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			}});
		holder.adapter_organization_detail_manager.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activity.setAdminOperationChoice(models.get(position_).getUser_id());
			}});
		holder.adapter_organization_detail_talk.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context, MessageActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("user_id", models.get(position_).getUser_id());
				bundle.putString("username", models.get(position_).getUser_name());
				bundle.putString("avatar_large", models.get(position_).getAvatar_large());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}});
		if(models.get(position).getIs_authentication()==1) {
			holder.adapter_organization_detail_image_verify.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_image_verify.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_image_verify.setImageResource(R.drawable.ic_user_verification_no);
		}
		if(models.get(position).getYear().equals("")&&models.get(position).getClass_name().equals("")) {
			holder.adapter_organization_detail_classinfo.setVisibility(View.GONE);
		}
		else {
			holder.adapter_organization_detail_classinfo.setVisibility(View.VISIBLE);
			holder.adapter_organization_detail_classinfo.setText(models.get(position).getYear()+"��"+models.get(position).getClass_name());
		}
		String city=models.get(position).getCity_name();
		String industry=models.get(position).getIndustry();
		String companyname=models.get(position).getCompanyname();
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

class OrganizationDetailHolder {
	LinearLayout adapter_organization_detail_clicklayout=null;
	TextView adapter_organization_detail_name=null;
	TextView adapter_organization_detail_isadmin=null;
	TextView adapter_organization_detail_apply_state=null;
	LinearLayout adapter_organization_detail_control=null;
	ImageView adapter_organization_detail_manager=null;
	ImageView adapter_organization_detail_talk=null;
	ImageView adapter_organization_detail_image=null;
	ImageView adapter_organization_detail_image_verify=null;
	TextView adapter_organization_detail_classinfo=null;
	TextView adapter_organization_detail_industry=null;
}
