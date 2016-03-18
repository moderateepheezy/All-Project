package com.renyu.alumni.postsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.ImageChoiceModel;

import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter {
	
	BitmapUtils bitmapUtils=null;
	BitmapDisplayConfig config=null;
	
	Context context=null;
	
	ArrayList<ImageChoiceModel> picInfoList=null;
	
	public ImageGridAdapter(Context context, ArrayList<ImageChoiceModel> picInfoList) {
		this.context=context;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		
		this.picInfoList=picInfoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return picInfoList.size();
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
		ActivityGridHolder holder=null;
		if(convertView==null) {
			holder=new ActivityGridHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_gridactivity, null);
			holder.activitygridheadimage=(ImageView) convertView.findViewById(R.id.gridactivity_image);
			convertView.setTag(holder);
		}
		else {
			holder=(ActivityGridHolder) convertView.getTag();
		}
		bitmapUtils.display(holder.activitygridheadimage, picInfoList.get(position).getPath()+"?imageView/2/w/200/h/200", config, null);		
		return convertView;
	}

}

class ActivityGridHolder {
	ImageView activitygridheadimage=null;
}
