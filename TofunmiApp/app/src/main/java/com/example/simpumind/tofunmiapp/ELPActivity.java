package com.example.simpumind.tofunmiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.simpumind.tofunmiapp.com.example.simpumind.home.AllPostAdapter;
import com.example.simpumind.tofunmiapp.com.example.simpumind.home.WallActivity;

public class ELPActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elp);

        ListView listView = (ListView)findViewById(R.id.list_post);
        listView.setAdapter(new AllPostAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elp, menu);
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
                i = new Intent(ELPActivity.this, WallActivity.class);
                startActivity(i);
                break;
            case R.id.action_car_race:
                i = new Intent(ELPActivity.this, CarRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_chat:
                i = new Intent(ELPActivity.this, ChatActivity.class);
                startActivity(i);
                break;
            case R.id.action_horse_race:
                i = new Intent(ELPActivity.this, HorseRaceActivity.class);
                startActivity(i);
                break;
            case R.id.action_news:
                i = new Intent(ELPActivity.this, NewsActivity.class);
                startActivity(i);
                break;
            case R.id.action_faqs:
                i = new Intent(ELPActivity.this, FaqsActivity.class);
                startActivity(i);
                break;
            case R.id.action_subscribe:
                i = new Intent(ELPActivity.this, SubscribeActivity.class);
                startActivity(i);
                break;
            case R.id.action_livescore:
                i = new Intent(ELPActivity.this, LiveScoreActivity.class);
                startActivity(i);
                break;

            case R.id.action_about_us:
                i = new Intent(ELPActivity.this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_contact_us:
                i = new Intent(ELPActivity.this, ContactUsActivity.class);
                startActivity(i);
                break;
            case R.id.action_boxing:
                i = new Intent(ELPActivity.this, BoxingActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
