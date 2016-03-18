package com.renyu.alumni.main2.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.PublicIndexModel;

import java.util.ArrayList;

public class DetailChildFragmentAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<PublicIndexModel> models=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;
	
	public DetailChildFragmentAdapter(Context context, ArrayList<PublicIndexModel> models) {
		this.context=context;
		this.models=models;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.pic_news_icon_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.pic_news_icon_default));	
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
		DetailChildFragmentHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_fragment_detailchild, null);
			holder=new DetailChildFragmentHolder();
			holder.adapter_fragment_detailchild_type=(TextView) convertView.findViewById(R.id.adapter_fragment_detailchild_type);
			holder.adapter_fragment_detailchild_title=(TextView) convertView.findViewById(R.id.adapter_fragment_detailchild_title);
			holder.adapter_fragment_detailchild_username=(TextView) convertView.findViewById(R.id.adapter_fragment_detailchild_username);
			holder.adapter_fragment_detailchild_time=(TextView) convertView.findViewById(R.id.adapter_fragment_detailchild_time);
			holder.adapter_fragment_detailchild_viewnums=(TextView) convertView.findViewById(R.id.adapter_fragment_detailchild_viewnums);
			holder.adapter_fragment_detailchild_image=(ImageView) convertView.findViewById(R.id.adapter_fragment_detailchild_image);
			convertView.setTag(holder);
		}
		else {
			holder=(DetailChildFragmentHolder) convertView.getTag();
		}
		switch(models.get(position).getResource_type()) {
		case 1:
			holder.adapter_fragment_detailchild_type.setText("����");
			break;
		case 2:
			holder.adapter_fragment_detailchild_type.setText("�");
			break;
		case 3:
			holder.adapter_fragment_detailchild_type.setText("��Ƹ");
			break;
		case 4:
			holder.adapter_fragment_detailchild_type.setText("����");
			break;
		case 5:
			holder.adapter_fragment_detailchild_type.setText("����");
			break;
		case 6:
			holder.adapter_fragment_detailchild_type.setText("˽��������");
			break;
		case 7:
			holder.adapter_fragment_detailchild_type.setText("˽����У��");
			break;
		}
		holder.adapter_fragment_detailchild_title.setText(models.get(position).getResource_title());
		holder.adapter_fragment_detailchild_username.setText(models.get(position).getUser_name());
		holder.adapter_fragment_detailchild_time.setText(CommonUtils.getPulicIndexTimeExtra(Long.parseLong(models.get(position).getGenerate_time()+"000")));
		holder.adapter_fragment_detailchild_viewnums.setText(models.get(position).getView_times()+"�����");
		if(models.get(position).getResource_pic().equals("")) {
			holder.adapter_fragment_detailchild_image.setVisibility(View.GONE);
			holder.adapter_fragment_detailchild_image.setImageResource(R.drawable.pic_news_icon_default);
		}
		else {
			String res=models.get(position).getResource_pic();
			if(res.indexOf("http")==-1) {
				res=ParamsManager.bucketName+models.get(position).getResource_pic();
			}
			bitmapUtils.display(holder.adapter_fragment_detailchild_image, res+"?imageView/2/w/200/h/200", config);
			holder.adapter_fragment_detailchild_image.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

}

class DetailChildFragmentHolder {
	TextView adapter_fragment_detailchild_type=null;
	TextView adapter_fragment_detailchild_title=null;
	TextView adapter_fragment_detailchild_username=null;
	TextView adapter_fragment_detailchild_time=null;
	TextView adapter_fragment_detailchild_viewnums=null;
	ImageView adapter_fragment_detailchild_image=null;
}
