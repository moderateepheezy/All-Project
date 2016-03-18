package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.simpumind.tofunmiapp.AboutUsActivity;
import com.example.simpumind.tofunmiapp.BoxingActivity;
import com.example.simpumind.tofunmiapp.CarRaceActivity;
import com.example.simpumind.tofunmiapp.ContactUsActivity;
import com.example.simpumind.tofunmiapp.ELPActivity;
import com.example.simpumind.tofunmiapp.FaqsActivity;
import com.example.simpumind.tofunmiapp.HorseRaceActivity;
import com.example.simpumind.tofunmiapp.LiveScoreActivity;
import com.example.simpumind.tofunmiapp.NewsActivity;
import com.example.simpumind.tofunmiapp.R;
import com.example.simpumind.tofunmiapp.ChatActivity;
import com.example.simpumind.tofunmiapp.SubscribeActivity;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.custom_grid, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
               // switch (item.getTitle().g)
                if(item.getTitle().equals("Yello Wall")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, WallActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Sport Chat")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);

                    //Start details activity
                    startActivity(intent);
                }

                if(item.getTitle().equals("Car Race")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, CarRaceActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Horse Race")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, HorseRaceActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Live Score")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, LiveScoreActivity.class);

                    //Start details activity
                    startActivity(intent);
                }

                if(item.getTitle().equals("FAQS")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, FaqsActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("ELP")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, ELPActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("News")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, NewsActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Subscribe")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, SubscribeActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Contact Us")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("About Us")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
                if(item.getTitle().equals("Boxing")) {
                    //Create intent
                    Intent intent = new Intent(HomeActivity.this, BoxingActivity.class);

                    //Start details activity
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        String[] x = getResources().getStringArray(R.array.image_names);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap,x[i]));
        }
        return imageItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        switch(id) {
            case R.id.action_wall:
                i = new Intent(HomeActivity.this, WallActivity.class);
                startActivity(i);
                break;
            case R.id.action_car_race:
                i = new Intent(HomeActivity.this, CarRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_chat:
                i = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_elp:
                i = new Intent(HomeActivity.this, ELPActivity.class);
                startActivity(i);
                break;
            case R.id.action_about_us:
                i = new Intent(HomeActivity.this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_contact_us:
                i = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_horse_race:
                i = new Intent(HomeActivity.this, HorseRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_boxing:
                i = new Intent(HomeActivity.this, BoxingActivity.class);
                startActivity(i);
                break;
            case R.id.action_news:
                i = new Intent(HomeActivity.this, NewsActivity.class);
                startActivity(i);
                break;
            case R.id.action_faqs:
                i = new Intent(HomeActivity.this, FaqsActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
