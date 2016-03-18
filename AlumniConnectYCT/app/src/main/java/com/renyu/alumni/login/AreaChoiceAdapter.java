package com.renyu.alumni.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renyu.alumni.R;
import com.renyu.alumni.model.CityModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

public class AreaChoiceAdapter extends SectionedBaseAdapter {
	
	LinkedHashMap<String, ArrayList<CityModel>> mapModel=null;
	ArrayList<String> sectionsList=null;
	Context context=null;
	
	public AreaChoiceAdapter(Context context, LinkedHashMap<String, ArrayList<CityModel>> mapModel, ArrayList<String> sectionsList) {
		this.context=context;
		this.mapModel=mapModel;
		this.sectionsList=sectionsList;
	}

	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		return mapModel.size();
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		return mapModel.get(sectionsList.get(section)).size();
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		AreaChoiceContentHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_areachoice, null);
			holder=new AreaChoiceContentHolder();
			holder.areachoice_name=(TextView) convertView.findViewById(R.id.areachoice_name);
			convertView.setTag(holder);
		}
		else {
			holder=(AreaChoiceContentHolder) convertView.getTag();
		}
		holder.areachoice_name.setText(mapModel.get(sectionsList.get(section)).get(position).getCityName());
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		AreaChoiceTitleHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_areachoice_title, null);
			holder=new AreaChoiceTitleHolder();
			holder.areachoice_title=(TextView) convertView.findViewById(R.id.areachoice_title);
			convertView.setTag(holder);
		}
		else {
			holder=(AreaChoiceTitleHolder) convertView.getTag();
		}
		holder.areachoice_title.setText(sectionsList.get(section).toUpperCase());
		return convertView;
	}

}

class AreaChoiceContentHolder {
	TextView areachoice_name=null;
}

class AreaChoiceTitleHolder {
	TextView areachoice_title=null;
}
