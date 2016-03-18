package com.renyu.alumni.news;

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
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.NewsModel;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {
	
	ArrayList<NewsModel> newsModels=null;
	Context context=null;
	
	BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;

	public NewsAdapter(Context context, ArrayList<NewsModel> newsModels) {
		this.context=context;
		this.newsModels=newsModels;
		
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.pic_news_icon_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.pic_news_icon_default));
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsModels.size();
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
		NewsHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_news, null);
			holder=new NewsHolder();
			holder.adapter_news_image=(ImageView) convertView.findViewById(R.id.adapter_news_image);
			holder.adapter_news_title=(TextView) convertView.findViewById(R.id.adapter_news_title);
			holder.adapter_news_time=(TextView) convertView.findViewById(R.id.adapter_news_time);
			convertView.setTag(holder);
		}
		else {
			holder=(NewsHolder) convertView.getTag();
		}
		bitmapUtils.display(holder.adapter_news_image, newsModels.get(position).getPic(), config);
		holder.adapter_news_title.setText(newsModels.get(position).getTitle());
		holder.adapter_news_time.setText(newsModels.get(position).getNewstime().substring(0, newsModels.get(position).getNewstime().indexOf("T")));
		return convertView;
	}

}

class NewsHolder {
	ImageView adapter_news_image=null;
	TextView adapter_news_title=null;
	TextView adapter_news_time=null;
}
