package org.simpumind.com.loginwithsocial;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

        public class SignActivity extends AppCompatActivity implements
                GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {


            private TwitterLoginButton loginButton;
            TwitterSession session;
            ImageView user_picture;
            String screenname,twitterImage,location,timeZone,description;
            CallbackManager callbackManager;

            GoogleApiClient mGoogleApiClient;
            GoogleSignInOptions gso;
            SignInButton signIn_btn;
            private static final int RC_SIGN_IN = 0;
            ProgressDialog progress_dialog;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                //For authentication, pass consumer key and consumer secret key
                facebookSDKInitialize();
                buidNewGoogleApiClient();
                setContentView(R.layout.activity_sign);
                customizeSignBtn();
                setBtnClickListeners();
                progress_dialog = new ProgressDialog(this);
                progress_dialog.setMessage("Signing in....");
                LoginButton loginButtons = (LoginButton) findViewById(R.id.login_button);
                loginButtons.setReadPermissions("email,publish_actions");
                getLoginDetails(loginButtons);
                loginButton = (TwitterLoginButton) findViewById(R.id.login_button_twitter);
                loginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {

                        session = Twitter.getSessionManager().getActiveSession();

                        Twitter.getApiClient(session).getAccountService()
                                .verifyCredentials(true, false, new Callback<User>() {

                                    @Override
                                    public void success(Result<User> userResult) {

                                        User user = userResult.data;
                                        twitterImage = user.profileImageUrl;
                                        screenname = user.screenName;
                                        location = user.location;
                                        timeZone = user.timeZone;
                                        description = user.email;
                                        loginWithTwitter();

                                    }

                                    @Override
                                    public void failure(TwitterException e) {
                                    }

                                });

                        loginButton.setVisibility(View.GONE);

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("TwitterKit", "Login with Twitter failure", exception);
                    }

                });
            }

            protected void facebookSDKInitialize() {

                FacebookSdk.sdkInitialize(getApplicationContext());

                callbackManager = CallbackManager.Factory.create();
            }

            protected void getLoginDetails(LoginButton login_button){

                // Callback registration
                login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult login_result) {

                        getUserInfo(login_result);
                    }

                    @Override
                    public void onCancel() {
                        // code for cancellation
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //  code to handle error
                    }
                });
            }


            private void loginWithTwitter() {
                Intent intent = new Intent(this,
                        LoginSuccess.class);
                intent.putExtra("name", screenname);
                intent.putExtra("email", location);
                intent.putExtra("pic", twitterImage);
                startActivity(intent);
            }

            protected void getUserInfo(LoginResult login_result){

                GraphRequest data_request = GraphRequest.newMeRequest(
                        login_result.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject json_object,
                                    GraphResponse response) {

                                Intent intent = new Intent(SignActivity.this,LoginSuccess.class);
                                intent.putExtra("jsondata",json_object.toString());
                                startActivity(intent);
                            }
                        });
                Bundle permission_param = new Bundle();
                permission_param.putString("fields", "id,name,email,gender,picture.width(120).height(120)");
                data_request.setParameters(permission_param);
                data_request.executeAsync();

            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                callbackManager.onActivityResult(requestCode, resultCode, data);
                loginButton.onActivityResult(requestCode, resultCode, data);

                if (requestCode == RC_SIGN_IN) {
                    if (resultCode != RESULT_OK) {

                        progress_dialog.dismiss();

                    }
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    getSignInResult(result);
                }
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        Toast.makeText(this, "start sign process", Toast.LENGTH_SHORT).show();
                        gSignIn();
                }
            }

            private void gSignIn() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                progress_dialog.show();
            }

            private void getSignInResult(GoogleSignInResult result) {

                if (result.isSuccess()) {

                    // Signed in successfully, show authenticated UI.
                    GoogleSignInAccount acct = result.getSignInAccount();
                    String name = acct.getDisplayName();
                    String email = acct.getEmail();
                    String dob = acct.getPhotoUrl().toString();
                    Toast.makeText(SignActivity.this, "Logged In!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginSuccess.class);
                    intent.putExtra("username", name);
                    intent.putExtra("googleEmail", email);
                    intent.putExtra("dob", dob);
                    startActivity(intent);

                    updateUI(true);
                 //   progress_dialog.dismiss();
                } else {
                    // Signed out, show unauthenticated UI.
                    Toast.makeText(SignActivity.this, "Not login!", Toast.LENGTH_SHORT).show();
                    updateUI(false);
                }
            }

            private void updateUI(boolean signedIn) {
                if (signedIn) {
                    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                }
            }

            @Override
            public void onConnected(Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }

            @Override
            protected void onStart() {
                super.onStart();
                mGoogleApiClient.connect();
            }

            @Override
            protected void onResume() {
                super.onResume();
                AppEventsLogger.activateApp(this);
            }

            @Override
            protected void onPause() {
                super.onPause();
                AppEventsLogger.deactivateApp(this);
            }

            @Override
            protected void onStop() {
                super.onStop();
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }
            }

            private void buidNewGoogleApiClient(){

                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this )
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }

            private void customizeSignBtn(){

                signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
                signIn_btn.setSize(SignInButton.SIZE_STANDARD);
                signIn_btn.setScopes(gso.getScopeArray());

            }

            private void setBtnClickListeners(){
                // Button listeners
                signIn_btn.setOnClickListener(this);

            }
        }
