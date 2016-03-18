package com.example.simpumind.simplexmlpullparser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;


public class XMLPullParserActivity extends ActionBarActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlpull_parser);

        listView = (ListView)findViewById(R.id.list);

        List<Employee> employees = null;
        try{
            XMLPullParserHandler parser = new XMLPullParserHandler();
            employees = parser.parse(getAssets().open("employees.xml"));
            ArrayAdapter<Employee> adapter =
                    new ArrayAdapter<Employee>(this,R.layout.list_item, employees);
            listView.setAdapter(adapter);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xmlpull_parser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
