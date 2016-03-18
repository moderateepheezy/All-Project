package org.simpumind.com.loginwithsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

    public class LoginSuccess extends AppCompatActivity {

        JSONObject response, profile_pic_data, profile_pic_url;
        private TextView textView;
        private TextView gender;
        private TextView email;
        private ImageView imageView;

        private ImageView twitterImage, googleImage;
       private TextView twitUserName, twitEmail,
                twitLocation, googleUserName, googleEmail, googleAbout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_success);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            /**
             *assigning facebook views
             **/
            textView = (TextView) findViewById(R.id.name);
            email = (TextView) findViewById(R.id.email);
            gender = (TextView) findViewById(R.id.gender);
            imageView = (ImageView) findViewById(R.id.userImage);
            /**
              *assigning twitter views
             **/
            twitUserName = (TextView) findViewById(R.id.twit_user);
            twitEmail = (TextView) findViewById(R.id.twit_email);
            twitLocation = (TextView) findViewById(R.id.twit_location);
            twitterImage = (ImageView) findViewById(R.id.twit_img);
            /**
             *assigning google views
             **/
            googleUserName = (TextView) findViewById(R.id.google_user);
            googleEmail = (TextView) findViewById(R.id.google_email);
            googleAbout = (TextView) findViewById(R.id.google_about);
            googleImage = (ImageView) findViewById(R.id.google_img);

            /**
             *Get data from class SignActivity intent
             **/

                Intent intent = getIntent();
                if(getIntent().hasExtra("jsondata")) {
                    String jsondata = intent.getStringExtra("jsondata");
                    setUserProfile(jsondata);
                }
                if(intent.hasExtra("name") || intent.hasExtra("email")){
                    getTwitterProflie();
                }
            if(intent.hasExtra("username") || intent.hasExtra("googleEmail")){
                    setGoogleProfile();
                }
        }

        public void setGoogleProfile(){
            Intent i = getIntent();
            String name = i.getStringExtra("username");
            String emails = i.getStringExtra("googleEmail");
            String genders = i.getStringExtra("gender");
            String pic = i.getStringExtra("dob");
            googleUserName.setText(name);
            googleAbout.setText(genders);
            googleEmail.setText(emails);
            Picasso.with(getApplicationContext()).load(pic)
                    .into(googleImage);
        }

        public void setUserProfile(String jsondata){

            try {
                response = new JSONObject(jsondata);
                 email.setText(response.get("email").toString());
                textView.setText(response.get("name").toString());
                gender.setText(response.get("gender").toString());
                profile_pic_data = new JSONObject(response.get("picture").toString());
                profile_pic_url =    new JSONObject(profile_pic_data.getString("data"));

                Picasso.with(this).load(profile_pic_url.getString("url"))
                        .into(imageView);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public void getTwitterProflie(){
            Intent i = getIntent();
            String name = i.getStringExtra("name");
            String emails = i.getStringExtra("email");
            String genders = i.getStringExtra("gender");
            String pic = i.getStringExtra("pic");
            twitUserName.setText(name);
            twitLocation.setText(genders);
            twitEmail.setText(emails);
            Picasso.with(getApplicationContext()).load(pic)
                    .into(twitterImage);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_sign, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            else if(id == R.id.action_add_account){
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }

    }
