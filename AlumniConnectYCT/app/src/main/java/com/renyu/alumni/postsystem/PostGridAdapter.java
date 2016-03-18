package com.renyu.alumni.postsystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.image.BitmapHelp;

import java.util.ArrayList;

public class PostGridAdapter extends BaseAdapter {
	
	ArrayList<String> pics=null;
	
	Context context=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	public PostGridAdapter(Context context, ArrayList<String> pics) {
		this.pics=pics;
		this.context=context;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PostGridHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_postgridimage, null);
			holder=new PostGridHolder();
			holder.adapter_postimage=(ImageView) convertView.findViewById(R.id.adapter_postimage);
			convertView.setTag(holder);
		}
		else {
			holder=(PostGridHolder) convertView.getTag();
		}
		final int pos_=position;
		if(position==pics.size()-1) {
			holder.adapter_postimage.setImageResource(R.drawable.ic_regist_add);
			holder.adapter_postimage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((SendPostActivity) context).addPic();
				}
			});
		}
		else {
			bitmapUtils.display(holder.adapter_postimage, pics.get(position), config);
			holder.adapter_postimage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((SendPostActivity) context).removePic(pos_);
				}
			});
		}
		
		return convertView;
	}

}

class PostGridHolder {
	ImageView adapter_postimage=null;
}