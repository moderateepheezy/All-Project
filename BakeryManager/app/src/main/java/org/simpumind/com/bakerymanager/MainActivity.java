package org.simpumind.com.bakerymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.parse.ParseUser;

import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.BakingActivity;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.DriverRequestActivity;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.MixingActivity;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.PackagingActivity;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.SuggestionActivity;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ParseUser currentUser = ParseUser.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (currentUser.getUsername() == null) {
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //toolbar.setTitle(currentUser.getUsername().toString());
        setSupportActionBar(toolbar);

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        intent = new Intent(MainActivity.this, MixingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, BakingActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, PackagingActivity.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(MainActivity.this, SuggestionActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, DriverRequestActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_center);
        gridView.setAnimation(anim);
        anim.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Handle click events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Setting coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                ParseUser.getCurrentUser().logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(currentUser.getUsername() == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
