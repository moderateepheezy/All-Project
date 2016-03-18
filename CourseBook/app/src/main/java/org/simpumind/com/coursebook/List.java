package org.simpumind.com.coursebook;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linearlistview.LinearListView;

public class List extends AppCompatActivity {

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }
            ((TextView) convertView).setText("Position " + position);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mCount;
        }
    };

    LinearListView.OnItemClickListener mListener = new LinearListView.OnItemClickListener() {

        @Override
        public void onItemClick(LinearListView parent, View view, int position,
                                long id) {
            Toast.makeText(getApplicationContext(),
                    "Tapped position " + position, Toast.LENGTH_SHORT).show();

        }
    };

    private int mCount = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        LinearListView horizontal = (LinearListView) findViewById(R.id.horizontal_list);
        LinearListView vertical = (LinearListView) findViewById(R.id.vertical_list);

        vertical.setDividerDrawable(new ColorDrawable(Color.CYAN));
        vertical.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE
                | LinearLayout.SHOW_DIVIDER_BEGINNING);

        horizontal.setDividerDrawable(getResources().getDrawable(R.mipmap.divider_vertical_holo_dark));

        horizontal.setDividerThickness(getResources().getDimensionPixelSize(R.dimen.padding_medium));
        vertical.setDividerThickness(getResources().getDimensionPixelSize(R.dimen.padding_small));

        horizontal.setAdapter(mAdapter);
        vertical.setAdapter(mAdapter);

        horizontal.setOnItemClickListener(mListener);
        vertical.setOnItemClickListener(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_change) {
            mCount = (int) (Math.random() * 50);
            mAdapter.notifyDataSetChanged();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}
