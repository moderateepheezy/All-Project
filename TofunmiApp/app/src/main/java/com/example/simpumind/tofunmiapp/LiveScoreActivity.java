package com.example.simpumind.tofunmiapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simpumind.tofunmiapp.com.example.simpumind.home.WallActivity;


public class LiveScoreActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_score);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_score, menu);
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
                i = new Intent(LiveScoreActivity.this, CarRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_chat:
                i = new Intent(LiveScoreActivity.this, ChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_elp:
                i = new Intent(LiveScoreActivity.this, ELPActivity.class);
                startActivity(i);
                break;
            case R.id.action_about_us:
                i = new Intent(LiveScoreActivity.this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_contact_us:
                i = new Intent(LiveScoreActivity.this, ContactUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_horse_race:
                i = new Intent(LiveScoreActivity.this, HorseRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_boxing:
                i = new Intent(LiveScoreActivity.this, BoxingActivity.class);
                startActivity(i);
                break;
            case R.id.action_news:
                i = new Intent(LiveScoreActivity.this, NewsActivity.class);
                startActivity(i);
                break;
            case R.id.action_faqs:
                i = new Intent(LiveScoreActivity.this, FaqsActivity.class);
                startActivity(i);
                break;
            case R.id.action_subscribe:
                i = new Intent(LiveScoreActivity.this, SubscribeActivity.class);
                startActivity(i);
                break;
            case R.id.action_wall:
                i = new Intent(LiveScoreActivity.this, WallActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
