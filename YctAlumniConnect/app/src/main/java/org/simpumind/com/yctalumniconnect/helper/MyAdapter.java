package org.simpumind.com.yctalumniconnect.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.simpumind.com.yctalumniconnect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 1/3/16.
 */
public class MyAdapter extends BaseAdapter{
    private List<Item> items = new ArrayList<>();
    private LayoutInflater inflater;

    public MyAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);

        items.add(new Item("Search", R.drawable.serachs));
        items.add(new Item("Reminisce ", R.drawable.reminisces));
        items.add(new Item("Chat", R.drawable.chats));
        items.add(new Item("Gallery", R.drawable.albums));
        items.add(new Item("News", R.drawable.newss));
        items.add(new Item("About", R.drawable.abt));
        items.add(new Item("Setting", R.drawable.settings));
        items.add(new Item("All Members", R.drawable.all_members));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return items.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = view;
        ImageView picture;
        TextView name;

        if(v == null)
        {
            v = inflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.con, v.findViewById(R.id.con));
            v.setTag(R.id.it, v.findViewById(R.id.it));
        }

        picture = (ImageView)v.getTag(R.id.con);
        name = (TextView)v.getTag(R.id.it);

        Item item = (Item)getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }


    private class Item
    {
        final String name;
        final int drawableId;

        Item(String name, int drawableId)
        {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
