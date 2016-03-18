package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.MixingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.ArrayList;
import java.util.List;

public class MixingDetailed extends AppCompatActivity {

    private ListView listView;
    private MixingAdapter mAdapters;
    private List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    private  List<Mixing> people = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixing_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MixingDetailed.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Baking Manager");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            people = new ArrayList<>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Mixing");

                query.addDescendingOrder("mixerName");
                ob = query.find();
                for (ParseObject mixs : ob) {
                    Mixing dr = new Mixing();
                    dr.setMixerName((String) mixs.get("mixerName"));
                    dr.setStartTime((String) mixs.get("startTime"));
                    dr.setEndTime((String) mixs.get("stoTime"));
                    dr.setTimeCreated(mixs.getCreatedAt().toString());

                    people.add(dr);
                }
            } catch (com.parse.ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapters = new MixingAdapter(MixingDetailed.this, people);
            listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(mAdapters);

            mProgressDialog.dismiss();
        }
    }

}
