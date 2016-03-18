package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Baking;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class BakingAdapter extends BaseAdapter{

    private Context mContext;
    private List<Baking> bak;

    LayoutInflater inflater = null;

    public BakingAdapter(Context context, List<Baking> baks){
        this.mContext = context;
        this.bak = baks;
        this.mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bak.size();
    }

    @Override
    public Baking getItem(int i) {
        return bak.get(i);
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
            v = li.inflate(R.layout.baking_fragment_list_item, viewGroup, false);
            holder = new MyViewHolder(v);
            v.setTag(holder);
        }else{
            holder = (MyViewHolder) v.getTag();
        }

        holder.bakerName.setText(bak.get(i).getBakerName());
        holder.startTime.setText(bak.get(i).getStartTime());
        holder.endTime.setText(bak.get(i).getEndTime());
        holder.date.setText(bak.get(i).getTimeCreated());
        holder.duration.setText(bak.get(i).getDuration());


        return v;

    }

    public class MyViewHolder{
        TextView bakerName;
        TextView startTime;
        TextView endTime;
        TextView date;
        TextView duration;

        public MyViewHolder(View convertView){
            bakerName = (TextView)convertView.findViewById(R.id.view_baker_name);
            startTime = (TextView)convertView.findViewById(R.id.view_start_time);
            endTime = (TextView) convertView.findViewById(R.id.view_end_time);
            date = (TextView) convertView.findViewById(R.id.view_time_created);
            duration = (TextView) convertView.findViewById(R.id.view_duration);
        }
    }
}
