package com.example.simpumind.tofunmiapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LoadScreenActivity extends ActionBarActivity {
    Button btnSignIn;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        getSupportActionBar().hide();

        btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoadScreenActivity.this,SignInActivity.class);
                startActivity(i);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoadScreenActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
