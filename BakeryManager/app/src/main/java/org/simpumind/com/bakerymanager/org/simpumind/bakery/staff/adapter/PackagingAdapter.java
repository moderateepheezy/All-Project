package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Packaging;

import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class PackagingAdapter extends BaseAdapter{

    private Context mContext;
    private List<Packaging> mix;

    LayoutInflater inflater = null;

    public PackagingAdapter(Context context, List<Packaging> mixs){
        this.mContext = context;
        this.mix = mixs;
        this.mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mix.size();
    }

    @Override
    public Packaging getItem(int i) {
        return mix.get(i);
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
            v = li.inflate(R.layout.packaging_fragment_list_item, viewGroup, false);
            holder = new MyViewHolder(v);
            v.setTag(holder);
        }else{
            holder = (MyViewHolder) v.getTag();
        }

        holder.mixerName.setText(mix.get(i).getPackagerName());
        holder.startTime.setText(mix.get(i).getStartTime());
        holder.endTime.setText(mix.get(i).getEndTime());
        holder.date.setText(mix.get(i).getTimeCreated());

        return v;

    }

    public class MyViewHolder{
        TextView mixerName;
        TextView startTime;
        TextView endTime;
        TextView date;

        public MyViewHolder(View convertView){
            mixerName = (TextView)convertView.findViewById(R.id.view_pakger_name);
            startTime = (TextView)convertView.findViewById(R.id.view_start_time);
            endTime = (TextView) convertView.findViewById(R.id.view_end_time);
            date = (TextView) convertView.findViewById(R.id.view_time_created);
        }
    }
}
