package com.renyu.alumni.ucenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;

import java.util.ArrayList;

public class SystemMessageAdapter extends BaseAdapter {
	
	ArrayList<String> strs=null;
	Context context=null;
	
	public SystemMessageAdapter(Context context, ArrayList<String> strs) {
		this.context=context;
		this.strs=strs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strs.size();
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
		SystemMessageHolder holder=null;
		if(convertView==null) {
			holder=new SystemMessageHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_systemmessage, null);
			holder.adapter_systemmessage_time=(TextView) convertView.findViewById(R.id.adapter_systemmessage_time);
			holder.adapter_systemmessage_content=(TextView) convertView.findViewById(R.id.adapter_systemmessage_content);
			convertView.setTag(holder);
		}
		else {
			holder=(SystemMessageHolder) convertView.getTag();
		}
		holder.adapter_systemmessage_time.setText(CommonUtils.getTimeFormat(Long.parseLong(strs.get(position).split("&")[0])));
		holder.adapter_systemmessage_content.setText(strs.get(position).split("&")[1]);
		return convertView;
	}

}

class SystemMessageHolder {
	TextView adapter_systemmessage_time=null;
	TextView adapter_systemmessage_content=null;
}
