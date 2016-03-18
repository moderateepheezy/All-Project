package org.simpumind.com.bakerymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

public class LoadScreenActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        final ProgressDialog progressDialog = new ProgressDialog(LoadScreenActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading App Contents...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser.getUsername() != null){
            Toast.makeText(getApplicationContext(), "Private Code :" +
            currentUser.get("pcode"), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            if(currentUser.get("pcode").equals("1111")){

                intent = new Intent(LoadScreenActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }else if(currentUser.get("pcode").equals("2222")){
                Toast.makeText(getApplicationContext(), "Not Available, check back later!", Toast.LENGTH_LONG).show();
                intent = new Intent(LoadScreenActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }else if(currentUser.get("pcode").equals("0000")){
                Toast.makeText(getApplicationContext(), "Not Available, check back later!", Toast.LENGTH_LONG).show();
                intent = new Intent(LoadScreenActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            intent = new Intent(LoadScreenActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
