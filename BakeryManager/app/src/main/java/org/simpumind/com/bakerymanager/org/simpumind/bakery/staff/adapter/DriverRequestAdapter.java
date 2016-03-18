package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.DriverRequest;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Suggestion;

import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class DriverRequestAdapter extends BaseAdapter{

    private Context mContext;
    private List<DriverRequest> mix;

    LayoutInflater inflater = null;

    public DriverRequestAdapter(Context context, List<DriverRequest> mixs){
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
    public DriverRequest getItem(int i) {
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
            v = li.inflate(R.layout.driver_request_list_item, viewGroup, false);
            holder = new MyViewHolder(v);
            v.setTag(holder);
        }else{
            holder = (MyViewHolder) v.getTag();
        }

        holder.driverName.setText(mix.get(i).getDriver());
        holder.customerName.setText(mix.get(i).getName());
        holder.noOfCake.setText(mix.get(i).getNoOfBurger());
        holder.creation.setText(mix.get(i).getTimeCreated());
        holder.noOfBurger.setText(mix.get(i).getNoOfBurger());
        holder.noOfSSardine.setText(mix.get(i).getNoOfBSardine());
        holder.noOfBSardine.setText(mix.get(i).getNoOfBSardine());
        holder.noOfRegularLoaves.setText(mix.get(i).getNoOfRegularLoaves());
        holder.address.setText(mix.get(i).getAddress());

        return v;

    }

    public class MyViewHolder{
        TextView creation;
        TextView driverName;
        private TextView customerName;
        private TextView noOfCake;
        private TextView noOfBurger;
        private TextView noOfSSardine;
        private TextView noOfBSardine;
        private TextView noOfRegularLoaves;
        private TextView address;

        public MyViewHolder(View convertView){
            noOfBSardine = (TextView)convertView.findViewById(R.id.no_of_big_sardine);
            customerName = (TextView)convertView.findViewById(R.id.name);
            creation = (TextView) convertView.findViewById(R.id.creation);
            driverName = (TextView) convertView.findViewById(R.id.driver_name);
            noOfSSardine = (TextView)convertView.findViewById(R.id.no_small_sardine);
            noOfBurger = (TextView)convertView.findViewById(R.id.no_of_burger);
            noOfRegularLoaves = (TextView)convertView.findViewById(R.id.no_of_regular_loves);
            address = (TextView)convertView.findViewById(R.id.address);
            noOfCake = (TextView)convertView.findViewById(R.id.no_of_cake);
        }
    }
}
