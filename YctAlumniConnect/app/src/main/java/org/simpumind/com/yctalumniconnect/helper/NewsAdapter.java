package org.simpumind.com.yctalumniconnect.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.simpumind.com.yctalumniconnect.R;
import org.simpumind.com.yctalumniconnect.model.News;

import java.util.List;

/**
 * Created by simpumind on 1/6/16.
 */
public class NewsAdapter extends BaseAdapter{

    private Context mContext;
    private List<News> news;

    LayoutInflater inflater = null;

    public NewsAdapter(Context context, List<News> news){
        this.mContext = context;
        this.news = news;
        this.mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public News getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder holder;
        View v = convertView;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.news_item, viewGroup, false);
            holder = new MyViewHolder(v);
            v.setTag(holder);
        }else{
            holder = (MyViewHolder) v.getTag();
        }

        holder.headings.setText(news.get(i).getHeading());
        holder.newses.setText(news.get(i).getNews());
        holder.datee.setText( news.get(i).getTime());

        return v;

    }

    public class MyViewHolder {
        TextView headings;
        TextView newses;
        TextView datee;

        public MyViewHolder(View convertView) {
            headings = (TextView) convertView.findViewById(R.id.news_heading);
            newses = (TextView) convertView.findViewById(R.id.news);
            datee = (TextView) convertView.findViewById(R.id.date);
        }
    }
}
