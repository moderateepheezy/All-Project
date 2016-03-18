package com.example.simpumind.androidxmlparsing;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;


public class SingleXMLParsingActivity extends ActionBarActivity {
    // All variables
    XMLParser parser;
    Document doc;
    String xml;
    ListView lv;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> menuItems;
    ProgressDialog pDialog;

    private String URL = "http://api.androidhive.info/list_paging/?page=1";

    // XML node keys
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    int current_page = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_xmlparsing);

        lv = (ListView)findViewById(R.id.listview);

        menuItems = new ArrayList<HashMap<String, String>>();

        parser = new XMLParser();
        xml = parser.getXmlFromUrl(URL); // getting XML
        doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_ID, parser.getValue(e, KEY_ID)); // id not using any where
            map.put(KEY_NAME, parser.getValue(e, KEY_NAME));

            // adding HashList to ArrayList
            menuItems.add(map);
        }

        // LoadMore button
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");

        // Adding Load More button to lisview at bottom
        lv.addFooterView(btnLoadMore);

        // Getting adapter
        adapter = new ListViewAdapter(this, menuItems);
        lv.setAdapter(adapter);

        /**
         * Listening to Load More button click event
         * */
        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                new loadMoreListView().execute();
            }
        });

        /**
         * Listening to listview single row selected
         * **/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SingleMenuItemActivity.class);
                in.putExtra(KEY_NAME, name);
                startActivity(in);
            }
        });
    }

    /**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class loadMoreListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    SingleXMLParsingActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
                    // increment current page
                    current_page += 1;

                    // Next page request
                    URL = "http://api.androidhive.info/list_paging/?page=" + current_page;

                    xml = parser.getXmlFromUrl(URL); // getting XML
                    doc = parser.getDomElement(xml); // getting DOM element

                    NodeList nl = doc.getElementsByTagName(KEY_ITEM);
                    // looping through all item nodes <item>
                    for (int i = 0; i < nl.getLength(); i++) {
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        Element e = (Element) nl.item(i);

                        // adding each child node to HashMap key => value
                        map.put(KEY_ID, parser.getValue(e, KEY_ID));
                        map.put(KEY_NAME, parser.getValue(e, KEY_NAME));

                        // adding HashList to ArrayList
                        menuItems.add(map);
                    }

                    // get listview current position - used to maintain scroll position
                    int currentPosition = lv.getFirstVisiblePosition();

                    // Appending new data to menuItems ArrayList
                    adapter = new ListViewAdapter(
                            SingleXMLParsingActivity.this,
                            menuItems);
                    lv.setAdapter(adapter);
                    // Setting new scroll position
                    lv.setSelectionFromTop(currentPosition + 1, 0);


            return (null);
        }

        protected void onPostExecute(Void unused) {
            // closing progress dialog
            pDialog.dismiss();
        }
    }
}
