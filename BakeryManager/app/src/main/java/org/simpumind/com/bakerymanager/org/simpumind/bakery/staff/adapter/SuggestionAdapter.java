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
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Suggestion;

import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class SuggestionAdapter extends BaseAdapter{

    private Context mContext;
    private List<Suggestion> mix;

    LayoutInflater inflater = null;

    public SuggestionAdapter(Context context, List<Suggestion> mixs){
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
    public Suggestion getItem(int i) {
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
            v = li.inflate(R.layout.suggestion_list_item, viewGroup, false);
            holder = new MyViewHolder(v);
            v.setTag(holder);
        }else{
            holder = (MyViewHolder) v.getTag();
        }

        holder.name.setText(mix.get(i).getName());
        holder.suggestion.setText(mix.get(i).getSuggestion());
        holder.creation.setText(mix.get(i).getCreation());

        return v;

    }

    public class MyViewHolder{
        TextView creation;
        TextView name;
        TextView suggestion;

        public MyViewHolder(View convertView){
            name = (TextView)convertView.findViewById(R.id.name);
            suggestion = (TextView)convertView.findViewById(R.id.suggestion);
            creation = (TextView) convertView.findViewById(R.id.creation);
        }
    }
}
