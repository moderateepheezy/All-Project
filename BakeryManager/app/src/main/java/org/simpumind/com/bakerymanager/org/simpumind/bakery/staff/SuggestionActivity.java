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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.simpumind.com.bakerymanager.LoginActivity;
import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.MixingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.SuggestionAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Packaging;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends AppCompatActivity {

    EditText sugName;

    ListView listView;

    Button btnSubmit;
    private SuggestionAdapter mAdapters;
    private List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    private  List<Suggestion> people = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sugName = (EditText)findViewById(R.id.etTweetReview);

        listView = (ListView)findViewById(R.id.suggestList);
        btnSubmit = (Button)findViewById(R.id.theReviewBarButton);


        new RemoteDataTask().execute();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save();
            }
        });
    }

    private void save() {
        final ProgressDialog progressDialog = new ProgressDialog(SuggestionActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving...");
        progressDialog.show();



       // Mixing mx = new Mixing();\
        String sug = sugName.getText().toString();

        ParseObject obj = new ParseObject("Suggestion");
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.getUsername();

        obj.put("name", currentUser.getUsername().toString());
        obj.put("suggestion", sug);

        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    new RemoteDataTask().execute();
                    sugName.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Saved Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Save Not Successfully!" + "  " +
                                    e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(getClass().getSimpleName(), "User update error: " + e);
                }
            }
        });
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
                    Intent i = new Intent(SuggestionActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SuggestionActivity.this);
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
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Suggestion");

                query.addDescendingOrder("createdAt");
                ob = query.find();
                for (ParseObject mixs : ob) {
                    Suggestion dr = new Suggestion();
                    dr.setName((String) mixs.get("name"));
                    dr.setSuggestion((String) mixs.get("suggestion"));
                    dr.setCreation(mixs.getCreatedAt().toString());

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
            mAdapters = new SuggestionAdapter(SuggestionActivity.this, people);
            listView = (ListView)findViewById(R.id.suggestList);
            listView.setAdapter(mAdapters);

            mProgressDialog.dismiss();
        }
    }

}
