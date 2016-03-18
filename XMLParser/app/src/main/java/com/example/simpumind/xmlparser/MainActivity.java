package com.example.simpumind.xmlparser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String url2 = "&mode=xml";
    private EditText location,country,temperature,humidity,pressure;
    private HandleXML obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = (EditText)findViewById(R.id.editText1);
        country = (EditText)findViewById(R.id.editText2);
        temperature = (EditText)findViewById(R.id.editText3);
        humidity = (EditText)findViewById(R.id.editText4);
        pressure = (EditText)findViewById(R.id.editText5);
    }

    public void open(View view){
        String url = location.getText().toString();
        String finalUrl = url1 + url + url2;
        country.setText(finalUrl);
        obj = new HandleXML(finalUrl);
        obj.fetchXML();
        while(obj.parsingComplete);
        country.setText(obj.getCountry());
        temperature.setText(obj.getTemperature());
        humidity.setText(obj.getHumidity());
        pressure.setText(obj.getPressure());

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
