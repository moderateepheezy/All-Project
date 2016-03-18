package com.example.simpumind.tofunmiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.simpumind.tofunmiapp.com.example.simpumind.home.AllPostAdapter;
import com.example.simpumind.tofunmiapp.com.example.simpumind.home.FAQSAdapter;
import com.example.simpumind.tofunmiapp.com.example.simpumind.home.WallActivity;

public class FaqsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        ListView listView = (ListView)findViewById(R.id.list_post);
        listView.setAdapter(new FAQSAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faqs, menu);
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
                i = new Intent(FaqsActivity.this, WallActivity.class);
                startActivity(i);
                break;
            case R.id.action_car_race:
                i = new Intent(FaqsActivity.this, CarRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_chat:
                i = new Intent(FaqsActivity.this, ChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_elp:
                i = new Intent(FaqsActivity.this, ELPActivity.class);
                startActivity(i);
                break;
            case R.id.action_horse_race:
                i = new Intent(FaqsActivity.this, HorseRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_news:
                i = new Intent(FaqsActivity.this, NewsActivity.class);
                startActivity(i);
                break;
            case R.id.action_subscribe:
                i = new Intent(FaqsActivity.this, SubscribeActivity.class);
                startActivity(i);
                break;
            case R.id.action_about_us:
                i = new Intent(FaqsActivity.this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_livescore:
                i = new Intent(FaqsActivity.this, LiveScoreActivity.class);
                startActivity(i);
                break;

            case R.id.action_boxing:
                i = new Intent(FaqsActivity.this, BoxingActivity.class);
                startActivity(i);
                break;
            case R.id.action_contact_us:
                i = new Intent(FaqsActivity.this, ContactUsActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
