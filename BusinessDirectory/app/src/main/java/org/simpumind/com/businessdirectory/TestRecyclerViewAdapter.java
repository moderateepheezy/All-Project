package org.simpumind.com.businessdirectory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<DirectoryData> peopleDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    public TestRecyclerViewAdapter(ArrayList<DirectoryData> people) {
        this.peopleDataSet = people;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);

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

        textViewName.setText(peopleDataSet.get(listPosition).getName());
        textViewEmail.setText(peopleDataSet.get(listPosition).getEmail());
        textViewAddress.setText(peopleDataSet.get(listPosition).getAddress());
        textViewPhone.setText(peopleDataSet.get(listPosition).getPhoneNo());
    }

    @Override
    public int getItemCount() {
        return peopleDataSet.size();
    }
}