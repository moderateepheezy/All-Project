package org.simpumind.com.business.org.simpumind.com.business;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;

import org.simpumind.com.business.MainActivity;
import org.simpumind.com.business.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class businessGridActivity extends AppCompatActivity {

    private ParseUser currentUser;
    Intent  intent;
    private ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        final GridView gridView = (GridView) findViewById(R.id.gv);

        items = new ArrayList<>();

        items.add(new Item("BUSINESS SERVICES", R.drawable.business_service));
        items.add(new Item("COMPUTERS & INTERNET", R.drawable.computer_internet));
        items.add(new Item("ENTERTAINMENT & MEDIA", R.drawable.entertainment_media));
        items.add(new Item("EVENTS & CONFERENCES", R.drawable.event_conference));
        items.add(new Item("FINANCES & INSURANCE", R.drawable.finace_insurance));
        items.add(new Item("FOOD & DRINK", R.drawable.food_drink));
        items.add(new Item("HEALTH & BEAUTY", R.drawable.health_beauty));
        items.add(new Item("LEGAL", R.drawable.legal));
        items.add(new Item("MANUFACTURING & INDUSTRY", R.drawable.manufacturing_industry));
        items.add(new Item("SHOPPING", R.drawable.shopping));
        items.add(new Item("TOURISM & ACCOMMODATION", R.drawable.tourism_accomodation));
        items.add(new Item("TRADESMEN & CONSTRUCTION", R.drawable.tradesmen_construction));
        items.add(new Item("TRANSPORT & MOTORING", R.drawable.transport_motoring));
        items.add(new Item("PUBLIC & SOCIAL SERVICES", R.drawable.public_social_service));
        items.add(new Item("PROPERTY", R.drawable.properties));

        gridView.setAdapter(new MyAdapter(this, items));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Item in = new Item();
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem =items.get(i).getName().toString();
                Toast.makeText(getApplicationContext(), "selected item: " + selectedItem, Toast.LENGTH_LONG).show();
                intent = new Intent(businessGridActivity.this, MainActivity.class);
                        intent.putExtra("businessName", selectedItem);
                        startActivity(intent);
            }
        });

    }

    public void loadLoginView() {
        Intent intent = new Intent(this, ParseLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
