package com.renyu.alumni.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.model.AluassociationinfoModel;

import java.util.ArrayList;

public class SearchAlumniResultAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<AluassociationinfoModel> models;
	SearchAlumniResultActivity activity;
	
	public SearchAlumniResultAdapter(Context context, ArrayList<AluassociationinfoModel> models, SearchAlumniResultActivity activity) {
		this.models=models;
		this.context=context;
		this.activity=activity;
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
		SearchAlumniResultAdapterHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_organization, null);
			holder=new SearchAlumniResultAdapterHolder();
			holder.adapter_organization_new_adder=(RelativeLayout) convertView.findViewById(R.id.adapter_organization_new_adder);
			holder.adapter_organization_new_apply=(RelativeLayout) convertView.findViewById(R.id.adapter_organization_new_apply);
			holder.adapter_organization_result=(RelativeLayout) convertView.findViewById(R.id.adapter_organization_result);
			holder.adapter_organization_title=(TextView) convertView.findViewById(R.id.adapter_organization_title);
			holder.adapter_organization_new_adder_num=(TextView) convertView.findViewById(R.id.adapter_organization_new_adder_num);
			holder.adapter_organization_new_adder_title=(TextView) convertView.findViewById(R.id.adapter_organization_new_adder_title);
			holder.adapter_organization_new_adder_otherinfo=(TextView) convertView.findViewById(R.id.adapter_organization_new_adder_otherinfo);
			holder.adapter_organization_result_title=(TextView) convertView.findViewById(R.id.adapter_organization_result_title);
			holder.adapter_organization_result_title_otherinfo=(TextView) convertView.findViewById(R.id.adapter_organization_result_title_otherinfo);
			holder.adapter_organization_resultback=(TextView) convertView.findViewById(R.id.adapter_organization_resultback);
			holder.adapter_organization_new_apply_title=(TextView) convertView.findViewById(R.id.adapter_organization_new_apply_title);
			holder.adapter_organization_new_apply_otherinfo=(TextView) convertView.findViewById(R.id.adapter_organization_new_apply_otherinfo);
			holder.adapter_organization_no=(TextView) convertView.findViewById(R.id.adapter_organization_no);
			convertView.setTag(holder);
		}
		else {
			holder=(SearchAlumniResultAdapterHolder) convertView.getTag();
		}

		final AluassociationinfoModel model=(AluassociationinfoModel) models.get(position);
		final int position_=position;
		if(model.getAluassociation_role_state()==0) {
			holder.adapter_organization_resultback.setText("����");
		}
		else if(model.getAluassociation_role_state()==1) {
			holder.adapter_organization_resultback.setText("�Ѽ���");
		}
		else if(model.getAluassociation_role_state()==2) {
			holder.adapter_organization_resultback.setText("�ܾ�");
		}
		else if(model.getAluassociation_role_state()==3) {
			holder.adapter_organization_resultback.setText("δ����");
		}
		else {
			holder.adapter_organization_resultback.setText("");
		}
		holder.adapter_organization_no.setVisibility(View.GONE);
		//����Ա�Ļ����жϵ�ǰ������������
		if(model.getAluassociation_admin()==1) {
			holder.adapter_organization_title.setVisibility(View.GONE);
			holder.adapter_organization_new_adder.setVisibility(View.VISIBLE);
			if(model.getAluassociation_apply()>0) {
				holder.adapter_organization_new_adder_num.setVisibility(View.VISIBLE);
				holder.adapter_organization_new_adder_num.setText(""+model.getAluassociation_apply());
			}
			else {
				holder.adapter_organization_new_adder_num.setVisibility(View.GONE);
			}
			holder.adapter_organization_new_adder_title.setText(model.getAluassociation_name());
			holder.adapter_organization_new_adder_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
			holder.adapter_organization_new_apply.setVisibility(View.GONE);
			holder.adapter_organization_result.setVisibility(View.GONE);
			holder.adapter_organization_new_adder.setOnClickListener(new RelativeLayout.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, OrganizationDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("type", "AluassociationinfoModel");
					bundle.putSerializable("AluassociationinfoModel", models.get(position_));
					if(models.get(position_).getAluassociation_role_state()==3) {
						bundle.putBoolean("isNew", true);
						bundle.putInt("id", models.get(position_).getAluassociation_id());
					}
					else {
						bundle.putBoolean("isNew", false);
					}
					intent.putExtras(bundle);
					context.startActivity(intent);
				}});
		}
		//�ǹ���Ա�Ļ����ж��Ƿ�Ϊ���״̬����һ��״̬
		else {
			if(model.getAluassociation_role_state()==1) {
				holder.adapter_organization_title.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setVisibility(View.VISIBLE);
				holder.adapter_organization_new_adder_num.setVisibility(View.GONE);
				holder.adapter_organization_new_adder_title.setText(model.getAluassociation_name());
				holder.adapter_organization_new_adder_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
				holder.adapter_organization_new_apply.setVisibility(View.GONE);
				holder.adapter_organization_result.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context, OrganizationDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("type", "AluassociationinfoModel");
						bundle.putSerializable("AluassociationinfoModel", models.get(position_));
						if(models.get(position_).getAluassociation_role_state()==3) {
							bundle.putBoolean("isNew", true);
							bundle.putInt("id", models.get(position_).getAluassociation_id());
						}
						else {
							bundle.putBoolean("isNew", false);
						}
						intent.putExtras(bundle);
						context.startActivity(intent);
					}});
			}
			else {
				holder.adapter_organization_title.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setVisibility(View.GONE);
				holder.adapter_organization_new_apply.setVisibility(View.GONE);
				holder.adapter_organization_result.setVisibility(View.VISIBLE);
				holder.adapter_organization_result_title.setText(model.getAluassociation_name());
				holder.adapter_organization_result_title_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
				holder.adapter_organization_result.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context, OrganizationDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("type", "AluassociationinfoModel");
						bundle.putSerializable("AluassociationinfoModel", models.get(position_));
						if(models.get(position_).getAluassociation_role_state()==3) {
							bundle.putBoolean("isNew", true);
							bundle.putInt("id", models.get(position_).getAluassociation_id());
						}
						else {
							bundle.putBoolean("isNew", false);
						}
						intent.putExtras(bundle);
						context.startActivity(intent);
					}});
			}
		}
		return convertView;
	}

}

class SearchAlumniResultAdapterHolder {
	RelativeLayout adapter_organization_new_adder=null;
	RelativeLayout adapter_organization_new_apply=null;
	RelativeLayout adapter_organization_result=null;
	TextView adapter_organization_title=null;
	TextView adapter_organization_new_adder_num=null;
	TextView adapter_organization_new_adder_title=null;
	TextView adapter_organization_new_adder_otherinfo=null;
	TextView adapter_organization_result_title=null;
	TextView adapter_organization_result_title_otherinfo=null;
	TextView adapter_organization_resultback=null;
	TextView adapter_organization_new_apply_title=null;
	TextView adapter_organization_new_apply_otherinfo=null;
	TextView adapter_organization_no=null;
}
