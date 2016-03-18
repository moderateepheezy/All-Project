package org.simpumind.com.nigeriasenator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 10/31/15.
 */
public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<HistoryType> mItems;

    public GridAdapter() {
        super();
        mItems = new ArrayList<HistoryType>();
        HistoryType species = new HistoryType();
        species.setName("How Was The Senate Formed ");
        species.setThumbnail(R.drawable.nig5);
        mItems.add(species);

        species = new HistoryType();
        species.setName("Breakdown Of Nigerian Senator Earnings");
        species.setThumbnail(R.drawable.nig2);
        mItems.add(species);

        species = new HistoryType();
        species.setName("Nigeria Constitution About Electing Senate");
        species.setThumbnail(R.drawable.nig3);
        mItems.add(species);

        species = new HistoryType();
        species.setName("Brief Write-up About Senator");
        species.setThumbnail(R.drawable.nig4);
        mItems.add(species);

        species = new HistoryType();
        species.setName("How Political Parties are Voted in The Senatorial System");
        species.setThumbnail(R.drawable.nig1);
        mItems.add(species);

        species = new HistoryType();
        species.setName("History Of Nigeria");
        species.setThumbnail(R.drawable.nig6);
        mItems.add(species);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HistoryType nature = mItems.get(i);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvspecies;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView)itemView.findViewById(R.id.tv_species);
        }
    }
}
