package org.simpumind.com.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;
import com.parse.ui.ParseLoginBuilder;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.simpumind.com.business.org.simpumind.com.business.AddBusiness;
import org.simpumind.com.business.org.simpumind.com.business.Const;
import org.simpumind.com.business.org.simpumind.com.business.DataAdapter;
import org.simpumind.com.business.org.simpumind.com.business.DirectoryData;
import org.simpumind.com.business.org.simpumind.com.business.PropDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private DataAdapter mAdapter;
    private static List<ParseObject> ob;
   // static ProgressDialog mProgressDialog;

    private SwipeRefreshLayout swipeContainer;

    private SearchBox search;
    private Toolbar toolbar;

    FloatingActionButton fab;

    private static  List<DirectoryData> people;
    Intent intent;

    String  busName;
    public static String businessName;

    @Bind(R.id.recycler)
    RecyclerView mRecycler;


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "AfeesLawal";
    private static final String TWITTER_SECRET = "olatunji";




    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }
        people = new ArrayList<>();
        new RemoteDataTask().execute();


        Const.bisName = new ArrayList<>();

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        for(int x = 0; x < people.size(); x++){
            SearchResult option = new SearchResult(people.get(x).getName(), getResources().getDrawable(R.drawable.ic_history));
            search.addSearchable(option);
        }
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                Toast.makeText(MainActivity.this, "Menu click", Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
                final List<DirectoryData> filteredModelList = mAdapter.filter(Const.bisName, term);
                mAdapter.animateTo(filteredModelList);
                mRecycler.scrollToPosition(0);
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(MainActivity.this, searchTerm + " Searched", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });
        search.setOverflowMenu(R.menu.overflow_menu);
        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_menu_item:
                        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                /*mAdapter.clear();
                mAdapter.addAll(people);*/
                new RemoteDataTask().execute();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //initRecycler();

        intent = this.getIntent();

        if(intent != null) {
            businessName = intent.getStringExtra("businessName");
            busName = businessName;
        }
        String current = ParseUser.getCurrentUser().getUsername();
        if (current.equals("Admin")) {
            // The query was successful.
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent i = new Intent(MainActivity.this, AddBusiness.class);
                    i.putExtra("businessName", busName);
                    Toast.makeText(getApplicationContext(), "Business Name: " + busName, Toast.LENGTH_LONG).show();
                    startActivity(i);*/
                    showDialog();
                }
            });
        } else {
        }

        ///new RemoteDataTask().execute();
    }


    public void loadLoginView() {
        Intent intent = new Intent(this, ParseLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            currentUser = null;
            loadLoginView();
        }

        return super.onOptionsItemSelected(item);
    }

    public class RemoteDataTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Business Directory");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                ParseQuery<ParseObject> query = new ParseQuery<>("DirectoryData");
                query.whereEqualTo("business", businessName);
                query.addAscendingOrder("name");
                ob = query.find();
                for(ParseObject business : ob){

                    DirectoryData dr = new DirectoryData();
                    dr.setName((String) business.get("name"));
                    dr.setAddress((String) business.get("address"));
                    dr.setPhoneNo((String) business.get("phone"));
                    dr.setEmail((String) business.get("email"));

                    Const.bisName.add(dr);
                    people.add(dr);
                }
            }catch (com.parse.ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return  null;
        }

        @Override
        protected void onPostExecute(Void result) {

            initRecycler();

            mProgressDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void initRecycler(){
        mAdapter = new DataAdapter();
        mAdapter.addAll(getAllNews());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    private List<DirectoryData> getAllNews(){
        try {
            people = new ArrayList<>();
            ParseQuery<ParseObject> query = new ParseQuery<>("DirectoryData");
            query.whereEqualTo("business", businessName);
            query.addAscendingOrder("name");
            ob = query.find();
            for(ParseObject business : ob){
                DirectoryData dr = new DirectoryData();
                dr.setName((String) business.get("name"));
                dr.setAddress((String) business.get("address"));
                dr.setPhoneNo((String) business.get("phone"));
                dr.setEmail((String) business.get("email"));

                people.add(dr);
            }
        }catch (ParseException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return people;
    }

    private void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = PropDialogFragment.newInstance(businessName);
        newFragment.show(ft, "dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())toolbar.setTitle("");
    }


}
