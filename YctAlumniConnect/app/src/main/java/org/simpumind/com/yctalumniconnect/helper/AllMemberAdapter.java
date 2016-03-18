package org.simpumind.com.yctalumniconnect.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;

import org.simpumind.com.yctalumniconnect.model.AllMember;
import org.simpumind.com.yctalumniconnect.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simpumind on 1/5/16.
 */
public class AllMemberAdapter extends BetterRecyclerAdapter<AllMember, AllMemberAdapter.ViewHolder> {

    Context c;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lsv_item_news_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position);
        AllMember am = getItem(position);
        holder.user.setText(am.getFirstName() + " " + am.getLastName());
        holder.depart.setText(am.getCourse());
        if(items.size() == 0){
            holder.userIm.setTitleText("A");
        }else{
            holder.userIm.setTitleText(items.get(position).getFirstName().substring(0, 1).toUpperCase());
        }
        if(position%2 == 0){
            holder.userIm.setBackgroundColor(c.getResources().getColor(R.color.green));
        }else{
            holder.userIm.setBackgroundColor(c.getResources().getColor(R.color.red));
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.userName)         public TextView user;
        @Bind(R.id.department)   public TextView depart;
        @Bind(R.id.rlv_name_view) public  RoundedLetterView userIm;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}