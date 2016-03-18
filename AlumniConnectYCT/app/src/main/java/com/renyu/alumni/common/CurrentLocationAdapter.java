package com.renyu.alumni.common;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.alumni.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentLocationAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<HashMap<String, String>> maps=null;
	
	public CurrentLocationAdapter(Context context, ArrayList<HashMap<String, String>> maps) {
		this.context=context;
		this.maps=maps;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return maps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return maps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CurrentLocationHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_currentlocation, null);
			holder=new CurrentLocationHolder();
			holder.adapter_currentlocation_title=(TextView) convertView.findViewById(R.id.adapter_currentlocation_title);
			holder.adapter_currentlocation_content=(TextView) convertView.findViewById(R.id.adapter_currentlocation_content);
			convertView.setTag(holder);
		}
		else {
			holder=(CurrentLocationHolder) convertView.getTag();
		}
		if(((CurrentLocationActivity) context).getChoicePosition()==position) {
			convertView.setBackgroundColor(context.getResources().getColor(R.color.image_adapter_background));
		}
		else {
			convertView.setBackgroundColor(Color.WHITE);
		}
		holder.adapter_currentlocation_title.setText(maps.get(position).get("title"));
		holder.adapter_currentlocation_content.setText(maps.get(position).get("content"));
		return convertView;
	}

}

class CurrentLocationHolder {
	TextView adapter_currentlocation_title=null;
	TextView adapter_currentlocation_content=null;
}
