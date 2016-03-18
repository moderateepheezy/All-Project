/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.renyu.alumni.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.renyu.alumni.R;
import com.renyu.alumni.image.BitmapHelp;
import com.renyu.alumni.model.NewsModel;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.RecyclingPagerAdapter;

public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<NewsModel> imageIdList;

    private int size;
    private boolean isInfiniteLoop;
    
    BitmapUtils bitmapUtils=null;;
	BitmapDisplayConfig config=null;

    public ImagePagerAdapter(Context context, List<NewsModel> imageIdList) {
        this.context=context;
        this.imageIdList=imageIdList;
        this.size=getSize(imageIdList);
        isInfiniteLoop=false;
        
        bitmapUtils=BitmapHelp.getBitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		config=new BitmapDisplayConfig();
		config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.pic_activity_default));
		config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.pic_activity_default));	
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop?Integer.MAX_VALUE:getSize(imageIdList);
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop?position%size:position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder=new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.adapter_newsimage, null);
            holder.adapter_viewpager_news_image=(ImageView) view.findViewById(R.id.adapter_viewpager_news_image);
            holder.adapter_viewpager_news_title=(TextView) view.findViewById(R.id.adapter_viewpager_news_title);
            view.setTag(holder);
        } else {
            holder=(ViewHolder)view.getTag();
        }
        bitmapUtils.display(holder.adapter_viewpager_news_image, imageIdList.get(getPosition(position)).getPic(), config);
        holder.adapter_viewpager_news_title.setText(imageIdList.get(getPosition(position)).getTitle());
        final int position_=position;
        view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context, NewsDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("url", imageIdList.get(getPosition(position_)).getUrl());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}});
        return view;
    }

    private static class ViewHolder {
    	ImageView adapter_viewpager_news_image=null;
    	TextView adapter_viewpager_news_title=null;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
    
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null?0:sourceList.size();
    }
}
