package com.renyu.alumni.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.renyu.alumni.R;
import com.renyu.alumni.login.AreaChoiceAdapter;
import com.renyu.alumni.model.CityModel;
import com.renyu.alumni.myview.IndexScrollerView;
import com.renyu.alumni.myview.IndexScrollerView.OnSectionListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

public class AreaChoiceFragment extends Fragment {
	
	View view=null;
	
	//ÿ��section��Ԫ��
	LinkedHashMap<String, ArrayList<CityModel>> mapModel=null;
	//����section
	ArrayList<String> sectionsList=null;
	//����section������Ԫ��
	ArrayList<CityModel> cityAlls=null;
	//����section������Ԫ��
	ArrayList<String> cityAllsWithString=null;
	
	PinnedHeaderListView areachoice_list=null;
	AreaChoiceAdapter adapter=null;
	IndexScrollerView areachoice_index=null;
	
	public static final AreaChoiceFragment newInstance(boolean isDomestic) {
		AreaChoiceFragment f=new AreaChoiceFragment();
		Bundle bundle=new Bundle();
		bundle.putBoolean("isDomestic", isDomestic);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null) {
			cityAlls=new ArrayList<CityModel>();
			cityAllsWithString=new ArrayList<String>();
			mapModel=new LinkedHashMap<String, ArrayList<CityModel>>();
			sectionsList=new ArrayList<String>();
			if(getArguments().getBoolean("isDomestic")) {
				CommonUtils.getAllCity(getActivity(), mapModel, sectionsList, cityAlls, cityAllsWithString);
			}
			else {
				CommonUtils.getAllInternationCity(getActivity(), mapModel, sectionsList, cityAlls, cityAllsWithString);
			}
			
			view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_areachoice, null);
			
			areachoice_list=(PinnedHeaderListView) view.findViewById(R.id.domestic_areachoice_list);
			adapter=new AreaChoiceAdapter(getActivity(), mapModel, sectionsList);
			areachoice_list.setAdapter(adapter);
			areachoice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(cityAlls.get(position)!=null) {
						((com.renyu.alumni.common.AreaChoiceActivity) getActivity()).back(cityAlls.get(position));
					}				
				}
			});
			
			areachoice_index=(IndexScrollerView) view.findViewById(R.id.domestic_areachoice_index);
			areachoice_index.setOnSectionListener(new OnSectionListener() {
				
				@Override
				public void getSection(int section) {
					// TODO Auto-generated method stub
					String sections="#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					String letter=String.valueOf(sections.charAt(section));
					int pos=cityAllsWithString.indexOf(letter.toLowerCase());
					areachoice_list.setSelection(pos);
				}
			});
		}
		ViewGroup parent=(ViewGroup) view.getParent();
		if(parent!=null) {
			parent.removeView(view);
		}
		return view;
	}
}
