package org.simpumind.com.yctalumniconnect.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.simpumind.com.yctalumniconnect.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView middleName;
    private TextView department;

    private Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = (TextView) findViewById(R.id.input_firstnm);
        middleName = (TextView) findViewById(R.id.input_middle_nm);
        lastName = (TextView) findViewById(R.id.input_last_nm);
        department = (TextView) findViewById(R.id.input_department);

        /*ParseQuery myQuery1 = new ParseQuery("myTable");
        myQuery1.whereContainedIn("key", firstName.getText().toString());

        ParseQuery myQuery2 = new ParseQuery("myTable");
        myQuery2.whereContainedIn("key", myList2);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(myQuery1);
        queries.add(myQuery2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);*/
    }

}
