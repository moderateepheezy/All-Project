package org.simpumind.com.business.org.simpumind.com.business;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.simpumind.com.business.R;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestListViewAdapter extends ArrayAdapter<Object> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public TestListViewAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_HEADER: {
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cards_layout, parent, false);

                    View cardView = (CardView)convertView.findViewById(R.id.card_view);
                    //View cardLayout = mCardView.findViewById(R.id.card_layout);
                    cardView.setBackgroundColor(Color.GREEN);

                }
                break;
                case TYPE_CELL: {
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cards_layout, parent, false);
                }
                break;
            }
        }
        return convertView;
    }
}