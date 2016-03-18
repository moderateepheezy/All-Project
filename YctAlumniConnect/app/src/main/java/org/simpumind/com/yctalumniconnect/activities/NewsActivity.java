package org.simpumind.com.yctalumniconnect.activities;

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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.simpumind.com.yctalumniconnect.R;
import org.simpumind.com.yctalumniconnect.app.AppConst;
import org.simpumind.com.yctalumniconnect.helper.NewsAdapter;
import org.simpumind.com.yctalumniconnect.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {


    private NewsAdapter mAdapters;
    private ListView listView;
    private ProgressDialog progressDialog;
    private List<ParseObject> ob;
    private List<News> allNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new AynsckClass().execute();

    }

    private void getAllNews(){
        try {
            allNews = new ArrayList<>();
            ParseQuery<ParseObject> query = new ParseQuery<>("News");
            //query.fromLocalDatastore();
            query.addAscendingOrder("createdAt");
            ob = query.find();
            for(ParseObject nt: ob){
                News n = new News();
                n.setHeading((String)nt.get("heading"));
                n.setNews((String) nt.get("news"));
                n.setTime(nt.getCreatedAt().toString());

                allNews.add(n);
            }
        }catch (ParseException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        progressDialog.dismiss();
    }

    private class AynsckClass extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewsActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAllNews();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapters = new NewsAdapter(getApplicationContext(), allNews);
            listView = (ListView)findViewById(R.id.news_);
            listView.setAdapter(mAdapters);
            mAdapters.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

}
