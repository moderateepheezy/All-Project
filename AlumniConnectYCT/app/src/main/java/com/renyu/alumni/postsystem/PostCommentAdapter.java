package com.renyu.alumni.postsystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.PostCommentsModel;
import com.renyu.alumni.myview.CircleImageView;

import java.util.ArrayList;

public class PostCommentAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<PostCommentsModel> models=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	public PostCommentAdapter(Context context, ArrayList<PostCommentsModel> models) {
		this.context=context;
		this.models=models;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_organization_user_default));	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return models.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return models.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PostCommentHolder holder=null;
		if(convertView==null) {
			holder=new PostCommentHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_postcomment, null);
			holder.adapter_postcomment_pubtime=(TextView) convertView.findViewById(R.id.adapter_postcomment_pubtime);
			holder.adapter_postcomment_username=(TextView) convertView.findViewById(R.id.adapter_postcomment_username);
			holder.adapter_postcomment_grade=(TextView) convertView.findViewById(R.id.adapter_postcomment_grade);
			holder.adapter_postcomment_desp=(TextView) convertView.findViewById(R.id.adapter_postcomment_desp);
			holder.adapter_postcomment_userimage=(CircleImageView) convertView.findViewById(R.id.adapter_postcomment_userimage);
			convertView.setTag(holder);
		}
		else {
			holder=(PostCommentHolder) convertView.getTag();
		}
		holder.adapter_postcomment_pubtime.setText(CommonUtils.getPulicIndexTimeExtra(Long.parseLong(models.get(position).getGenerate_time()+"000")));
		holder.adapter_postcomment_username.setText(models.get(position).getUser_name());
		holder.adapter_postcomment_desp.setText(models.get(position).getComent_content());
		bitmapUtils.display(holder.adapter_postcomment_userimage, ParamsManager.bucketName+models.get(position).getAvatar_large()+"?imageView/2/w/200/h/200", config);
		String gradle=models.get(position).getYear()+"�� "+models.get(position).getCollege_name();
		if(!gradle.trim().equals("��")) {
			holder.adapter_postcomment_grade.setText(gradle.trim());
			holder.adapter_postcomment_grade.setVisibility(View.VISIBLE);
		}
		else {
			holder.adapter_postcomment_grade.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

}

class PostCommentHolder {
	CircleImageView adapter_postcomment_userimage=null;
	TextView adapter_postcomment_pubtime=null;
	TextView adapter_postcomment_username=null;
	TextView adapter_postcomment_grade=null;
	TextView adapter_postcomment_desp=null;
}
