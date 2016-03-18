package com.example.simpumind.tofunmiapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.simpumind.tofunmiapp.com.example.simpumind.home.HomeActivity;


public class SignInActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        Button btnSingIn = (Button)findViewById(R.id.btnSingIn);

        getSupportActionBar().hide();

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }
}
