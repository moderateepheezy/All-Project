package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.SuggestionAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.DriverRequest;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class DriverRequestActivity extends AppCompatActivity {

    EditText noOfCake;
    EditText noOfBurger;
    EditText noOfSSardine;
    EditText noOfBSardine;
    EditText noOfRegular;
    EditText address;
    EditText driverName;
    EditText customer_name;
    Button btnSave;
    TextView entries;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave = (Button)findViewById(R.id.btn_save);
        driverName = (EditText) findViewById(R.id.driver_name);
        address = (EditText) findViewById(R.id.address);
        noOfRegular = (EditText) findViewById(R.id.no_regular_loaves);
        noOfBSardine = (EditText) findViewById(R.id.no_big_sardine);
        noOfSSardine = (EditText) findViewById(R.id.no_small_sardine);
        noOfBurger = (EditText) findViewById(R.id.no_burger);
        noOfCake = (EditText) findViewById(R.id.no_cake);
        customer_name = (EditText) findViewById(R.id.customer_name);
        entries = (TextView) findViewById(R.id.all_entries);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    save();
                    driverName.setText("");
                    noOfCake.setText("");
                    noOfBurger.setText("");
                    noOfSSardine.setText("");
                    noOfBSardine.setText("");
                    address.setText("");
                    noOfRegular.setText("");
            }
        });

        entries.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MixingFragment fragment = new MixingFragment();
                fragmentTransaction.add(R.id.fragment_container, fragment, "MixingFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
                Intent i = new Intent(DriverRequestActivity.this, RequestDetails.class);
                startActivity(i);
                return false;
            }
        });
/*
        driver = (TextView) findViewById(R.id.driverName);
        btnDrive = (Button) findViewById(R.id.chooseDriver);*/


        /*btnDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String[] d = {"Fola", "Tayo", "Salako"};

                queryParse();
            }
        });*/
    }

    /*public void queryParse() {
        final ProgressDialog progressDialog = new ProgressDialog(DriverRequestActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("pcode", "2222");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    driverArray = new String[objects.size()];
                    for (int i = 0; i < objects.size(); i++) {
                        driverArray[i] = objects.get(i).getUsername();
                    }
                    Toast.makeText(getApplicationContext(),
                            "Hurray: ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        alertDailog(driverArray);
    }

    public void alertDailog(final String[] drivers){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverRequestActivity.this);
        builder.setTitle("Drivers");
        builder.setItems(drivers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btnDrive.setText(drivers[i].toString());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

    private void save() {
        final ProgressDialog progressDialog = new ProgressDialog(DriverRequestActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        int noOfCakeI = Integer.parseInt(noOfCake.getText().toString()) * 50;
        int noOfBurgerI = Integer.parseInt(noOfBurger.getText().toString()) * 150;
        int noOfSSardineI = Integer.parseInt(noOfSSardine.getText().toString()) * 130;
        int noOfBSardineI = Integer.parseInt(noOfBSardine.getText().toString()) * 190;
        int noOfRegularI = Integer.parseInt(noOfRegular.getText().toString()) * 200;

        DriverRequest mx = new DriverRequest();

        String a = String.valueOf(noOfBSardineI);
        ParseObject obj = new ParseObject("DriverRequest");
        obj.put("CustomerName", customer_name.getText().toString());
        obj.put("driverName", driverName.getText().toString());
        obj.put("noOfBSardine", noOfBSardine.getText().toString() + "\t Total Amount: " + Integer.valueOf(noOfBSardineI));
        obj.put("noOfSSardine", noOfSSardine.getText().toString()  + "\t Total Amount: " + Integer.valueOf(noOfSSardineI));
        obj.put("noOfRegular", noOfRegular.getText().toString()  + "\t Total Amount: " + Integer.valueOf(noOfRegularI));
        obj.put("noOfBurger", noOfBurger.getText().toString()  + "\t Total Amount: " + Integer.valueOf(noOfBurgerI));
        obj.put("noOfCake", noOfCake.getText().toString()  + "\t Total Amount: " + Integer.valueOf(noOfCakeI));
        obj.put("address", address.getText().toString());

        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
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
}
