package org.simpumind.com.business.org.simpumind.com.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginActivity;

import org.simpumind.com.business.MainActivity;
import org.simpumind.com.business.R;

public class AddBusiness extends AppCompatActivity {

    private EditText busName;
    private EditText busAddress;
    private EditText busPhone;
    private EditText busEmail;

    String businessName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        busName =(EditText)findViewById(R.id.fname);
        busAddress =(EditText)findViewById(R.id.lname);
        busPhone =(EditText)findViewById(R.id.uname);
        busEmail =(EditText)findViewById(R.id.email);


        intent = this.getIntent();

        if(intent != null) {
            businessName = intent.getStringExtra("businessName");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void loadLoginView() {
        Intent intent = new Intent(this, ParseLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ParseUser currentUser = ParseUser.getCurrentUser();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            currentUser = null;

            loadLoginView();
        }

        return super.onOptionsItemSelected(item);
    }

}
