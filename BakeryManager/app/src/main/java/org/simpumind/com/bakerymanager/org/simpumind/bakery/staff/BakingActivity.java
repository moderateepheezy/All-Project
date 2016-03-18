package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BakingActivity extends AppCompatActivity {

    EditText bakerName;
    EditText startTime;
    EditText stopTime;
    EditText duration;
    TextView entries;
    Button btnSave;

    private Time today;

    private String TimeStart;

    private String mixName;
    private String start;
    private String stop;

    private Date d1;
    private Date d2;

    private static String ty;
    long offset1, offset2, dur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSave = (Button) findViewById(R.id.btn_save);

        bakerName = (EditText) findViewById(R.id.mixer_name);
        startTime = (EditText) findViewById(R.id.input_start_time);
        stopTime = (EditText) findViewById(R.id.input_end_time);
        duration = (EditText) findViewById(R.id.duration);
        entries = (TextView) findViewById(R.id.all_entries);


        startTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                TimeStart = today.format("%k:%M:%S");
                offset1 = today.toMillis(false);

                startTime.setText(TimeStart.toString());
                return false;
            }
        });

        stopTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                TimeStart = today.format("%k:%M:%S");
               offset2 = today.toMillis(false);
                stopTime.setText(TimeStart.toString());
               // String ty = String.valueOf(dur);

                dur = offset2 - offset1;
               ty = String.format("%d min, %d sec",
                       TimeUnit.MILLISECONDS.toMinutes(dur),
                       TimeUnit.MILLISECONDS.toSeconds(dur) -
                               TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dur))
               );

                duration.setText(ty.toString());
                return false;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    save();
                    bakerName.setText("");
                    startTime.setText("");
                    stopTime.setText("");
                    duration.setText("");
                } else {

                }
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
                Intent i = new Intent(BakingActivity.this, BakingDetail.class);
                startActivity(i);
                return false;
            }
        });

    }

    private void save() {
        final ProgressDialog progressDialog = new ProgressDialog(BakingActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving...");
        progressDialog.show();


        Mixing mx = new Mixing();

        ParseObject obj = new ParseObject("Baking");
        obj.put("mixerName", mixName);
        obj.put("startTime", start);
        obj.put("stopTime", stop);
        obj.put("duration", ty.toString());

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

    public boolean validate(){

        boolean valid = true;
        mixName  = bakerName.getText().toString();
        start = startTime.getText().toString();
        stop = stopTime.getText().toString();

        if(mixName.length() < 3 || mixName.isEmpty()){
            bakerName.setError("Enter More Than 3 Character!");
            valid = false;
        }else {
            bakerName.setError(null);
        }
        if(stop.length() == 0){
            stopTime.setError("Click To Enter Set Time!");
            valid = false;
        }else {
            bakerName.setError(null);
        }
        if(start.length() == 0){
            startTime.setError("Click To Enter Set Time!");
            valid = false;
        }else {
            bakerName.setError(null);
        }

        return valid;
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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
