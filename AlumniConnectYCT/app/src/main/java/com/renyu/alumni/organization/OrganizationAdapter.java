package com.renyu.alumni.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.model.AluassociationinfoModel;
import com.renyu.alumni.model.ClassInfoModel;
import com.renyu.alumni.model.CreateclassinfoModel;
import com.renyu.alumni.model.NoOrganizationModel;

import java.util.ArrayList;

public class OrganizationAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<Object> obj;
	OrganizationFragment fragment;
	
	public OrganizationAdapter(Context context, ArrayList<Object> obj, OrganizationFragment fragment) {
		this.context=context;
		this.obj=obj;
		this.fragment=fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return obj.size();
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
		OrganizationHolder holder=null;
		if(convertView==null) {
			holder=new OrganizationHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_organization, null);
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
			holder=(OrganizationHolder) convertView.getTag();
		}
		if(obj.get(position) instanceof String) {
			holder.adapter_organization_no.setVisibility(View.GONE);
			holder.adapter_organization_title.setVisibility(View.VISIBLE);
			holder.adapter_organization_new_adder.setVisibility(View.GONE);
			holder.adapter_organization_new_apply.setVisibility(View.GONE);
			holder.adapter_organization_result.setVisibility(View.GONE);
			holder.adapter_organization_title.setText(obj.get(position).toString());
		}
		else if(obj.get(position) instanceof ClassInfoModel) {
			holder.adapter_organization_no.setVisibility(View.GONE);
			final ClassInfoModel model=(ClassInfoModel) obj.get(position);
			//����Ա�Ļ����жϵ�ǰ������������
			if(model.getClass_admin()==1) {
				holder.adapter_organization_title.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setVisibility(View.VISIBLE);
				if(model.getStudent_apply()>0) {
					holder.adapter_organization_new_adder_num.setVisibility(View.VISIBLE);
					holder.adapter_organization_new_adder_num.setText(""+model.getStudent_apply());
				}
				else {
					holder.adapter_organization_new_adder_num.setVisibility(View.GONE);
				}
				holder.adapter_organization_new_adder_title.setText(model.getClass_name());
				holder.adapter_organization_new_adder_otherinfo.setText("�����ˣ�"+model.getAdmin_name()+"  �Ѽ��룺"+model.getStudent_num()+"��");
				holder.adapter_organization_new_apply.setVisibility(View.GONE);
				holder.adapter_organization_result.setVisibility(View.GONE);

				holder.adapter_organization_new_adder.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jumpToOrganizationDetail(model);
					}});
			}
			//�ǹ���Ա�Ļ����ж��Ƿ�Ϊ���״̬����һ��״̬
			else {
				if(model.getClass_role_state()==1) {
					holder.adapter_organization_title.setVisibility(View.GONE);
					holder.adapter_organization_new_adder.setVisibility(View.VISIBLE);
					holder.adapter_organization_new_adder_num.setVisibility(View.GONE);
					holder.adapter_organization_new_adder_title.setText(model.getClass_name());
					holder.adapter_organization_new_adder_otherinfo.setText("�����ˣ�"+model.getAdmin_name()+"  �Ѽ��룺"+model.getStudent_num()+"��");
					holder.adapter_organization_new_apply.setVisibility(View.GONE);
					holder.adapter_organization_result.setVisibility(View.GONE);
					holder.adapter_organization_new_adder.setOnClickListener(new RelativeLayout.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							jumpToOrganizationDetail(model);
						}});
				}
				else {
					holder.adapter_organization_title.setVisibility(View.GONE);
					holder.adapter_organization_new_adder.setVisibility(View.GONE);
					holder.adapter_organization_new_apply.setVisibility(View.GONE);
					holder.adapter_organization_result.setVisibility(View.VISIBLE);
					holder.adapter_organization_result_title.setText(model.getClass_name());
					holder.adapter_organization_result_title_otherinfo.setText("�����ˣ�"+model.getAdmin_name());
					if(model.getClass_role_state()==0) {
						holder.adapter_organization_resultback.setText("����");
					}
					else if(model.getClass_role_state()==2) {
						holder.adapter_organization_resultback.setText("�ܾ�");
					}
					holder.adapter_organization_result.setOnClickListener(new RelativeLayout.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							jumpToOrganizationDetail(model);
						}});
				}
			}
		}
		else if(obj.get(position) instanceof CreateclassinfoModel) {
			holder.adapter_organization_no.setVisibility(View.GONE);
			final CreateclassinfoModel model=(CreateclassinfoModel) obj.get(position);
			//����Աֱ����ʾ���������¼��һ���û�ֻ��ʾ������
			if(model.getSuper_admin()==1) {
				holder.adapter_organization_title.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setVisibility(View.GONE);
				holder.adapter_organization_new_apply.setVisibility(View.VISIBLE);
				holder.adapter_organization_new_apply.setOnClickListener(new LinearLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context, ReviewClassActivity.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable("model", model);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}});
				holder.adapter_organization_new_apply_title.setText(model.getClass_name());
				holder.adapter_organization_new_apply_otherinfo.setText("�����ˣ�"+model.getCreate_username());
				holder.adapter_organization_result.setVisibility(View.GONE);
			}
			else {
				holder.adapter_organization_title.setVisibility(View.GONE);
				holder.adapter_organization_new_adder.setVisibility(View.GONE);
				holder.adapter_organization_new_apply.setVisibility(View.GONE);
				holder.adapter_organization_result.setVisibility(View.VISIBLE);
				holder.adapter_organization_result_title.setText(model.getClass_name());
				holder.adapter_organization_result_title_otherinfo.setText("�����ˣ�"+model.getCreate_username());
				if(model.getClass_state()==0) {
					holder.adapter_organization_resultback.setText("����");
				}
				else if(model.getClass_state()==2) {
					holder.adapter_organization_resultback.setText("�ܾ�");
				}
				holder.adapter_organization_result.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						jumpToOrganizationDetail(model);
					}});
			}
		}
		else if(obj.get(position) instanceof AluassociationinfoModel) {
			holder.adapter_organization_no.setVisibility(View.GONE);
			final AluassociationinfoModel model=(AluassociationinfoModel) obj.get(position);
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
						jumpToOrganizationDetail(model);
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
							jumpToOrganizationDetail(model);
						}});
				}
				else {
					holder.adapter_organization_title.setVisibility(View.GONE);
					holder.adapter_organization_new_adder.setVisibility(View.GONE);
					holder.adapter_organization_new_apply.setVisibility(View.GONE);
					holder.adapter_organization_result.setVisibility(View.VISIBLE);
					holder.adapter_organization_result_title.setText(model.getAluassociation_name());
					holder.adapter_organization_result_title_otherinfo.setText("����Ա��"+model.getAdmin_name()+"  �Ѽ��룺"+model.getAluassociation_num()+"��");
					if(model.getAluassociation_role_state()==0) {
						holder.adapter_organization_resultback.setText("����");
						
					}
					else if(model.getAluassociation_role_state()==2) {
						holder.adapter_organization_resultback.setText("�ܾ�");
					}
					holder.adapter_organization_result.setOnClickListener(new RelativeLayout.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							jumpToOrganizationDetail(model);
						}});
				}
			}
		}
		else if(obj.get(position) instanceof NoOrganizationModel) {
			final NoOrganizationModel model=(NoOrganizationModel) obj.get(position);
			holder.adapter_organization_no.setVisibility(View.VISIBLE);
			holder.adapter_organization_no.setText(model.getNoOrganizationMessage());
			holder.adapter_organization_title.setVisibility(View.GONE);
			holder.adapter_organization_new_adder.setVisibility(View.GONE);
			holder.adapter_organization_new_apply.setVisibility(View.GONE);
			holder.adapter_organization_result.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private void jumpToOrganizationDetail(Object model) {
		Intent intent=new Intent(context, OrganizationDetailActivity.class);
		Bundle bundle=new Bundle();
		if(model instanceof ClassInfoModel) {
			bundle.putString("type", "ClassInfoModel");
			bundle.putSerializable("ClassInfoModel", (ClassInfoModel) model);
		}
		else if(model instanceof CreateclassinfoModel) {
			bundle.putString("type", "CreateclassinfoModel");
			bundle.putSerializable("CreateclassinfoModel", (CreateclassinfoModel) model);
		}
		else if(model instanceof AluassociationinfoModel) {
			bundle.putString("type", "AluassociationinfoModel");
			bundle.putSerializable("AluassociationinfoModel", (AluassociationinfoModel) model);
		}
		bundle.putBoolean("isNew", false);
		bundle.putBoolean("refreshCurrent", true);
		intent.putExtras(bundle);
		fragment.startActivityForResult(intent, ParamsManager.ORGANIZATION_REFRESH);
	}

}

class OrganizationHolder {
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
