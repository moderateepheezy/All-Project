package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.DriverRequestAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.MixingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.DriverRequest;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.ArrayList;
import java.util.List;

public class RequestDetails extends AppCompatActivity {

    private ListView listView;
    private DriverRequestAdapter mAdapters;
    private List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    private  List<DriverRequest> people = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(getApplicationContext(), DriverRequestActivity.class);
                startActivity(i);
            }
        });

        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(RequestDetails.this);
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
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DriverRequest");

                query.addDescendingOrder("timeCreated");
                ob = query.find();
                for (ParseObject mixs : ob) {
                    DriverRequest dr = new DriverRequest();
                    dr.setDriver((String) mixs.get("driverName"));
                    dr.setNoOfBSardine((String) mixs.get("noOfBSardine"));
                    dr.setNoOfSSardine((String) mixs.get("noOfSSardine"));
                    dr.setNoOfRegularLoaves((String) mixs.get("noOfRegular"));
                    dr.setAddress((String) mixs.get("address"));
                    dr.setNoOfCake((String) mixs.get("noOfCake"));
                    dr.setName((String) mixs.get("CustomerName"));
                    dr.setNoOfBurger((String) mixs.get("noOfBurger"));
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
            mAdapters = new DriverRequestAdapter(RequestDetails.this, people);
            listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(mAdapters);

            mProgressDialog.dismiss();
        }
    }

}
