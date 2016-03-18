package org.simpumind.com.coursebook;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linearlistview.LinearListView;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;


public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
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

        LinearListView vertical = (LinearListView) findViewById(R.id.vertical_list);

        vertical.setDividerDrawable(new ColorDrawable(Color.CYAN));
        vertical.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE
                | LinearLayout.SHOW_DIVIDER_BEGINNING);
        vertical.setDividerThickness(getResources().getDimensionPixelSize(R.dimen.padding_small));
        vertical.setAdapter(mAdapter);
        vertical.setOnItemClickListener(mListener);

        ActionSlideExpandableListView list = (ActionSlideExpandableListView)this.findViewById(R.id.list);

        // fill the list with data
        list.setAdapter(buildDummyData());

        // listen for events in the two buttons for every list item.
        // the 'position' var will tell which list item is clicked
        list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

            @Override
            public void onClick(View listView, View buttonview, int position) {

                /**
                 * Normally you would put a switch
                 * statement here, and depending on
                 * view.getId() you would perform a
                 * different action.
                 */
                /*String actionName = "";
                if(buttonview.getId()==R.id.buttonA) {
                    actionName = "buttonA";
                    Intent i = new Intent(MainActivity.this, List.class);
                    startActivity(i);
                } else {
                    actionName = "ButtonB";
                }
                *//**
                 * For testing sake we just show a toast
                 *//*
                Toast.makeText(
                        MainActivity.this,
                        "Clicked Action: "+actionName+" in list item "+position,
                        Toast.LENGTH_SHORT
                ).show();*/
            }

            // note that we also add 1 or more ids to the setItemActionListener
            // this is needed in order for the listview to discover the buttons
        }, R.id.vertical_list);
    }

    /**
     * Builds dummy data for the test.
     * In a real app this would be an adapter
     * for your data. For example a CursorAdapter
     */
    public ListAdapter buildDummyData() {
        final int SIZE = 20;
        String[] values = new String[SIZE];
        for(int i=0;i<SIZE;i++) {
            values[i] = "Item "+i;
        }
        return new ArrayAdapter<String>(
                this,
                R.layout.expandable_list_item,
                R.id.text,
                values
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.menu_change) {
            mCount = (int) (Math.random() * 50);
            mAdapter.notifyDataSetChanged();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
