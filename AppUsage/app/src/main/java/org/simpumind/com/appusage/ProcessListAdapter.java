package org.simpumind.com.appusage;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.squareup.picasso.Picasso;

import org.simpumind.com.appusage.util.Utils;

import java.util.List;

import static org.simpumind.com.appusage.AppIconRequestHandler.SCHEME_PNAME;


/**
 * Created by simpumind on 3/1/16.
 */
public class ProcessListAdapter extends BaseAdapter {

    private final List<AndroidAppProcess> processes;
    private final LayoutInflater inflater;
    private final Context context;
    private final Picasso picasso;
    private final int iconSize;

    public ProcessListAdapter(Context context, List<AndroidAppProcess> processes) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        this.iconSize = Utils.toPx(context, 46);
        this.picasso = Picasso.with(context);
        this.processes = processes;
    }

    @Override public int getCount() {
        return processes.size();
    }

    @Override public AndroidAppProcess getItem(int position) {
        return processes.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_process, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AndroidAppProcess process = getItem(position);

        ImageView imageView = holder.find(R.id.imageView);
        TextView textView = holder.find(R.id.textView);

        picasso.load(Uri.parse(SCHEME_PNAME + ":" + process.getPackageName()))
                .placeholder(android.R.drawable.sym_def_app_icon)
                .resize(iconSize, iconSize)
                .centerInside()
                .into(imageView);

        textView.setText(Utils.getName(context, process));

        return convertView;
    }

    static class ViewHolder {

        private final SparseArray<View> views = new SparseArray<>();

        private final View view;

        public ViewHolder(View view) {
            this.view = view;
            view.setTag(this);
        }

        public <T extends View> T find(int id) {
            View v = views.get(id);
            if (v == null) {
                v = view.findViewById(id);
                views.put(id, v);
            }
            //noinspection unchecked
            return (T) v;
        }

    }

}
