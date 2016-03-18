package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.simpumind.tofunmiapp.AboutUsActivity;
import com.example.simpumind.tofunmiapp.BoxingActivity;
import com.example.simpumind.tofunmiapp.CarRaceActivity;
import com.example.simpumind.tofunmiapp.ChatActivity;
import com.example.simpumind.tofunmiapp.ContactUsActivity;
import com.example.simpumind.tofunmiapp.ELPActivity;
import com.example.simpumind.tofunmiapp.FaqsActivity;
import com.example.simpumind.tofunmiapp.HorseRaceActivity;
import com.example.simpumind.tofunmiapp.LiveScoreActivity;
import com.example.simpumind.tofunmiapp.NewsActivity;
import com.example.simpumind.tofunmiapp.R;

public class WallActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        ListView listView = (ListView)findViewById(R.id.list_post);
        listView.setAdapter(new PostAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wall, menu);
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
            case R.id.action_car_race:
                i = new Intent(WallActivity.this, CarRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_chat:
                i = new Intent(WallActivity.this, ChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_elp:
                i = new Intent(WallActivity.this, ELPActivity.class);
                startActivity(i);
                break;
            case R.id.action_about_us:
                i = new Intent(WallActivity.this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_contact_us:
                i = new Intent(WallActivity.this, ContactUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_horse_race:
                i = new Intent(WallActivity.this, HorseRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_boxing:
                i = new Intent(WallActivity.this, BoxingActivity.class);
                startActivity(i);
                break;
            case R.id.action_news:
                i = new Intent(WallActivity.this, NewsActivity.class);
                startActivity(i);
                break;
            case R.id.action_faqs:
                i = new Intent(WallActivity.this, FaqsActivity.class);
                startActivity(i);
                break;
            case R.id.action_subscribe:
                i = new Intent(WallActivity.this, LiveScoreActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
