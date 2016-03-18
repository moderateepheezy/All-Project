package org.simpumind.com.nigeriasenator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Context c;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Nigeria Senator");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setIcon(R.drawable.ic_launcher);


        // Calling the RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        switch (position){
                            case 0:
                                intent = new Intent(MainActivity.this, ContentDisplay.class);
                                intent.putExtra("formed", readSavedData("senateform", getApplicationContext()));
                                intent.putExtra("formTitle", "How Was The Senate Formed");
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(MainActivity.this, ContentDisplay.class);
                                intent.putExtra("earn", readSavedData("senateearn", getApplicationContext()));
                                intent.putExtra("earnTitle", "How Much The Senate Earn");
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, ContentDisplay.class);
                                intent.putExtra("constitue", readSavedData("constitutesay", getApplicationContext()));
                                intent.putExtra("conTitle", "Nigeria constitution About Electing Senate");
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(MainActivity.this, ContentDisplay.class);
                                intent.putExtra("voted", readSavedData("votedfor", getApplicationContext()));
                                intent.putExtra("votedTitle", "How Political Parites Are Voted");
                                startActivity(intent);
                                break;
                            case 5:
                                 intent = new Intent(MainActivity.this, ContentDisplay.class);
                                intent.putExtra("history", readSavedData("history", getApplicationContext()));
                                intent.putExtra("hisTitle", "History Of Nigeria");
                                startActivity(intent);
                                break;
                        }
                    }
                })
        );

        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridAdapter();
        mRecyclerView.setAdapter(mAdapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String readSavedData(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }


}
