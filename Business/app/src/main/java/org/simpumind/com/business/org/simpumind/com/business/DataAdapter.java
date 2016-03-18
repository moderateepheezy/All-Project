package org.simpumind.com.business.org.simpumind.com.business;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;

import org.simpumind.com.business.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simpumind on 12/2/15.
 */
public class DataAdapter extends BetterRecyclerAdapter<DirectoryData, DataAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position);
        DirectoryData dr = getItem(position);
        holder.textViewName.setText(dr.getName());
        holder.textViewEmail.setText(dr.getEmail());
        holder.textViewAddress.setText(dr.getAddress());
        holder.textViewPhone.setText(dr.getPhoneNo());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.companyName)TextView textViewName;
        @Bind(R.id.companyEmail) TextView textViewEmail;
        @Bind(R.id.companyPhone) TextView textViewPhone;
        @Bind(R.id.companyAddress)TextView textViewAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addAll(List<DirectoryData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public List<DirectoryData> filter(List<DirectoryData> models, String query) {
        query = query.toLowerCase();

        final List<DirectoryData> filteredModelList = new ArrayList<>();
        for (DirectoryData model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void animateTo(List<DirectoryData> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<DirectoryData> newModels) {
        for (int i = items.size() - 1; i >= 0; i--) {
            final DirectoryData model = items.get(i);
            if (!newModels.contains(model)) {
                remove(i);
            }
        }
    }

    @Override
    public void add(int index, DirectoryData object) {
        super.add(index, object);
    }

    @Override
    public DirectoryData remove(int index) {
        return super.remove(index);
    }

    private void applyAndAnimateAdditions(List<DirectoryData> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final DirectoryData model = newModels.get(i);
            if (!items.contains(model)) {
                add(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<DirectoryData> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final DirectoryData model = newModels.get(toPosition);
            final int fromPosition = items.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
