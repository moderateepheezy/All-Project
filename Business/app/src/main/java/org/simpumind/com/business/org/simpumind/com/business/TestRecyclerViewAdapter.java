package org.simpumind.com.business.org.simpumind.com.business;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.simpumind.com.business.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.MyViewHolder> {

    private List<DirectoryData> peopleDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewPhone;
        TextView textViewAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.companyName);
            this.textViewEmail = (TextView) itemView.findViewById(R.id.companyEmail);
            this.textViewPhone = (TextView) itemView.findViewById(R.id.companyPhone);
            this.textViewAddress = (TextView) itemView.findViewById(R.id.companyAddress);
            this.img = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public TestRecyclerViewAdapter(List<DirectoryData> people) {
        this.peopleDataSet = people;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

       /* view.setOnClickListener(MainActivity.myOnClickListener);*/

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewEmail = holder.textViewEmail;
        TextView textViewAddress = holder.textViewAddress;
        TextView textViewPhone = holder.textViewPhone;
        ImageView imageViewImagee = holder.img;

        DirectoryData drw = peopleDataSet.get(listPosition);

        textViewName.setText(drw.getName());
        textViewEmail.setText(drw.getEmail());
        textViewAddress.setText(drw.getAddress());
        textViewPhone.setText(drw.getPhoneNo());
        imageViewImagee.getResources().getDrawable(R.drawable.business);
    }

    @Override
    public int getItemCount() {
        return peopleDataSet.size();
    }
}